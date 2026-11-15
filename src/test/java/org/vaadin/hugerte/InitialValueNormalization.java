package org.vaadin.hugerte;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * HugeRTE normalises the initial value (e.g. whitespace). The normalised value is synced to the server, but
 * it must not be reported as a user change, otherwise e.g. forms become "dirty" right after opening.
 */
@Route
public class InitialValueNormalization extends VerticalLayout {

    public InitialValueNormalization() {
        Div userChanges = new Div();
        userChanges.setId("user-changes");
        Div serverValue = new Div();
        serverValue.setId("server-value");
        HugeRte editor = new HugeRte();
        editor.setValue("<p>K&ouml;yh&auml;   </p>");
        editor.addValueChangeListener(e -> {
            serverValue.setText(e.getValue());
            if (e.isFromClient()) {
                userChanges.add(new Div(e.getValue()));
            }
        });
        add(editor, serverValue, userChanges);
    }
}
