package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.hugerte.HugeRte;

public class FullHeightInFlexboxPart extends VerticalLayout {

    public FullHeightInFlexboxPart() {
        setSizeFull();
        setAlignItems(Alignment.STRETCH);
        setPadding(false);

        HugeRte rte = new HugeRte("Full height in a flexbox container");
        rte.setHelperText("This demo shows the RTE inside a flexbox container with a flex grow of 1. There are no min-max restrictions.");

        add(rte);
        expand(rte);
    }
}
