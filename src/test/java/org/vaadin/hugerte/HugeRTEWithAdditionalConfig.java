package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class HugeRTEWithAdditionalConfig extends VerticalLayout {

    public HugeRTEWithAdditionalConfig() {
        HugeRte hugeRte = new HugeRte()
                .configureBasicSetup()
                .configurePlugins(Plugin.TABLE)
                .configureMenubar(Menubar.TABLE)
                .configureToolbar(Toolbar.TABLE);
        add(hugeRte);
    }
}
