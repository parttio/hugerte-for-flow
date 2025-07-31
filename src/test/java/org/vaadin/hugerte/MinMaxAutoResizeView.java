package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu
public class MinMaxAutoResizeView extends VerticalLayout {

    public MinMaxAutoResizeView() {
        HugeRte rte = new HugeRte("Autoresize with a range of 200-600px");

        rte.configurePlugins(Plugin.AUTORESIZE);
        rte.configure("max_height", 600);
        rte.setMinHeight("200px");

        add(rte);
    }
}
