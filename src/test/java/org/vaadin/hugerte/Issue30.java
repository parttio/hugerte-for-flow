package org.vaadin.hugerte;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@Route
@PreserveOnRefresh
public class Issue30 extends VerticalLayout {

    public static class HugeRteField extends CustomField<String> {

        private static final long serialVersionUID = 1L;

        private HugeRte ivEditor = null;

        public HugeRteField() {
            super();
            setWidth("100%");
            setHeight("500px");
            ivEditor = new HugeRte();
            //TODO GEAR-2044 GearRichtextEditorField : updateValue() wird nicht aufgerufen
            //https://github.com/parttio/tinymce-for-flow/issues/30
            //ivEditor.addValueChangeListener(e -> this.updateValue());
            ivEditor.setWidthFull();
            ivEditor.setHeight("450px");
            add(ivEditor);
        }

        //from CustomField
        @Override
        protected String generateModelValue() {
            return getEditor().getValue();
        }

        @Override
        protected void setPresentationValue(String aNewPresentationValue) {
            getEditor().setValue(aNewPresentationValue);
        }

        public HugeRte getEditor() {
            return ivEditor;
        }

    }

    public static class AnotherCustomField extends CustomField<String> {

        private static final long serialVersionUID = 1L;

        private TextField ivEditor = null;

        public AnotherCustomField() {
            super();
            setWidth("100%");
            setHeight("40px");
            ivEditor = new TextField();
            add(ivEditor);
        }

        //from CustomField
        @Override
        protected String generateModelValue() {
            return getEditor().getValue();
        }

        @Override
        protected void setPresentationValue(String aNewPresentationValue) {
            getEditor().setValue(aNewPresentationValue);
        }

        public TextField getEditor() {
            return ivEditor;
        }

    }

    public Issue30() {
        Dialog dialog = new Dialog();
        HugeRteField rte = new HugeRteField();
        rte.setWidthFull();
        rte.getEditor().configure("branding", false);
        rte.getEditor().configure("statusbar", false);
        rte.setValue("<h2>Hallo Leute,</h2>");
        AnotherCustomField anotherCustomField = new AnotherCustomField();
        anotherCustomField.setValue("Jorma");

        Button close = new Button("Close", e -> {
            dialog.close();
            Notification.show(rte.getValue());
            Notification.show(anotherCustomField.getValue());
        });
        dialog.add(rte, anotherCustomField, close);
        Button open = new Button("Open", e -> {
            dialog.open();
        });
        add(open);
    }
}
