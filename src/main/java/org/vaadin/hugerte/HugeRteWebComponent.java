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
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.shared.HasValidationProperties;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.dom.Element;

@NpmPackage(value = "hugerte", version = "1.0.9")
@NpmPackage(value = "diff-match-patch", version = "1.0.5")
@Tag("vaadin-huge-rte")
@JsModule("./vaadin-huge-rte.js")
public class HugeRteWebComponent extends AbstractSinglePropertyField<HugeRteWebComponent, String> implements
        HasValidationProperties, HasValidator<String>, InputNotifier, /*TODO KeyNotifier,*/
        HasSize, HasStyle, Focusable<HugeRteWebComponent>, HasLabel {

    private static final DiffMatchPatch DIFF_MATCH_PATCH = new DiffMatchPatch();

    public HugeRteWebComponent() {
        super("value", "", true);

        setValueChangeMode(ValueChangeMode.CHANGE);

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

    public void setValueChangeMode(ValueChangeMode  valueChangeMode) {
        if (valueChangeMode == null) {
            setValueChangeMode(ValueChangeMode.CHANGE);
        } else {
            getElement().setProperty("valueChangeMode", valueChangeMode.toString().toLowerCase());
        }
    }

    public ValueChangeMode getValueChangeMode() {
        return ValueChangeMode.valueOf(getElement().getProperty("valueChangeMode",  ValueChangeMode.CHANGE.toString().toLowerCase()));
    }

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
