package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu
public class AutoSavePluginExample extends VerticalLayout {

    public AutoSavePluginExample() {
        HugeRte hugeRte = new HugeRte()
                .configureBasicSetup()
                .configurePlugins(Plugin.AUTOSAVE)
                // TODO add constant
                .configure("toolbar", "restoredraft")
                // save more eagerly for demo
                .configure("autosave_interval","5s")
                // Default by hugerte uses id of editor, which happens to be random
                // byfault for this integration
                .configure("autosave_prefix", "hugerte-autosave-myfield-id");

        add(hugeRte);
        setAlignItems(Alignment.STRETCH);
    }
}
