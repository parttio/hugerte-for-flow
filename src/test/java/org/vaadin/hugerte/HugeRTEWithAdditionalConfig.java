package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
public class HugeRTEWithAdditionalConfig extends VerticalLayout {

    public HugeRTEWithAdditionalConfig() {
        HugeRte hugeRte = new HugeRte();
        hugeRte.configurePlugin(true, Plugin.TABLE)
                .configureMenubar(true, Menubar.TABLE)
                .configureToolbar(true, Toolbar.TABLE);
        add(hugeRte);
    }
}
