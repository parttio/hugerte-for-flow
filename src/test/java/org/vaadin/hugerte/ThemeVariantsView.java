package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu(order = 6)
public class ThemeVariantsView extends VerticalLayout {

    public static final String FORM_WIDTH = "600px";

    public ThemeVariantsView() {
        createInstance("Default Theming");
        hr();
        createInstance("No Editor Focus Highlight", HugeRteVariant.NO_EDITOR_FOCUS_HIGHLIGHT);
        hr();
        createInstance("No Invalid Background", HugeRteVariant.NO_INVALID_BACKGROUND);
    }

    private void createInstance(String label, HugeRteVariant... variants) {
        Data sampleData = new Data();
        sampleData.setHtmlContent("<p>This is my <b>initial sample value</b></p>");

        HugeRte rte = new HugeRte();
        rte.setLabel(label);
        rte.addThemeVariants(variants);

        add(rte);

        Binder<Data> binder = new Binder<>();
        binder.forField(rte)
                .asRequired("Please set some awesome html content!")
                .bind(Data::getHtmlContent, Data::setHtmlContent);

        binder.readBean(sampleData);

        Button buttonSave = new Button("Save");
        buttonSave.addClickListener(e -> {
            if (binder.writeBeanIfValid(sampleData)) {
                Notification.show("Saved").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification.show("There are validation errors.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        });

        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button buttonReset = new Button("Reset");
        buttonReset.addClickListener(e -> {
            binder.readBean(sampleData);
            binder.validate();
            Notification.show("Reset to latest saved state");
        });
        buttonReset.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        Button buttonClear = new Button("Clear");
        buttonClear.addClickListener(e -> {
            binder.readBean(new Data());
            binder.validate();
            Notification.show("Cleared values");
        });
        buttonClear.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(buttonReset, buttonClear);
        buttons.addToEnd(buttonSave);
        add(buttons);

        buttons.getStyle().setWidth(FORM_WIDTH);
        rte.getStyle().setWidth(FORM_WIDTH).setHeight("250px");
    }

    private void hr() {
        Hr hr = new Hr();
        hr.setWidth(FORM_WIDTH);
        add(hr);
    }

    private static class Data {
        private String plainContent;
        private String htmlContent;

        public String getHtmlContent() {
            return htmlContent;
        }

        public void setHtmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
        }

        public String getPlainContent() {
            return plainContent;
        }

        public void setPlainContent(String plainContent) {
            this.plainContent = plainContent;
        }
    }
}
