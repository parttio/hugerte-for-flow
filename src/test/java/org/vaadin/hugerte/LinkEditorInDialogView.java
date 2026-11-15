package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

/**
 * A moderately sized, toolbar only editor in a Vaadin Dialog for testing the link editor (the editor's own dialog)
 * inside a Vaadin Dialog, e.g. if it scrolls when the viewport is small.
 */
@Route
public class LinkEditorInDialogView extends Div {

    public LinkEditorInDialogView() {
        add(new Paragraph("Open the dialog, select some text and edit the link. Try also with a low browser window."));
        add(new Button("Open dialog", e -> new EditorDialog(false, false).open()) {{
                setId("open-dialog");
            }});
        add(new Button("Open dialog with compact editor (configureCompactSetup())", e -> new EditorDialog(true, false).open()) {{
                setId("open-compact-dialog");
            }});
        // Scroller (contain: paint) and FormLayout (contain: layout) confine fixed position descendants
        add(new Button("Open dialog with compact editor in a FormLayout in a Scroller", e -> new EditorDialog(true, true).open()) {{
                setId("open-scroller-dialog");
            }});
    }

    static class EditorDialog extends Dialog {

        EditorDialog(boolean compact, boolean inScroller) {
            setHeaderTitle("Edit item");
            TextField title = new TextField("Title") {{
                setWidthFull();
            }};
            if (inScroller) {
                add(new Scroller(new FormLayout(title, new LinkEditor(compact))));
            } else {
                add(title, new LinkEditor(compact));
            }
            getFooter().add(new Button("Close", e -> close()));
        }
    }

    static class LinkEditor extends HugeRte {

        LinkEditor(boolean compact) {
            setLabel("Description");
            setId("editor");
            setWidth("500px");
            if (compact) {
                configureCompactSetup();
            } else {
                configurePlugins(Plugin.LINK, Plugin.AUTOLINK);
                configureMenubar(false);
                configureToolbar(Toolbar.UNDO, Toolbar.REDO, Toolbar.SEPARATOR, Toolbar.BOLD, Toolbar.ITALIC,
                        Toolbar.SEPARATOR, Toolbar.LINK);
                setHeight("250px");
            }
            setValue("<p>Select this text and add a link, or edit <a href=\"https://vaadin.com\">this link</a>.</p>");
        }
    }
}
