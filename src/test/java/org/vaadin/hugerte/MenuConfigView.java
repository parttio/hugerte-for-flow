package org.vaadin.hugerte;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

@Route
public class MenuConfigView extends Div {

    protected HugeRte hugeRte;

    public MenuConfigView() {
        hugeRte = new HugeRte();

        hugeRte.configureMenubar(Menubar.FILE, Menubar.EDIT, Menubar.VIEW);
        hugeRte.configurePlugins(Plugin.TABLE);
        hugeRte.configureToolbar(Toolbar.BOLD, Toolbar.TABLE);
        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");
        hugeRte.setHeight("700px");
        
        add(hugeRte);

        hugeRte.addValueChangeListener(e -> {
            Notification.show("ValueChange event!");
            System.out.println(e.getValue());
        });

    }

}
