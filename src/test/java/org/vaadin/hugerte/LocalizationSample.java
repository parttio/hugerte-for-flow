package org.vaadin.hugerte;

import org.apache.commons.lang3.StringUtils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;

@Route
@Menu
public class LocalizationSample extends VerticalLayout {

    private final ComboBox<Language> languageComboBox;
    private HugeRte rte;

    public LocalizationSample() {
        languageComboBox = new ComboBox<>("Language");
        languageComboBox.setItems(Language.values());
        languageComboBox.setItemLabelGenerator(language -> {
            // Make the enum listing a bit more "attractive"
            String spacedName = String.join(" ", language.name().split("_"));
            return StringUtils.capitalize(spacedName.toLowerCase());
        });
        languageComboBox.setClearButtonVisible(true);
        languageComboBox.addValueChangeListener(event -> showInstanceForCurrentLanguage());

        add(languageComboBox);

        showInstanceForCurrentLanguage();

    }

    /**
     * Initializes a new instance of the editor. At the moment, the editor can only be configured before first
     * attachment. Until this changes, we have to reinstantiate the editor and apply the current value and new language.
     */
    private void showInstanceForCurrentLanguage() {
        String value = null;
        if (rte != null) {
            value = rte.getCurrentValue();
            remove(rte);
        }
        rte = new HugeRte();

        if(value != null) {
            rte.setValue(value);
        }

        languageComboBox.getOptionalValue().ifPresent(rte::configureLanguage);
        add(rte);
    }
}
