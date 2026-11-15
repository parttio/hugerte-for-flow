package org.vaadin.hugerte;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Application shortcuts must work also while typing in the editor, although its content lives in an iframe.
 */
@Route
public class ShortcutForwardingView extends VerticalLayout {

    private final HugeRte editor = new HugeRte() {{
        setId("editor");
        setHeight("250px");
    }};
    private final Div saved = new Div() {{
        setId("saved");
    }};
    private final Counter cancelCount = new Counter("cancel-count");
    private final Counter ctrlBCount = new Counter("ctrl-b-count");

    public ShortcutForwardingView() {
        add(new Paragraph("Type in the editor and press Ctrl+S to save, Escape to cancel. Ctrl+B is handled by the "
                + "editor (bold), so its application shortcut must not be triggered."));
        add(new Checkbox("Forward shortcuts", editor.isShortcutForwardingEnabled(),
                e -> editor.setShortcutForwardingEnabled(e.getValue())) {{
                setId("forwarding");
            }});
        add(editor);
        add(new Button("Save (Ctrl+S)", e -> saved.setText(editor.getValue())) {{
                addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
            }});
        add(new Button("Cancel (Escape)", e -> cancelCount.increment()) {{
                addClickShortcut(Key.ESCAPE);
            }});
        add(new Button("Application shortcut for Ctrl+B", e -> ctrlBCount.increment()) {{
                addClickShortcut(Key.KEY_B, KeyModifier.CONTROL);
            }});
        add(saved, cancelCount, ctrlBCount);
    }

    static class Counter extends Div {

        private int count;

        Counter(String id) {
            setId(id);
            setText("0");
        }

        void increment() {
            setText(String.valueOf(++count));
        }
    }
}
