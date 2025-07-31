package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style.Overflow;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu
public class MinMaxAutoResizeView extends VerticalLayout {

    public MinMaxAutoResizeView() {
        HugeRte rte = new HugeRte();

        rte.addClassName("auto-resize");
        rte.configurePlugins(Plugin.AUTORESIZE);
        rte.configure("content_style", """
                body {
                  overflow-y: auto !important; /* Force scrollbars on the body */
                  overflow-x: hidden !important; /* Prevent horizontal scroll if not needed */
                }""");

        rte.setSizeUndefined();
        rte.setMinHeight("200px");
        rte.setMaxHeight("600px");
        rte.getStyle().setOverflow(Overflow.HIDDEN);

        add(rte);
    }
}
