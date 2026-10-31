package org.vaadin.hugerte;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.theme.lumo.Lumo;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("HugeRTE for Flow - Demos");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Select<Theme> themeSelect = initThemeSelector();
        themeSelect.getStyle().setMarginLeft("auto");

        Button darkMode = new Button("Dark Mode", e -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            boolean lightModeActive = !themeList.contains("dark");
            themeList.set("dark", lightModeActive);
            e.getSource().setText(lightModeActive ? "Light Mode" : "Dark Mode");
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

    private Select<Theme> initThemeSelector() {
        Select<Theme> themeSelect = new Select<>("", Theme.values());
        themeSelect.setValue(Theme.current());
        themeSelect.addValueChangeListener(e -> {
            VaadinSession.getCurrent().setAttribute(Theme.class, e.getValue());
            UI.getCurrent().getPage().reload();
        });
        return themeSelect;
    }

    /**
     * The theme is stored in the session and only its style sheet is loaded, by {@link Application}. Changing it
     * reloads the page, as removing a theme style sheet at runtime doesn't fully restore the components styled by it.
     */
    enum Theme implements ClientSideReference {
        LUMO(Lumo.STYLESHEET),
        AURA(Aura.STYLESHEET);

        private final String value;

        Theme(String value) {
            this.value = value;
        }

        static Theme current() {
            return Optional.ofNullable(VaadinSession.getCurrent().getAttribute(Theme.class)).orElse(LUMO);
        }

        @Override
        public String getClientSideRepresentation() {
            return value;
        }
    }
}
