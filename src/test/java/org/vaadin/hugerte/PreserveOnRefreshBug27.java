package org.vaadin.hugerte;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.vaadin.firitin.components.button.VButton;

@Route
@PreserveOnRefresh
public class PreserveOnRefreshBug27 extends VerticalLayout {

    public PreserveOnRefreshBug27() {
        Dialog dialog = new Dialog();
        __HugeRte hugeRte = new __HugeRte();
        hugeRte.configure("branding", false);
        hugeRte.configure("statusbar", false);
        hugeRte.setValue("<h2>Hallo Leute,</h2>");
        dialog.add(hugeRte);
        dialog.add(new VButton("Cancel", e -> dialog.close()).withTabIndex(3));
        VButton button = new VButton("Focus (CTRL-I)", e -> hugeRte.focus());
        button.setTabIndex(2);
        button.addClickShortcut(Key.of("i"), KeyModifier.CONTROL);
        dialog.add(button);
        Button open = new Button("Open", e -> {dialog.open(); hugeRte.focus();});
        Button enable = new Button("Disable");
        enable.addClickListener(e -> {
            if ("Disable".equals(enable.getText())) {
                hugeRte.setEnabled(false);
                enable.setText("Enable");
            } else {
                hugeRte.setEnabled(true);
                enable.setText("Disable");
            }
        });
        Button readOnly = new Button("ReadOnly");
        readOnly.addClickListener(e -> {
            if ("ReadOnly".equals(readOnly.getText())) {
                hugeRte.setReadOnly(true);
                readOnly.setText("Writable");
            } else {
                hugeRte.setReadOnly(false);
                readOnly.setText("ReadOnly");
            }
        });
        add(open, enable, readOnly);
    }
}
