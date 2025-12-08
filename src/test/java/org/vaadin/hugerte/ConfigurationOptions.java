package org.vaadin.hugerte;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import tools.jackson.databind.ObjectMapper;

@Route
@Menu(order = 3)
public class ConfigurationOptions extends Div {

    public ConfigurationOptions() {
        add(new H1("Couple of examples with various init configurations"));
        addWithConfig()
                .configure("branding", false)
                .configure("plugins", "link");
        addWithConfig()
                .configure("inline", true)
                        .configure("plugins", "link").setHeight(null);;

        addWithConfig()
                .configure("plugins", "quickbars", "link")
                .configure("toolbar", false)
                .configure("menubar", false)
                .configure("inline", true);
        addWithConfig()
                .configure("sking", "oxide-dark")
                .configure("content_css", "dark");

    }

    private HugeRte addWithConfig() {
        HugeRte hugeRte = new HugeRte();
        hugeRte.setValue("Initial <em>content</em>");
        final Pre pre = new Pre();
        add(pre);
        add(hugeRte);
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> {
                    pre.setText(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(hugeRte.config));
                }));
        return hugeRte;
    }

}
