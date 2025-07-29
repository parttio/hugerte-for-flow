package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
public class HugeRTEWithVaadinSelect extends VerticalLayout {
    
    public enum Options {
        FOO,BAR
    }

    public HugeRTEWithVaadinSelect() {
        
        HugeRte rte = new HugeRte();
        rte.setValue("Edit me");
        
        Select<Options> select = new Select<>();
        select.setItems(Options.values());
        select.setValue(Options.FOO);
        
        Button b = new Button("Set value and toggle visibility");
        b.addClickListener(e -> {
            select.setValue(Options.BAR);
            if(rte.isAttached()) {
                remove(select);
                remove(rte);
            } else {
                add(rte);
                add(select);
            }
            
        });
        add(b);
    }
}
