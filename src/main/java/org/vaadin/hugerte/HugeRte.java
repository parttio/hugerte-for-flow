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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Style.Overflow;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * A Rich Text editor, based on HugeRTE JS component.
 * <p>
 *
 * @author mstahv
 */
@Tag("vaadin-huge-rte")
@JavaScript("context://frontend/hugerte_addon/hugerte/hugerte.min.js")
@JsModule("./vaadin-huge-rte.js")
@StyleSheet("context://frontend/hugerteLumo.css")
public class HugeRte extends AbstractField<HugeRte, String>
        implements
        HasSize, HasThemeVariant<HugeRteVariant>, HasLabel, Focusable<HugeRte>, HasTooltip {

    public static final String DEFAULT_HEIGHT = "500px";
    private final DomListenerRegistration domListenerRegistration;
    JsonObject config = Json.createObject();
    private boolean connectorInitialized;

    private final int debounceTimeout = 0;
    private boolean basicEditorCreated;
    private boolean enabled = true;
    private boolean readOnly = false;
    private ResizeDirection resizeDirection = ResizeDirection.NONE;
    private Plugin[] activePlugins = {};

    /**
     * Creates a new instance with the given label.
     *
     * @param label label
     */
    public HugeRte(String label) {
        this();
        setLabel(label);
    }

    /**
     * Creates a new instance with the given label and initial value. The initial value is set as it is without
     * any further processing.
     *
     * @param label        label
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
     *
     * @param label               label
     * @param initialValue        initial value
     * @param valueChangeListener value change listener
     */
    public HugeRte(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<HugeRte, String>> valueChangeListener) {
        this();
        setLabel(label);
        setValue(initialValue);
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Creates a new instance with the given label and value change listener.
     *
     * @param label               label
     * @param valueChangeListener value change listener
     */
    public HugeRte(String label, ValueChangeListener<? super ComponentValueChangeEvent<HugeRte, String>> valueChangeListener) {
        this();
        setLabel(label);
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Creates a new instance with the given value change listener.
     *
     * @param valueChangeListener value change listener
     */
    public HugeRte(ValueChangeListener<? super ComponentValueChangeEvent<HugeRte, String>> valueChangeListener) {
        this();
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Creates a new instance. Use the different `configure` methods to apply any necessary configuration before
     * attaching it.
     */
    public HugeRte() {
        super("");
        addClassName("vaadin-huge-rte");
        setHeight(DEFAULT_HEIGHT);
        getStyle().setOverflow(Overflow.AUTO); // see https://github.com/parttio/hugerte-for-flow/issues/9

//        editorContainer.getStyle().set("height", "100%");
//        getElement().appendChild(editorContainer);

        configureResize(ResizeDirection.NONE); // by default no resizing

        domListenerRegistration = getElement().addEventListener("tchange",
                (DomEventListener) event -> {
//                    boolean value = event.getEventData().hasKey("event.htmlString");
                    String htmlString = event.getEventData().getString("event.htmlString");
                    setModelValue(htmlString, true);
//                    this.value = htmlString;
                });
        domListenerRegistration.addEventData("event.htmlString");
        domListenerRegistration.debounce(debounceTimeout);

        addBlurListener(e -> closeToolbarOverflowMenu());

    }

    /**
     * Define the mode of value change triggering. BLUR: Value is triggered only
     * when HugeRTE loses focus, TIMEOUT: HugeRTE will send value change eagerly
     * but debounced with timeout, CHANGE: value change is sent when HugeRTE
     * emits change event (e.g. enter, tab)
     *
     * @param mode The mode.
     * @see #setDebounceTimeout(int)
     */
    public void setValueChangeMode(ValueChangeMode mode) {
        if (mode == ValueChangeMode.BLUR) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("setMode", "blur");
            });
        } else if (mode == ValueChangeMode.TIMEOUT) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("setMode", "timeout");
            });
        } else if (mode == ValueChangeMode.CHANGE) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("setMode", "change");
            });
        }
    }

    /**
     * Sets the debounce timeout for the value change event. The default is 0,
     * when value change is triggered on blur and enter key presses. When value
     * is more than 0 the value change is emitted with delay of given timeout
     * milliseconds after last keystroke.
     *
     * @param debounceTimeout the debounce timeout in milliseconds
     * @see #setValueChangeMode(ValueChangeMode)
     */
    public void setDebounceTimeout(int debounceTimeout) {
        if (debounceTimeout > 0) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("setEager", "timeout");
            });
        } else {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("setEager", "change");
            });
        }
        domListenerRegistration.debounce(debounceTimeout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (getId().isEmpty()) {
            setId(UUID.randomUUID().toString());
        }
        if (!getEventBus().hasListener(BlurEvent.class)) {
            // adding fake blur listener so throttled value
            // change events happen by latest at blur
            addBlurListener(e -> {
            });
        }
        super.onAttach(attachEvent);
        runBeforeClientResponse(ui -> {
            getElement().setPropertyJson("config", config);

            // to prevent any additional config afterwards
            connectorInitialized = true;
        });
//        saveOnClose();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // See https://github.com/parttio/tinymce-for-flow/issues/33
        if (isVisible()) {
            detachEvent.getUI().getPage().executeJs("""
                    hugerte.get($0).remove();
                    """, getId().orElse(""));
        }
        super.onDetach(detachEvent);
        connectorInitialized = false;
        // save the current value to the dom element in case the component gets
        // reattached
    }

    private void saveOnClose() {
        runBeforeClientResponse(ui -> {
            getElement().callJsFunction("saveOnClose");
        });
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    /**
     * Sets the base configuration object as RAW JS. So be very careful what you pass in here.
     * Any additional config passed via the configure methods will be applied on top of this config.
     *
     * @param jsConfig config to apply
     */
    public void setConfig(String jsConfig) {
        checkAlreadyInitialized();
        Objects.requireNonNull(jsConfig, "config cannot be null");
        getElement().setPropertyJson("baseConfig", Json.parse(jsConfig));
    }

    public HugeRte configure(String configurationKey, String value) {
        checkAlreadyInitialized();
        config.put(configurationKey, value);
        return this;
    }

    public HugeRte configure(String configurationKey, String... value) {
        checkAlreadyInitialized();
        JsonArray array = Json.createArray();
        for (int i = 0; i < value.length; i++) {
            array.set(i, value[i]);
        }
        config.put(configurationKey, array);
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
     * Note, that this updates the value on the client-side on the next round-trip,
     * so the value on the server side is not necessarily up-to-date, right after this
     * call, but will be synced soon and a value change event will be fired after a
     * small timeout.
     * </p>
     *
     * @param htmlString the html snippet to be inserted
     */
    public void replaceSelectionContent(String htmlString) {
        runBeforeClientResponse(ui -> getElement().callJsFunction(
                "replaceSelectionContent", htmlString));
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
    public Registration addFocusListener(ComponentEventListener<FocusEvent<HugeRte>> listener) {
        DomListenerRegistration domListenerRegistration = getElement()
                .addEventListener("tfocus", event -> listener
                        .onComponentEvent(new FocusEvent<>(this, true)));
        return domListenerRegistration;
    }

    @Override
    public Registration addBlurListener(ComponentEventListener<BlurEvent<HugeRte>> listener) {
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
        Focusable.super.setEnabled(reallyEnabled);
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("setEnabled", reallyEnabled));
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        super.setReadOnly(readOnly);
        adjustEnabledState();
    }

    @Override
    protected void setPresentationValue(String html) {
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("setEditorContent", html));
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

        JsonArray jsonArray = config.get("plugins");
        int initialIndex = 0;

        if (jsonArray != null) {
            initialIndex = jsonArray.length();
        } else {
            jsonArray = Json.createArray();
        }

        for (Plugin plugin : plugins) {
            jsonArray.set(initialIndex, plugin.pluginLabel);
            initialIndex++;
        }

        config.put("plugins", jsonArray);
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
        if (config.hasKey("menubar")) {
            menubar = config.getString("menubar");
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

        JsonValue jsonValue = config.get("toolbar");
        String toolbarStr = "";

        if (jsonValue != null) {
            toolbarStr = toolbarStr.concat(jsonValue.asString());
        }

        for (Toolbar toolbar : toolbars) {
            toolbarStr = toolbarStr.concat(" ").concat(toolbar.toolbarLabel)
                    .concat(" ");
        }

        config.put("toolbar", toolbarStr);
        return this;
    }

    /**
     * Configures the resize option for the editor. Please note, that there are additional settings to this instance
     * necessary, so it is not recommended to call directly {@code configure("resize", ...)}.
     *
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
                .callJsFunction("closeToolbarOverflowMenu"));
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
        BOTH
    }
}
