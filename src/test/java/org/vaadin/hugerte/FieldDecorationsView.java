package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

/**
 * Field decorations (label, required indicator, helper and error message) of HugeRte next to the core fields, to
 * compare them visually in both Lumo and Aura.
 */
@Route
@Menu(order = 7)
public class FieldDecorationsView extends VerticalLayout {

    public FieldDecorationsView() {
        add(new Row(false), new Row(true));
    }

    static class Row extends HorizontalLayout {
        Row(boolean invalid) {
            setWidthFull();
            add(new DecoratedEditor(invalid), new DecoratedTextArea(invalid), new DecoratedTextField(invalid));
        }
    }

    static class DecoratedEditor extends HugeRte {
        DecoratedEditor(boolean invalid) {
            super("HugeRte label");
            setWidth("40%");
            setHelperText("Helper text");
            setRequiredIndicatorVisible(true);
            setErrorMessage("Error message");
            setInvalid(invalid);
            configure("menubar", false);
            setEditorHeight("150px");
        }
    }

    static class DecoratedTextArea extends TextArea {
        DecoratedTextArea(boolean invalid) {
            super("TextArea label");
            setWidth("30%");
            setHelperText("Helper text");
            setRequiredIndicatorVisible(true);
            setErrorMessage("Error message");
            setInvalid(invalid);
        }
    }

    static class DecoratedTextField extends TextField {
        DecoratedTextField(boolean invalid) {
            super("TextField label");
            setWidth("30%");
            setHelperText("Helper text");
            setRequiredIndicatorVisible(true);
            setErrorMessage("Error message");
            setInvalid(invalid);
        }
    }
}
