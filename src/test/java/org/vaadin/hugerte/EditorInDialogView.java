package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu(order = 2)
public class EditorInDialogView extends Div {

    public EditorInDialogView() {
        initTransferedInstance();
        initStandaloneDialog();

    }

    private void initTransferedInstance() {
        Button button = new Button("Open dialog and move editor there");
        HugeRte editor = new HugeRte();
        editor.configureBasicSetup();
        editor.setWidth("500px");
        editor.setHeight("300px");

        button.addClickListener(event -> {
            Dialog dialog = new Dialog();

            dialog.add(editor);
            dialog.open();

            dialog.addOpenedChangeListener(event1 -> {
                if (!event1.isOpened()) {
                    add(editor);
                }
            });
        });

        add(button, editor);

    }

    private void initStandaloneDialog() {
        Button button = new Button("Open standalone editor dialog");
        button.addClickListener(event -> {
            Dialog dialog = new Dialog();
            HugeRte editor = new HugeRte();
            editor.configureBasicSetup();
            editor.setWidth("500px");
            editor.setHeight("300px");

            dialog.add(editor);
            dialog.open();
        });

        add(button);
    }

}
