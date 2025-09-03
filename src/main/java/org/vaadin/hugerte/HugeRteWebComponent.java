package org.vaadin.hugerte;

import java.util.LinkedList;
import java.util.List;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch.Patch;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.InputNotifier;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.dom.Element;

/**
 * A Rich Text editor, based on HugeRTE JS component.
 * <p>
 * @author mstahv, Stefan Uebe
 */
@NpmPackage(value = "hugerte", version = "1.0.9")
@NpmPackage(value = "diff-match-patch", version = "1.0.5")
@Tag("vaadin-huge-rte")
@JsModule("./vaadin-huge-rte.js")
public class HugeRteWebComponent extends AbstractSinglePropertyField<HugeRteWebComponent, String> implements
        HasValidationProperties, HasValidator<String>, InputNotifier, /*TODO KeyNotifier,*/
        HasSize, HasStyle, Focusable<HugeRteWebComponent>, HasLabel {

    // TODO finalize value change mode on client side
    // TODO configuration general
    // TODO config plugins
    // TODO config toolbar
    // TODO config menubar
    // TODO config resize
    // TODO enable / readonly
    // TODO check client side imports,
    // TODO stylings / lumo integration
    // TODO theme variants
    // TODO replace selection
    // TODO static "createInstanceWithDefaultConfig()"
    // TODO close toolbar
    // TODO KeyNotifier
    // TODO Demos


    private static final DiffMatchPatch DIFF_MATCH_PATCH = new DiffMatchPatch();

    /**
     * Creates a new instance.
     */
    public HugeRteWebComponent() {
        super("value", "", true);

        setValueChangeMode(ValueChangeMode.ON_CHANGE);

        Element element = getElement();
        element.addEventListener("_blur", event -> fireEvent(new BlurEvent<>(this, true)));
        element.addEventListener("_focus", event -> fireEvent(new FocusEvent<>(this, true)));

        element.addEventListener("_value-delta", event -> {
            String delta = event.getEventData().getString("event.detail.delta");
            String oldValue = getValue();
            String newValue = applyDelta(oldValue, delta);
            setModelValue(newValue, true);
        }).addEventData("event.detail.delta");
    }

    /**
     * Creates a new instance with the given label.
     * @param label label
     */
    public HugeRteWebComponent(String label) {
        this();
        setLabel(label);
    }

    /**
     * Creates a new instance with the given label and initial value. The initial value is set as it is without
     * any further processing.
     * @param label label
     * @param initialValue initial value
     */
    public HugeRteWebComponent(String label, String initialValue) {
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
    public HugeRteWebComponent(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<HugeRteWebComponent, String>> valueChangeListener) {
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
    public HugeRteWebComponent(String label, ValueChangeListener<? super ComponentValueChangeEvent<HugeRteWebComponent, String>> valueChangeListener) {
        this();
        setLabel(label);
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Creates a new instance with the given value change listener.
     * @param valueChangeListener value change listener
     */
    public HugeRteWebComponent(ValueChangeListener<? super ComponentValueChangeEvent<HugeRteWebComponent, String>> valueChangeListener) {
        this();
        addValueChangeListener(valueChangeListener);
    }

    /**
     * Sets the value change mode of this instance. By default the editor uses {@link ValueChangeMode#ON_CHANGE}. Null
     * resets the mode to the default.
     * @param valueChangeMode new value change mode
     */
    public void setValueChangeMode(ValueChangeMode valueChangeMode) {
        if (valueChangeMode == null) {
            setValueChangeMode(ValueChangeMode.ON_CHANGE);
        } else {
            getElement().setProperty("valueChangeMode", valueChangeMode.getClientSide());
        }
    }

    /**
     * Returns the current value change mode. Never null.
     * @return value change mode
     */
    public ValueChangeMode getValueChangeMode() {
        return ValueChangeMode.fromClientSide(getElement().getProperty("valueChangeMode",  ValueChangeMode.ON_CHANGE.getClientSide()));
    }

    /**
     * Applies the given delta onto the "old" value. Returns the "new", resulting value
     * @param oldValue old value
     * @param delta delta to apply
     * @return new value
     */
    private String applyDelta(String oldValue, String delta) {
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
}
