package org.vaadin.hugerte;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.vaadin.hugerte.sizes.*;

import java.util.HashMap;
import java.util.Map;

@Route
@Menu(order = 4)
public class SizesView extends VerticalLayout {

    private final Map<Tab, Component> tabsMap = new HashMap<>();
    private final Tabs tabs;
    private Component currentContent;

    public SizesView() {
        setSizeFull();
        setAlignItems(Alignment.STRETCH);

        tabs = new Tabs();
        add(tabs);

        tabs.setAutoselect(true);
        tabs.addSelectedChangeListener(event -> {
            if(currentContent != null) {
                currentContent.removeFromParent();
            }

            Component newContent = tabsMap.get(event.getSelectedTab());
            currentContent = newContent;
            addAndExpand(newContent);
        });

        register("Full height / Block", new FullHeightInBlockPart());
        register("Full height / Flexbox", new FullHeightInFlexboxPart());
        register("Fixed height / Block", new FixedHeightInBlockPart());
        register("Fixed height / Flexbox", new FixedHeightInFlexboxPart());
        register("SplitLayout", new FullHeightInSplitLayoutPart());
        register("Resizable", new MinMaxResizablePart());
        register("Autoresize Plugin", new MinMaxAutoResizePluginPart());

    }

    private void register(String label, Component component) {
        Tab tab = new Tab(label);

        // first register the component in the map, then add the tab - otherwise the selection listener will throw a NPE
        tabsMap.put(tab, component);
        tabs.add(tab);
    }
}
