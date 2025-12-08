/*
 * Copyright 2020 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.hugerte;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.FocusOption;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.Style.Overflow;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

/**
 * A Rich Text editor, based on HugeRTE JS component.
 * <p>
 * @author mstahv
 */
//@Tag("vaadin-huge-rte")
@JavaScript("context://frontend/hugerteConnector.js")
@StyleSheet("context://frontend/hugerteLumo.css")
public class HugeRte extends CustomField<String>
        implements HasSize, HasThemeVariant<HugeRteVariant> {

    public static final String DEFAULT_HEIGHT = "500px";
    private final DomListenerRegistration domListenerRegistration;
    private String id;
    private boolean initialContentSent;
    private String currentValue = "";
    private String rawConfig;
    Map<String,Object> config = new HashMap<>();
    private final Element editorContainer = new Element("div");
    private boolean connectorInitialized;

    private final int debounceTimeout = 0;
    private boolean basicEditorCreated;
    private boolean enabled = true;
    private boolean readOnly = false;
    private ResizeDirection resizeDirection = ResizeDirection.NONE;
    private Plugin[] activePlugins = {};

    /**
     * Creates a new instance. Use the different `configure` methods to apply any necessary configuration before
     * attaching it.
     */
    public HugeRte() {
        super("");
        addClassName("vaadin-huge-rte");
        setHeight(DEFAULT_HEIGHT);
        getStyle().setOverflow(Overflow.AUTO); // see https://github.com/parttio/hugerte-for-flow/issues/9

        editorContainer.getStyle().set("height", "100%");
        getElement().appendChild(editorContainer);

        configureResize(ResizeDirection.NONE); // by default no resizing

        domListenerRegistration = getElement().addEventListener("tchange",
                (DomEventListener) event -> {
                    boolean value = event.getEventData().has("event.htmlString");
                    String htmlString = event.getEventData()
                            .get("event.htmlString").asString();
                    currentValue = htmlString;
                    setModelValue(htmlString, true);
                });
        domListenerRegistration.addEventData("event.htmlString");
        domListenerRegistration.debounce(debounceTimeout);

        addBlurListener(e -> closeToolbarOverflowMenu());

    }

    /**
     * Creates a new instance with the given label.
     * @param label label
     */
    public HugeRte(String label) {
        this();
        setLabel(label);
    }

    /**
     * Creates a new instance with the given label and initial value. The initial value is set as it is without
     * any further processing.
     * @param label label
     * @param initialValue initial value
     */
    public HugeRte(String label, String initialValue) {
        this();
        setLabel(label);
        setValue(initialValue);
    }

    /**
     * Creates a new instance with the given label, initial value and value change listener. The initial value is set as it is without
     * any further processing.
     * @param label label
     * @param initialValue initial value
     * @param valueChangeListener value change listener
     */
    public HugeRte(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<CustomField<String>, String>> valueChangeListener) {
        this();
        setLabel(label);
        setValue(initialValue);
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Creates a new instance with the given label and value change listener.
     * @param label label
     * @param valueChangeListener value change listener
     */
    public HugeRte(String label, ValueChangeListener<? super ComponentValueChangeEvent<CustomField<String>, String>> valueChangeListener) {
        this();
        setLabel(label);
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Creates a new instance with the given value change listener.
     * @param valueChangeListener value change listener
     */
    public HugeRte(ValueChangeListener<? super ComponentValueChangeEvent<CustomField<String>, String>> valueChangeListener) {
        this();
        addValueChangeListener(valueChangeListener);
    }

    @Override
    protected String generateModelValue() {
        return currentValue;
    }

    /**
     * Define the mode of value change triggering. BLUR: Value is triggered only
     * when HugeRTE loses focus, TIMEOUT: HugeRTE will send value change eagerly
     * but debounced with timeout, CHANGE: value change is sent when HugeRTE
     * emits change event (e.g. enter, tab)
     *
     * @see #setDebounceTimeout(int)
     * @param mode
     *            The mode.
     */
    public void setValueChangeMode(ValueChangeMode mode) {
        if (mode == ValueChangeMode.BLUR) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setMode", "blur");
            });
        } else if (mode == ValueChangeMode.TIMEOUT) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setMode", "timeout");
            });
        } else if (mode == ValueChangeMode.CHANGE) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setMode", "change");
            });
        }
    }

    /**
     * Sets the debounce timeout for the value change event. The default is 0,
     * when value change is triggered on blur and enter key presses. When value
     * is more than 0 the value change is emitted with delay of given timeout
     * milliseconds after last keystroke.
     *
     * @see #setValueChangeMode(ValueChangeMode)
     * @param debounceTimeout
     *            the debounce timeout in milliseconds
     */
    public void setDebounceTimeout(int debounceTimeout) {
        if (debounceTimeout > 0) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setEager", "timeout");
            });
        } else {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setEager", "change");
            });
        }
        domListenerRegistration.debounce(debounceTimeout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (id == null) {
            id = UUID.randomUUID().toString();
            editorContainer.setAttribute("id", id);
        }
        if (!getEventBus().hasListener(BlurEvent.class)) {
            // adding fake blur listener so throttled value
            // change events happen by latest at blur
            addBlurListener(e -> {
            });
        }
        if (!attachEvent.isInitialAttach()) {
            // Value after initial attach should be set via HugeRTE JavaScript
            // API, otherwise value is not updated upon reattach
            initialContentSent = true;
        }
        super.onAttach(attachEvent);
        if (attachEvent.isInitialAttach())
            injectEditorScript();
        initConnector();
        saveOnClose();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // See https://github.com/parttio/tinymce-for-flow/issues/33
        if (isVisible()) {
            detachEvent.getUI().getPage().executeJs("""
                    hugerte.get($0).remove();
                    """, id);
        }
        super.onDetach(detachEvent);
        initialContentSent = false;
        connectorInitialized = false;
        // save the current value to the dom element in case the component gets
        // reattached
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> {
            if(rawConfig == null) {
                rawConfig = "{}";
            }
            ui.getPage().executeJs(
                    "const editor = $0;" +
                    "const rawconfig = " + rawConfig + ";\n" +
                    "window.Vaadin.Flow.hugerteConnector.initLazy(rawconfig, $0, $1, $2, $3, $4)",
                    getElement(), editorContainer, config, currentValue,
                    (enabled && !readOnly))
                    .then(res -> initialContentSent = true);

            connectorInitialized = true;
        });
    }
    
    private void saveOnClose(){
        runBeforeClientResponse(ui -> {
            getElement().callJsFunction("$connector.saveOnClose");});
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    @Deprecated
    public String getCurrentValue() {
        return getValue();
    }

    /**
     * Sets the base configuration object as RAW JS. So be very careful what you pass in here.
     *
     * @param jsConfig config to apply
     */
    public void setConfig(String jsConfig) {
        checkAlreadyInitialized();
        this.rawConfig = jsConfig;
    }

    public HugeRte configure(String configurationKey, String value) {
        checkAlreadyInitialized();
        config.put(configurationKey, value);
        return this;
    }

    public HugeRte configure(String configurationKey, String... value) {
        checkAlreadyInitialized();
        config.put(configurationKey, value);
        return this;
    }

    public HugeRte configure(String configurationKey, boolean value) {
        checkAlreadyInitialized();
        config.put(configurationKey, value);
        return this;
    }

    public HugeRte configure(String configurationKey, double value) {
        checkAlreadyInitialized();
        config.put(configurationKey, value);
        return this;
    }

    public HugeRte configureLanguage(Language language) {
        String code = language.getCode();
        if (code != null) { // English has no code, since it has no dedicated lang file.
            config.put("language", code);
        }
        return this;
    }

    /**
     * Replaces text in the editors selection (can be just a caret position).
     * <p>
     *     Note, that this updates the value on the client-side on the next round-trip,
     *     so the value on the server side is not necessarily up-to-date, right after this
     *     call, but will be synced soon and a value change event will be fired after a
     *     small timeout.
     * </p>
     *
     * @param htmlString
     *            the html snippet to be inserted
     */
    public void replaceSelectionContent(String htmlString) {
        runBeforeClientResponse(ui -> getElement().callJsFunction(
                "$connector.replaceSelectionContent", htmlString));
    }

    /**
     * Injects actual editor script to the host page from the add-on bundle.
     * <p>
     * Override this with an empty implementation if you to use the cloud hosted
     * version, or own custom script if needed.
     */
    protected void injectEditorScript() {
        getUI().orElseThrow().getPage().addJavaScript(
                "context://frontend/hugerte_addon/hugerte/hugerte.min.js");
    }

    @Override
    public void focus(FocusOption... options) {
        runBeforeClientResponse(ui -> {
            // Dialog has timing issues...
            getElement().executeJs("""
                    const el = this;
                    if(el.$connector.isInDialog()) {
                        setTimeout(() => {
                            el.$connector.focus()
                        }, 150);
                    } else {
                        el.$connector.focus();
                    }
                    """);
            ;
        });
    }

    @Override
    public Registration addFocusListener(ComponentEventListener<FocusEvent<CustomField<String>>> listener) {
        DomListenerRegistration domListenerRegistration = getElement()
                .addEventListener("tfocus", event -> listener
                        .onComponentEvent(new FocusEvent<>(this, true)));
        return domListenerRegistration;
    }

    @Override
    public Registration addBlurListener(ComponentEventListener<BlurEvent<CustomField<String>>> listener) {
        DomListenerRegistration domListenerRegistration = getElement()
                .addEventListener("tblur", event -> listener
                        .onComponentEvent(new BlurEvent<>(this, true)));
        return domListenerRegistration;
    }

    @Override
    public void blur() {
        throw new UnsupportedOperationException(
                "Not implemented, HugeRTE does not support programmatic blur.");
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        adjustEnabledState();
    }

    private void adjustEnabledState() {
        boolean reallyEnabled = this.enabled && !this.readOnly;
        super.setEnabled(reallyEnabled);
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setEnabled", reallyEnabled));
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        super.setReadOnly(readOnly);
        adjustEnabledState();
    }

    @Override
    protected void setPresentationValue(String html) {
        this.currentValue = html;
        if (initialContentSent) {
            runBeforeClientResponse(ui -> getElement()
                    .callJsFunction("$connector.setEditorContent", html));
        }
    }

    private HugeRte initBasicEditorConfiguration() {
        setValue("");
        this.configure("branding", false);
        this.basicEditorCreated = true;
        this.configurePlugins(Plugin.ADVLIST, Plugin.AUTOLINK,
                Plugin.LISTS, Plugin.SEARCH_REPLACE);
        this.configureMenubar(Menubar.FILE, Menubar.EDIT, Menubar.VIEW,
                Menubar.FORMAT);
        this.configureToolbar(Toolbar.UNDO, Toolbar.REDO,
                Toolbar.SEPARATOR, Toolbar.BLOCKS, Toolbar.SEPARATOR,
                Toolbar.BOLD, Toolbar.ITALIC, Toolbar.SEPARATOR,
                Toolbar.ALIGN_LEFT, Toolbar.ALIGN_CENTER, Toolbar.ALIGN_RIGHT,
                Toolbar.ALIGN_JUSTIFY, Toolbar.SEPARATOR, Toolbar.OUTDENT,
                Toolbar.INDENT);
        return this;

    }

    public HugeRte configurePlugins(Plugin... plugins) {
        return configurePlugins(false, plugins);
    }

    public HugeRte configurePlugins(boolean setupBasicConfig, Plugin... plugins) {
        checkAlreadyInitialized();

        this.activePlugins = plugins;

        checkForResizeConflicts();

        if (setupBasicConfig && !basicEditorCreated) {
            initBasicEditorConfiguration();
        }

        if (Set.of(plugins).contains(Plugin.AUTORESIZE)) {
            setHeight(null);
        }

        Object[] o = (Object[]) config.get("plugins");
        int oldLenght = o == null ? 0 :o.length;

        Object[] newPlugins = new Object[oldLenght + plugins.length];
        for(int i = 0; i < newPlugins.length; i++) {
            if(i < oldLenght) {
                newPlugins[i] = o[i];
            } else {
                newPlugins[i] = plugins[i -oldLenght].pluginLabel;
            }
        }
        config.put("plugins", newPlugins);
        return this;
    }

    public HugeRte configureMenubar(Menubar... menubars) {
        return configureMenubar(false, menubars);
    }

    public HugeRte configureMenubar(boolean setupBasicConfig, Menubar... menubars) {
        checkAlreadyInitialized();

        if (setupBasicConfig && !basicEditorCreated) {
            initBasicEditorConfiguration();
        }

        String newconfig = Arrays.stream(menubars).map(m -> m.menubarLabel)
                .collect(Collectors.joining(" "));

        String menubar;
        if (config.get("menubar") != null) {
            menubar = (String) config.get("menubar");
            menubar = menubar + " " + newconfig;
        } else {
            menubar = newconfig;
        }

        config.put("menubar", menubar);
        return this;
    }

    public HugeRte configureToolbar(Toolbar... toolbars) {
        return configureToolbar(false, toolbars);
    }

    public HugeRte configureToolbar(boolean setupBasicConfig, Toolbar... toolbars) {
        checkAlreadyInitialized();

        if (setupBasicConfig && !basicEditorCreated) {
            initBasicEditorConfiguration();
        }

        String toolbarStr = (String) config.get("toolbar");
        if(toolbarStr == null) {
            toolbarStr = "";
        }

        for (int i = 0; i < toolbars.length; i++) {
            toolbarStr = toolbarStr.concat(" ").concat(toolbars[i].toolbarLabel)
                    .concat(" ");
        }

        config.put("toolbar", toolbarStr);
        return this;
    }

    /**
     * Configures the resize option for the editor. Please note, that there are additional settings to this instance
     * necessary, so it is not recommended to call directly {@code configure("resize", ...)}.
     * @param direction direction
     * @return this instance
     */
    public HugeRte configureResize(ResizeDirection direction) {
        this.resizeDirection = Objects.requireNonNull(direction);
        checkForResizeConflicts();

        if (direction != ResizeDirection.NONE) {
            setHeight(null); // otherwise resize will not work as expected

            if (direction == ResizeDirection.VERTICALLY) {
                return configure("resize", true);
            }

            return configure("resize", "both");
        }

        // fallback, if configure resize is used multiple times
        setHeight(DEFAULT_HEIGHT);
        return configure("resize", false);
    }

    /**
     * Ensures, that resize and autoresize are not enabled at the same time.
     */
    private void checkForResizeConflicts() {
        if (resizeDirection != ResizeDirection.NONE && Set.of(activePlugins).contains(Plugin.AUTORESIZE)) {
            throw new ConfigurationConflictException("The \"resize\" configuration and the \"autoresize\" plugin are" +
                                                     " not compatible. Please check you configuration.");
        }
    }

    private void checkAlreadyInitialized() {
        if (connectorInitialized) {
            throw new AlreadyInitializedException();
        }
    }

    public void closeToolbarOverflowMenu() {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.closeToolbarOverflowMenu"));
    }

    /**
     * Thrown, when the editor already has been initialized. For instance, the configuration must not be changed
     * afterwards and is a typical source for this exception.
     */
    public static class AlreadyInitializedException extends RuntimeException {
        public AlreadyInitializedException() {
            super("Cannot apply configuration to the editor, it already has been initialized. You need to " +
                  "detach it first");
        }
    }

    /**
     * Thrown, when a conflict in the configuration has been detected. This is for instance the case, when
     * a plugin and another configuration should not be used in combination (e.g. config "resize" and plugin "autoresize").
     */
    public static class ConfigurationConflictException extends RuntimeException {
        public ConfigurationConflictException(String message) {
            super(message);
        }
    }

    /**
     * Directions, that can be set for the editor to be resizable. Please note, that "only horizonzal" resizing is
     * not supported.
     *
     * @see <a href="https://www.tiny.cloud/docs/tinymce/latest/editor-size-options/#resize">Official TinyMCE docs</a>
     */
    public enum ResizeDirection {
        /**
         * Resizing is disabled.
         */
        NONE,

        /**
         * Enables vertical resizing.
         */
        VERTICALLY,

        /**
         * Enables vertical and horizontal resizing.
         */
        BOTH;
    }
}
