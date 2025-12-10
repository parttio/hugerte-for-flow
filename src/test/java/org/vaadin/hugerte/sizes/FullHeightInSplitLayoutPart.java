package org.vaadin.hugerte.sizes;

import com.vaadin.flow.component.splitlayout.SplitLayout;
import org.vaadin.hugerte.HugeRte;

public class FullHeightInSplitLayoutPart extends SplitLayout {

    public FullHeightInSplitLayoutPart() {
        setSizeFull();
        setOrientation(Orientation.VERTICAL);

        SplitLayout left = new SplitLayout();
        SplitLayout right = new SplitLayout();

        HugeRte rte1 = new HugeRte("Left 1 instance");
        HugeRte rte2 = new HugeRte("Left 2 instance");
        HugeRte rte3 = new HugeRte("Right 1 instance");
        HugeRte rte4 = new HugeRte("Right 2 instance");

        rte1.setSizeFull();
        rte2.setSizeFull();
        rte3.setSizeFull();
        rte4.setSizeFull();

        left.addToPrimary(rte1);
        left.addToSecondary(rte2);
        right.addToPrimary(rte3);
        right.addToSecondary(rte4);

        addToPrimary(left);
        addToSecondary(right);
    }
}
