package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.hugerte.HugeRte;
import org.vaadin.hugerte.ResizeDirection;

public class MinMaxResizablePart extends VerticalLayout {

    public MinMaxResizablePart() {
        setSizeFull();
        setAlignItems(Alignment.STRETCH);
        setPadding(false);

        HugeRte rte = new HugeRte("Resizable with a range of 200-600px");
        rte.configureResize(ResizeDirection.BOTH);

        // do not use the "normal" height stylings here
//        rte.setHeight("500px");
//        rte.setMinHeight("200px");
//        rte.setMaxHeight("600px");

        // but the editor's ones only
        rte.setEditorHeight(500);
        rte.setEditorMinHeight(200);
        rte.setEditorMaxHeight(600);

        rte.setHelperText("This demo shows the \"resizable\" feature. Please note, that any height settings must be applied on the editor, not the component.");

        add(rte);
    }
}
