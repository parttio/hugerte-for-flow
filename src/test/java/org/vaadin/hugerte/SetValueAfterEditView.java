package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Regression view for the "setValue() does not update the UI when setting a value back to a previously-set value
 * after the user has edited the field" issue.
 *
 * Set A, edit in the browser to B, then click "Reset to A" - the editor must show A again. "Clear" covers the same
 * case for the empty value: after an edit, clearing from the server must empty the editor too.
 */
@Route
public class SetValueAfterEditView extends VerticalLayout {

    public static final String INITIAL_VALUE = "<h2>Hello Friends</h2>";

    public SetValueAfterEditView() {
        HugeRte editor = new HugeRte();
        editor.setValue(INITIAL_VALUE);

        Button reset = new Button("Reset to A", e -> editor.setValue(INITIAL_VALUE));
        reset.setId("reset");

        Button clear = new Button("Clear", e -> editor.clear());
        clear.setId("clear");

        add(editor, reset, clear);
    }
}
