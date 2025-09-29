package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;

@Route
@Menu
public class UpdateValueOnDetachedEditor extends VerticalLayout {
    public UpdateValueOnDetachedEditor() {

        __HugeRte hugeRte = new __HugeRte();
        hugeRte.setValue("Jorma");

        Button b = new Button("Replace value while detached");
        b.addClickListener(e -> {
            hugeRte.removeFromParent();
            hugeRte.setValue("Now: " + LocalDateTime.now());
            add(hugeRte);
        });

        add(b, hugeRte);

    }
}
