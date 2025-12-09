package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

import java.time.LocalTime;

@Route
@Menu
public class DetachInvisible extends VerticalLayout {

    public DetachInvisible() {
        // https://github.com/parttio/tinymce-for-flow/issues/33
        HugeRte hugeRte = new HugeRte();
        add(hugeRte);
        add(new Button("Toggle visible", e-> hugeRte.setVisible(!hugeRte.isVisible())));
        add(new Button("Toggle attached", e-> {
            if(hugeRte.isAttached()) {
                hugeRte.removeFromParent();
            } else {
                addComponentAsFirst(hugeRte);
            }
        }));
        add(new Button("Set value", e-> hugeRte.setValue("Now: " + LocalTime.now())));
    }
}
