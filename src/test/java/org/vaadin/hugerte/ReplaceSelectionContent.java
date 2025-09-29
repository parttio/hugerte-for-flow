package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;

@Route
@Menu
public class ReplaceSelectionContent extends VerticalLayout {

    public ReplaceSelectionContent() {
        setAlignItems(Alignment.STRETCH);
    	
    	HugeRte editor = new HugeRte();
        editor.setValue("Hello World, this is a test text. Simply select a part of this text and click the \"Insert time\" button to see the replace selection in action." +
                        "<br><br>" +
                        "Please note, that the value change mode of the editor is not taken into account when replacing text. Instead it will fire a value" +
                        " change immediately.");
        editor.setValueChangeMode(ValueChangeMode.ON_CHANGE);
    	
    	Button insertCurrentTime = new Button("Insert time", event -> {
            String now = LocalDateTime.now().toString();
            editor.replaceSelectionContent(now);
        });

		editor.addValueChangeListener(event ->
                Notification.show("ValueChange event: " + event.getValue()).setPosition(Position.BOTTOM_END));

        Button showValue = new Button("Show value", event ->
                Notification.show(editor.getValue()));

        add(editor, new HorizontalLayout(insertCurrentTime, showValue));

    }
}
