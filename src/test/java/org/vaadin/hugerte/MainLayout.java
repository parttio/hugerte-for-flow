package org.vaadin.hugerte;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.shared.util.SharedUtil;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("HugeRTE for Flow - Demos");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        addToNavbar(toggle, title);

        SideNav nav = new SideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        MenuConfiguration.getMenuEntries()
                .stream()
                .map(entry -> new SideNavItem(SharedUtil.camelCaseToHumanFriendly(entry.title()), entry.path()))
                .forEach(nav::addItem);

        addToDrawer(scroller);
    }
}
