package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Table edits after restoring the content into a new editor instance must reach the server,
 * https://github.com/parttio/hugerte-for-flow/issues/42
 */
@Route
public class Issue42 extends VerticalLayout {

    private final Div editorHolder = new Div();
    private final Pre serverValue = new Pre();
    private String savedValue = "";
    private HugeRte editor;

    public Issue42() {
        editorHolder.setWidthFull();
        serverValue.setId("server-value");
        add(new Button("Save", e -> savedValue = editor.getValue()) {{
                setId("save");
            }},
            new Button("New editor with saved content", e -> createEditor(savedValue)) {{
                setId("recreate");
            }},
            editorHolder, serverValue);
        createEditor("");
    }

    private void createEditor(String value) {
        editorHolder.removeAll();
        editor = new HugeRte();
        editor.setId("editor");
        editor.configurePlugins(Plugin.TABLE);
        editor.configureToolbar(Toolbar.TABLE);
        editor.setHeight("400px");
        editor.setValue(value);
        editor.addValueChangeListener(e -> serverValue.setText(e.getValue()));
        serverValue.setText(value);
        editorHolder.add(editor);
    }
}
