package org.vaadin.hugerte;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Patch;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.InputNotifier;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.dom.Element;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * A Rich Text editor, based on HugeRTE JS component.
 * <p>
 *
 * @author mstahv, Stefan Uebe
 */
@Tag("vaadin-huge-rte")
//@NpmPackage(value = "hugerte", version = "1.0.9") // > Part of the npm integration (not yet working, therefore commented out)
@NpmPackage(value = "diff-match-patch", version = "1.0.5")
@JsModule("./vaadin-huge-rte.js")
@CssImport("./vaadin-huge-rte.css")
public class HugeRte extends AbstractSinglePropertyField<HugeRte, String> implements
        HasValidationProperties, HasValidator<String>, InputNotifier, /*TODO KeyNotifier,*/
        HasSize, HasStyle, Focusable<HugeRte>, HasLabel, HasHelper {

    // TODO enable / readonly
    // TODO field styling (e.g. label)
    // TODO theme variants
    // TODO replace selection
    // TODO static "createInstanceWithDefaultConfig()"
    // TODO tooltips
    // TODO close toolbar
    // TODO test in dialog
    // TODO KeyNotifier
    // TODO Demos
    // TODO review and finalize npm integration (see commented out code)
    // TODO dynamic plugin loading for npm integration

    public static final int DEFAULT_VALUE_CHANGE_MODE_TIMEOUT = 2000;
    public static final ValueChangeMode DEFAULT_VALUE_CHANGE_MODE = ValueChangeMode.ON_CHANGE;
    public static final String DEFAULT_HEIGHT = "500px";

    private static final DiffMatchPatch DIFF_MATCH_PATCH = new DiffMatchPatch();

    private final JsonObject initialConfig = Json.createObject();

    private Plugin[] activePlugins = {};
    private ResizeDirection resizeDirection = ResizeDirection.NONE;


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
     * Creates a new instance.
     */
    public HugeRte() {
        super("value", "", true);

        setValueChangeMode(DEFAULT_VALUE_CHANGE_MODE);
        setValueChangeTimeout(DEFAULT_VALUE_CHANGE_MODE_TIMEOUT);

        Element element = getElement();
        element.setPropertyJson("initialConfig", initialConfig);

        element.addEventListener("_blur", event -> fireEvent(new BlurEvent<>(this, true)));
        element.addEventListener("_focus", event -> fireEvent(new FocusEvent<>(this, true)));

        element.addEventListener("_value-delta", event -> {
            String delta = event.getEventData().getString("event.detail.delta");
            String oldValue = getValue();
            String newValue = applyDelta(oldValue, delta);
            setModelValue(newValue, true);
        }).addEventData("event.detail.delta");


        addAttachListener(event -> {
            // remove this call once the import is done via npm
            getUI().orElseThrow().getPage().addJavaScript(
                    "context://frontend/hugerte_addon/hugerte/hugerte.min.js");
        });

    }

    /**
     * Applies the given delta onto the "old" value. Returns the "new", resulting value
     *
     * @param oldValue old value
     * @param delta    delta to apply
     * @return new value
     */
    public static String applyDelta(String oldValue, String delta) {
        // convert string to patch object
        List<Patch> patches = DIFF_MATCH_PATCH.patchFromText(delta);

        // apply patch object
        Object[] results = DIFF_MATCH_PATCH.patchApply(
                patches instanceof LinkedList<Patch> alreadyLinkedList
                        ? alreadyLinkedList
                        : new LinkedList<>(patches),
                oldValue);

        // extract the resulting string
        return (String) results[0];
    }

    /// Sets the base configuration object as RAW JS. So be very careful what you pass in here. Any other configuration,
    /// set via the `configure()` methods will be applied on top of the given json object.
    ///
    /// @param jsConfig config to apply
    public void setRawConfig(String jsConfig) {
        checkAlreadyInitialized();
        getElement().setPropertyJson("rawInitialConfig", Json.parse(jsConfig));
    }

    /**
     * Sets a single string for the given configuration key. Please note, that there is no check, if the
     * given config key exists or the value is valid or might be applied to this key.
     * @param configurationKey config key
     * @param value value to set
     * @return this instance
     */
    public HugeRte configure(String configurationKey, String value) {
        checkAlreadyInitialized();
        initialConfig.put(configurationKey, value);
        return this;
    }

    /**
     * Sets an array of strings for the given configuration key. The result will also be an array on the client side.
     * Please note, that there is no check, if the given config key exists or the value is valid or might be
     * applied to this key.
     * @param configurationKey config key
     * @param value value to set
     * @return this instance
     */
    public HugeRte configure(String configurationKey, String... value) {
        checkAlreadyInitialized();
        JsonArray array = Json.createArray();
        for (int i = 0; i < value.length; i++) {
            array.set(i, value[i]);
        }
        initialConfig.put(configurationKey, array);
        return this;
    }

    /**
     * Sets a single boolean value for the given configuration key. Please note, that there is no check, if the
     * given config key exists or the value is valid or might be applied to this key.
     * @param configurationKey config key
     * @param value value to set
     * @return this instance
     */
    public HugeRte configure(String configurationKey, boolean value) {
        checkAlreadyInitialized();
        initialConfig.put(configurationKey, value);
        return this;
    }

    /**
     * Sets a single number for the given configuration key. Please note, that there is no check, if the
     * given config key exists or the value is valid or might be applied to this key.
     * @param configurationKey config key
     * @param value value to set
     * @return this instance
     */
    public HugeRte configure(String configurationKey, double value) {
        checkAlreadyInitialized();
        initialConfig.put(configurationKey, value);
        return this;
    }

    /**
     * Sets a single string for the given configuration key by reading the given client side reference's representation.
     * Please note, that there is no check, if the given config key exists or the value is valid or might be applied
     * to this key.
     * @param configurationKey config key
     * @param value value to set
     * @return this instance
     */
    public HugeRte configure(String configurationKey, ClientSideReference value) {
        return configure(configurationKey, value.getClientSideRepresentation());
    }

    /**
     * Sets the language the editor shall use. This dedicated method exists due to the special handling
     * of the {@link Language#ENGLISH} item.
     * @param language language to set
     * @return this instance
     */
    public HugeRte configureLanguage(Language language) {
        String code = language.getCode();
        if (code != null) { // English has no code, since it has no dedicated lang file.
            initialConfig.put("language", code);
        } else {
            initialConfig.remove("language");
        }

        return this;
    }

    /**
     * Configures the plugins, that shall be enabled for this editor instance. Previous settings will be overridden.
     * Passing nothing will disable all plugins.
     *
     * @param plugins plugins to enable
     * @return this instance
     */
    public HugeRte configurePlugins(Plugin... plugins) {
        checkAlreadyInitialized();
        checkForResizeConflicts();

        this.activePlugins = plugins;

        if (Set.of(plugins).contains(Plugin.AUTORESIZE)) {
            setHeight(null);
        }

        JsonArray jsonArray = Json.createArray();
        initialConfig.put("plugins", jsonArray);

        for (Plugin plugin : plugins) {
            jsonArray.set(jsonArray.length(), plugin.getClientSideRepresentation());
        }

        return this;
    }

    /**
     * Configures the menubar of the editor instance to show the given items. Previous settings will be overridden.
     * Passing nothing will hide the menubar.
     *
     * @param menubarItems menubar items to show
     * @return this instance
     */
    public HugeRte configureMenubar(Menubar... menubarItems) {
        checkAlreadyInitialized();

        configure("menubar", ClientSideReference.join(menubarItems));

        return this;
    }

    /**
     * Configures the toolbar of the editor instance to show the given items. Previous settings will be overridden.
     * Passing nothing will hide the toolbar.
     *
     * @param toolbarItems toolbar items to show
     * @return this instance
     */
    public HugeRte configureToolbar(Toolbar... toolbarItems) {
        checkAlreadyInitialized();
        initialConfig.put("toolbar", ClientSideReference.join(toolbarItems));
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
        if (isAttached()) {
            throw new AlreadyInitializedException();
        }
    }

    /**
     * Sets the value change mode of this instance. By default the editor uses {@link ValueChangeMode#ON_CHANGE}. Null
     * resets the mode to the default.
     *
     * @param valueChangeMode new value change mode
     */
    public void setValueChangeMode(ValueChangeMode valueChangeMode) {
        if (valueChangeMode == null) {
            setValueChangeMode(DEFAULT_VALUE_CHANGE_MODE);
        } else {
            getElement().setProperty("valueChangeMode", valueChangeMode.getClientSideRepresentation());
        }
    }

    /**
     * Returns the current value change mode. Never null.
     *
     * @return value change mode
     */
    public ValueChangeMode getValueChangeMode() {
        return ValueChangeMode.fromClientSide(getElement().getProperty("valueChangeMode", DEFAULT_VALUE_CHANGE_MODE.getClientSideRepresentation()));
    }

    /// Sets the timespan in milliseconds, that will be used by several value change modes.
    /// * TIMEOUT: the time, that is waited after the last change before the value is synced.
    /// * INTERVAL: the time between two value syncs. Also used as initial time before the first call.
    ///
    /// Default is 2000.
    ///
    /// Please note, that the client also throttles the amount of events, that might be fired to prevent the
    /// events from overhelming the server.
    ///
    /// @param timeoutInMillseconds milliseconds to be used by the value change modes
    public void setValueChangeTimeout(int timeoutInMillseconds) {
        if (timeoutInMillseconds <= 0) {
            throw new IllegalArgumentException("Timeout must be greater than 0");
        }

        getElement().setProperty("valueChangeTimeout", timeoutInMillseconds);
    }

    /// Sets the timespan in milliseconds, that will be used by several value change modes.
    /// * TIMEOUT: the time, that is waited after the last change before the value is synced.
    /// * INTERVAL: the time between two value syncs. Also used as initial time before the first call.
    ///
    /// Default is 2000.
    ///
    /// @return timespan in milliseconds
    public int getValueChangeTimeout() {
        return getElement().getProperty("valueChangeTimeout", DEFAULT_VALUE_CHANGE_MODE_TIMEOUT);
    }

}
