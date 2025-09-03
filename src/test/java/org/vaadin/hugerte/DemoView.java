package org.vaadin.hugerte;

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
@Menu(order = 0)
public class DemoView extends Div {

    protected HugeRteWebComponent hugeRte;

    public DemoView() {
        hugeRte = new HugeRteWebComponent();
        hugeRte.setValueChangeMode(ValueChangeMode.ON_BLUR);

        hugeRte.setLabel("Hello Huge RTE");
        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");
        hugeRte.setHeight("700px");
        add(hugeRte);

        Button b = new Button("Set content dynamically", e -> {
            hugeRte.setValue("New value");
        });
        add(b);

        Button b2 = new Button("Show content", e -> {
            var n = new Dialog(new VerticalLayout(
                    new H5("New value:"),
                    new RichText(hugeRte.getValue())
            ));
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

        hugeRte.addValueChangeListener(e -> {
            Notification.show("ValueChange event!");
            System.out.println();
            System.out.println("### value change event###");
            System.out.println(e.getValue());
        });

    }

}
