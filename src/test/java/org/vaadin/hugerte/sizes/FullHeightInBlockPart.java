package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.html.Div;
import org.vaadin.hugerte.HugeRte;

public class FullHeightInBlockPart extends Div {

    public FullHeightInBlockPart() {
        setSizeFull();
        HugeRte rte = new HugeRte("Full height inside a block element (div)");
        rte.setHeightFull();

        rte.setHelperText("This demo shows the RTE inside a normal block container with a height of 100%. There are no min-max restrictions.");

        add(rte);
    }
}
