package org.vaadin.hugerte;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Layout
public class MainLayout extends AppLayout {

    private Registration currentStyleSheetRegistration;

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("HugeRTE for Flow - Demos");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Select<Theme> themeSelect = new Select<>("", Theme.values());
        themeSelect.addValueChangeListener(e -> {
            if(currentStyleSheetRegistration != null) {
                currentStyleSheetRegistration.remove();
                currentStyleSheetRegistration = null;
            }

            Theme theme = e.getValue();

            if (theme.getClientSideRepresentation() != null) {
                currentStyleSheetRegistration = UI.getCurrent().getPage().addStyleSheet(theme.getClientSideRepresentation());
            }
        });
        themeSelect.setValue(Theme.LUMO);
        themeSelect.getStyle().setMarginLeft("auto");

        Button darkMode = new Button("Dark Mode", e -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            boolean lightModeActive = !themeList.contains("dark");
            themeList.set("dark", lightModeActive);
            e.getSource().setText(lightModeActive ? "Dark Mode" : "Light Mode");
        });


        addToNavbar(toggle, title, themeSelect, darkMode);

        SideNav nav = new SideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        MenuConfiguration.getMenuEntries()
                .stream()
                .map(entry -> new SideNavItem(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(entry.title())), entry.path()))
                .forEach(nav::addItem);

        addToDrawer(scroller);
    }

    private enum Theme implements ClientSideReference {
        NONE(null),
        LUMO("@vaadin/vaadin-lumo-styles/lumo.css"),
        AURA("@vaadin/aura/aura.css");

        private final String value;

        Theme(String value) {
            this.value = value;
        }

        @Override
        public String getClientSideRepresentation() {
            return value;
        }
    }
}
