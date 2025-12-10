package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.html.Div;
import org.vaadin.hugerte.HugeRte;

public class FixedHeightInBlockPart extends Div {

    public FixedHeightInBlockPart() {
        setSizeFull();
        HugeRte rte = new HugeRte("Fixed height inside a block element (div)");
        rte.setHeight("800px");

        rte.setHelperText("This demo shows the RTE inside a normal block container with a height of 800px.");

        add(rte);
    }
}
