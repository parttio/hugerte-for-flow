package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.firitin.components.RichText;

@Route("/this/is/long/route")
@Menu
public class DeepLinkingView extends Div {


    protected __HugeRte hugeRte;

    public DeepLinkingView() {
        hugeRte = new __HugeRte();

        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");
        hugeRte.setHeight("700px");
        
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

    }

}
