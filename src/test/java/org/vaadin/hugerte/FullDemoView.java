package org.vaadin.hugerte;

import java.util.Arrays;
import java.util.function.Predicate;
import org.vaadin.firitin.components.RichText;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu(order = 1)
public class FullDemoView extends Div {

    // sample of a full toolbar
    public static final Toolbar[] FULL_TOOLBAR = {
            Toolbar.UNDO, Toolbar.REDO, Toolbar.SEPARATOR,
            Toolbar.BLOCKS, Toolbar.FONT_FAMILY, Toolbar.FONTSIZE, Toolbar.SEPARATOR,
            Toolbar.BOLD, Toolbar.ITALIC, Toolbar.UNDERLINE, Toolbar.STRIKETHROUGH, Toolbar.BLOCKQUOTE, Toolbar.SEPARATOR,
            Toolbar.ALIGN_LEFT, Toolbar.ALIGN_CENTER, Toolbar.ALIGN_RIGHT, Toolbar.ALIGN_JUSTIFY, Toolbar.SEPARATOR,
            Toolbar.NUMLIST, Toolbar.BULLIST, Toolbar.SEPARATOR,
            Toolbar.LINK, Toolbar.IMAGE, Toolbar.INSERT_DATETIME, Toolbar.SEPARATOR,
            Toolbar.TABLE, Toolbar.MEDIA, Toolbar.SEPARATOR,
            Toolbar.LINE_HEIGHT, Toolbar.OUTDENT, Toolbar.INDENT, Toolbar.SEPARATOR,
            Toolbar.FORECOLOR, Toolbar.BACKCOLOR, Toolbar.REMOVE_FORMAT, Toolbar.SEPARATOR,
            Toolbar.CHARMAP, Toolbar.EMOTICONS, Toolbar.SEPARATOR,
            Toolbar.CODE, Toolbar.FULLSCREEN, Toolbar.PREVIEW, Toolbar.SEPARATOR,
            Toolbar.SAVE, Toolbar.PRINT, Toolbar.SEPARATOR,
            Toolbar.PAGEBREAK, Toolbar.ANCHOR, Toolbar.CODE_SAMPLE, Toolbar.SEPARATOR,
            Toolbar.ACCORDION, Toolbar.ACCORDION_REMOVE, Toolbar.SEPARATOR,
            Toolbar.LTR, Toolbar.RTL, Toolbar.SEPARATOR,
            Toolbar.HELP
    };

    /**
     * Full set of plugins. We exclude the autoresize plugin, since that might be unexpected for a general demo due
     * to its maybe not so wide usage?
     */
    public static final Plugin[] ALL_PLUGINS_WITHOUT_AUTO_RESIZE = Arrays.stream(Plugin.values())
            .filter(Predicate.not(Plugin.AUTORESIZE::equals))
            .toArray(Plugin[]::new);

    protected HugeRte hugeRte;

    public FullDemoView() {
        hugeRte = new HugeRte();

        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");
        hugeRte.setHeight("700px");

        hugeRte.configurePlugin(false, ALL_PLUGINS_WITHOUT_AUTO_RESIZE);
        hugeRte.configureToolbar(false, FULL_TOOLBAR);

        add(hugeRte);

        Button b = new Button("Set content dynamically", e -> {
            hugeRte.setValue("New value");
        });
        add(b);

        Button b2 = new Button("Show content", e -> {

            var n = new Notification("", 3000);
            n.add(new VerticalLayout(
                    new H5("New value:"),
                    new RichText(hugeRte.getCurrentValue())
                    )
            );
            n.open();
        });
        add(b2);
        
        Button focus = new Button("focus", e->{
            hugeRte.focus();
        });
        add(focus);
        hugeRte.addFocusListener(e->{
            Notification.show("Focus event!");
        });
        hugeRte.addBlurListener(e->{
            Notification.show("Blur event!");
        });

        Button disable = new Button("Disabble", e-> {
            hugeRte.setEnabled(!hugeRte.isEnabled());
            e.getSource().setText(hugeRte.isEnabled() ? "Disable" : "Enable");
        });
        add(disable);

        Button blur = new Button("blur (NOT SUPPORTED really, but of course works from button)", e-> {
            hugeRte.blur();
        });
        blur.addClickShortcut(Key.KEY_B, KeyModifier.CONTROL);
        add(blur);

        Button bDialog = new Button("Open in Dialog");
        bDialog.addClickListener(e -> {
            Dialog dialog = new Dialog(hugeRte);
            dialog.open();

            dialog.addOpenedChangeListener(event -> {
                if (!event.isOpened()) {
                    addComponentAsFirst(hugeRte);
                }
            });
        });

        add(bDialog);


        hugeRte.addValueChangeListener(e -> {
            Notification.show("ValueChange event!");
            System.out.println(e.getValue());
        });

    }

}
