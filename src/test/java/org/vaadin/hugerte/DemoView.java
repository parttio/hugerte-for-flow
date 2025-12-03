package org.vaadin.hugerte;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu(order = 0)
public class DemoView extends VerticalLayout {

    protected HugeRte hugeRte;

    public DemoView() {
        setAlignItems(Alignment.STRETCH);

        hugeRte = new HugeRte();
        hugeRte.setValueChangeMode(ValueChangeMode.ON_BLUR);
        hugeRte.configureResize(ResizeDirection.BOTH);

        hugeRte.setLabel("Hello Huge RTE");
        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");
//        hugeRte.setHeight("700px");
        add(new TextField("Sample Textfield"), new TextArea("Sample Textarea"), hugeRte);

        Button b = new Button("Set content dynamically", e -> {
            hugeRte.setValue("New value");
        });

        Button b2 = new Button("Show content", e -> {
            var n = new Dialog(new VerticalLayout(
                    new H5("New value:"),
                    new Html("<div>" + hugeRte.getValue() + "</div>")
            ));
            n.open();
        });

        Button focus = new Button("focus", e->{
            hugeRte.focus();
        });
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

        Button blur = new Button("blur (NOT SUPPORTED really, but of course works from button)", e-> {
            hugeRte.blur();
        });
        blur.addClickShortcut(Key.KEY_B, KeyModifier.CONTROL);
        add(new HorizontalLayout(b, b2, focus, disable, blur));

        hugeRte.addValueChangeListener(e -> {
            Notification.show("ValueChange event!");
        });

    }

}
