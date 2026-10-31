package org.vaadin.hugerte;

import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Toolbar actions and commands must fire value change events in timeout mode as well.
 * https://github.com/parttio/hugerte-for-flow/issues/35
 */
@Route
public class Issue35 extends VerticalLayout {

    private final Pre lastValue = new Pre();

    public Issue35() {
        lastValue.setId("last-value");
        add(new TimeoutModeEditor(), lastValue);
    }

    class TimeoutModeEditor extends HugeRte {
        TimeoutModeEditor() {
            setWidthFull();
            configureBasicSetup();
            setValueChangeMode(ValueChangeMode.TIMEOUT);
            setValueChangeTimeout(300);
            setValue("<p>Some text</p>");
            addValueChangeListener(e -> lastValue.setText(e.getValue()));
        }
    }
}
