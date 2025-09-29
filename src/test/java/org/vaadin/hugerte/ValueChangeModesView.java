package org.vaadin.hugerte;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.vaadin.firitin.components.RichText;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu(order = 0)
public class ValueChangeModesView extends Div {

    protected HugeRte hugeRte;

    public ValueChangeModesView() {
        hugeRte = new HugeRte();

        hugeRte.setLabel("Hello Huge RTE");

        Span timePicker = new Span();
        RichText output = new RichText();

        Select<ValueChangeMode> modeSelect = new Select<>();
        modeSelect.setItems(ValueChangeMode.values());
        IntegerField timeoutField = new IntegerField();
        timeoutField.setMin(1);

        modeSelect.addValueChangeListener(e -> {
            ValueChangeMode value = e.getValue();
            hugeRte.setValueChangeMode(value);
            timeoutField.setVisible(value == ValueChangeMode.TIMEOUT || value == ValueChangeMode.INTERVAL);
        });

        timeoutField.addValueChangeListener(e -> {
            hugeRte.setValueChangeTimeout(e.getValue());
        });

        add(new HorizontalLayout(modeSelect, timeoutField), hugeRte, timePicker, output);

        hugeRte.addValueChangeListener(e -> {
            timePicker.setText(LocalTime.now().truncatedTo(ChronoUnit.MILLIS).toString());
            output.setRichText(e.getValue());
        });

        modeSelect.setValue(hugeRte.getValueChangeMode());
        timeoutField.setValue(hugeRte.getValueChangeTimeout());
        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");

    }

}
