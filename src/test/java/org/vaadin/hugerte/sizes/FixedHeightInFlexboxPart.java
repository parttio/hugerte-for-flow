package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.hugerte.HugeRte;

public class FixedHeightInFlexboxPart extends VerticalLayout {

    public FixedHeightInFlexboxPart() {
        setSizeFull();
        setAlignItems(Alignment.STRETCH);
        setPadding(false);

        HugeRte rte = new HugeRte("Fixed height in a flexbox container");
        rte.setHelperText("This demo shows the RTE inside a flexbox container with a flex basis of 800px.");

        add(rte);
        rte.setHeight(null); // not necessary, but to prevent misinterpretation
        rte.getStyle().setFlexBasis("800px");
    }
}
