package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class DetachInvisible extends VerticalLayout {

    public DetachInvisible() {
        // https://github.com/parttio/tinymce-for-flow/issues/33
        HugeRte hugeRte = new HugeRte();
        add(hugeRte);
        add(new Button("Toggle visible", e-> hugeRte.setVisible(!hugeRte.isVisible())));
        add(new Button("Detach", e-> hugeRte.removeFromParent()));
    }
}
