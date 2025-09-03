package org.vaadin.hugerte;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;

@Tag("vaadin-huge-rte")
@NpmPackage(value = "hugerte", version = "1.0.9")
@JsModule("./vaadin-huge-rte.js")
public class HugeRteWebComponent extends AbstractSinglePropertyField<HugeRteWebComponent, String> implements
        HasValidationProperties, HasValidator<String>, HasValueChangeMode/*, InputNotifier, KeyNotifier*/,
        HasSize, HasStyle, Focusable<HugeRteWebComponent>, HasLabel {

    private int valueChangeTimeout;
    private ValueChangeMode currentMode;

    public HugeRteWebComponent() {
        super("value", "", true);
        setValueChangeMode(ValueChangeMode.ON_CHANGE);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default value is {@link ValueChangeMode#ON_CHANGE}.
     */
    @Override
    public ValueChangeMode getValueChangeMode() {
        return currentMode;
    }

    @Override
    public void setValueChangeMode(ValueChangeMode valueChangeMode) {
        currentMode = valueChangeMode;
        setSynchronizedEvent(ValueChangeMode.eventForMode(valueChangeMode, "input"));
        applyChangeTimeout();
    }

    @Override
    public void setValueChangeTimeout(int valueChangeTimeout) {
        this.valueChangeTimeout = valueChangeTimeout;
        applyChangeTimeout();
    }

    @Override
    public int getValueChangeTimeout() {
        return valueChangeTimeout;
    }

    void applyChangeTimeout() {
        ValueChangeMode.applyChangeTimeout(getValueChangeMode(),
                getValueChangeTimeout(), getSynchronizationRegistration());
    }
}
