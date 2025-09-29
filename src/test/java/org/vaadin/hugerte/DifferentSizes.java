/*
 * Copyright 2020 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.hugerte;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

/**
 *
 * @author mstahv
 */
@Route
@Menu(order = 4)
public class DifferentSizes extends VerticalLayout {

    private TextField questionKey = new TextField("Question Key");

    private __HugeRte tmcdeditor1 = new __HugeRte();
    private __HugeRte tmcdeditor2 = new __HugeRte();
    private __HugeRte tmcdeditor3 = new __HugeRte();
    private __HugeRte tmcdeditor4 = new __HugeRte();
    private __HugeRte tmcdeditor5 = new __HugeRte();
    private __HugeRte tmcdeditor6 = new __HugeRte();
    private TextField firstName = new TextField("First Name:");
    private TextField lastName = new TextField("Last Name:");
    private TextArea address = new TextArea("Address");

    private RadioButtonGroup<String> trigger = new RadioButtonGroup<>();

    public DifferentSizes() {
        String[] YesNoChoices = {"Yes", "No"};

        tmcdeditor1.setWidth("500px");
        tmcdeditor1.setHeight("200px");
        tmcdeditor1.setVisible(false);

        tmcdeditor2.setVisible(false);

        tmcdeditor3.setHeight("1000px");
        tmcdeditor3.setWidth("100%");
        tmcdeditor3.setVisible(false);

        tmcdeditor4.setWidth("50%");
        tmcdeditor4.setHeight("500px");
        tmcdeditor4.setVisible(false);

        tmcdeditor5.setWidth("800px");
        tmcdeditor5.setHeight("500px");
        tmcdeditor5.setVisible(false);

        tmcdeditor6.setWidth("800px");
        tmcdeditor6.setHeight("500px");
        tmcdeditor6.setVisible(false);

        //String tmcoptions = "{\"branding\": false,\"menubar\": false,\"contextmenu\": false,\"toolbar\": false,\"statusbar\": false}";
        String tmcoptions = "{\"resize\": false,\"statusbar\": false, \"menubar\": \"tools help\", \"branding\": false, \"plugins\" : \"link image code\",    \"toolbar\" : \"undo redo | styleselect | forecolor | bold italic underline | alignleft aligncenter alignright alignjustify | outdent indent | link image | code\" }";
        tmcdeditor1.setConfig(tmcoptions);
        tmcdeditor2.setConfig(tmcoptions);
        tmcdeditor3.setConfig(tmcoptions);
        tmcdeditor4.setConfig(tmcoptions);
        tmcdeditor5.setConfig(tmcoptions);
        tmcdeditor6.setConfig(tmcoptions);

        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        group.setLabel("Have you anything to report like  lot of text?");
        group.setItems("Yes", "No");

        RadioButtonGroup<String> group1 = new RadioButtonGroup<>();
        group1.setLabel("Have you anything to report about this?");
        group1.setItems("Yes", "No");

        RadioButtonGroup<String> group2 = new RadioButtonGroup<>();
        group2.setLabel("Have you anything to report about that ?");
        group2.setItems("Yes", "No");

        RadioButtonGroup<String> group3 = new RadioButtonGroup<>();
        group3.setLabel("Have you anything to report about that ?");
        group3.setItems("Yes", "No");

        RadioButtonGroup<String> group4 = new RadioButtonGroup<>();
        group4.setLabel("Have you anything to report about that ?");
        group4.setItems("Yes", "No");

        RadioButtonGroup<String> group5 = new RadioButtonGroup<>();
        group5.setLabel("Have you anything to report about that ?");
        group5.setItems("Yes", "No");

        questionKey.focus();

        group.addValueChangeListener(event -> {
            if (event.getValue().equalsIgnoreCase("Yes")) {
                tmcdeditor1.setVisible(true);
            } else {
                tmcdeditor1.setVisible(false);
            }
        }
        );

        group1.addValueChangeListener(event -> {
            if (event.getValue().equalsIgnoreCase("Yes")) {

                tmcdeditor2.setVisible(true);

            } else {
                tmcdeditor2.setVisible(false);
            }
        }
        );

        group2.addValueChangeListener(event -> {
            if (event.getValue().equalsIgnoreCase("Yes")) {
                tmcdeditor3.setVisible(true);
            } else {
                tmcdeditor3.setVisible(false);
            }
        }
        );

        group3.addValueChangeListener(event -> {
            if (event.getValue().equalsIgnoreCase("Yes")) {
                tmcdeditor4.setVisible(true);
            } else {
                tmcdeditor4.setVisible(false);
            }
        }
        );

        group4.addValueChangeListener(event -> {
            if (event.getValue().equalsIgnoreCase("Yes")) {
                tmcdeditor5.setVisible(true);
            } else {
                tmcdeditor5.setVisible(false);
            }
        }
        );

        group5.addValueChangeListener(event -> {
            if (event.getValue().equalsIgnoreCase("Yes")) {
                tmcdeditor6.setVisible(true);
            } else {
                tmcdeditor6.setVisible(false);
            }
        }
        );

        VerticalLayout formLayout = new VerticalLayout(questionKey, firstName, lastName, address, group, tmcdeditor1, group1, tmcdeditor2, group2, tmcdeditor3, group3, tmcdeditor4, group4, tmcdeditor5, group5, tmcdeditor6);
        firstName.setWidth("50%");
        lastName.setWidthFull();
        add(formLayout);

    }

}
