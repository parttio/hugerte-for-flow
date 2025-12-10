package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.hugerte.HugeRte;
import org.vaadin.hugerte.Plugin;

public class MinMaxAutoResizePluginPart extends VerticalLayout {

    public MinMaxAutoResizePluginPart() {
        setSizeFull();
        setAlignItems(Alignment.STRETCH);
        setPadding(false);

        HugeRte rte = new HugeRte("Autoresize with a range of 200-600px");

        rte.configurePlugins(Plugin.AUTORESIZE);

        // don't do this
//        rte.setMinHeight(250, Unit.PIXELS);
//        rte.setMaxHeight(600, Unit.PIXELS);

        // but this
        rte.setEditorMinHeight(200);
        rte.setEditorMaxHeight(600);

        rte.setHelperText("This demo shows the autoresize feature in combination with min and max height. The editor will grow with its content. Please note that, for best visual results, you should apply height restrictions on the editor, not the component.");

        add(rte);
    }
}
