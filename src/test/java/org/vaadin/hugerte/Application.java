package org.vaadin.hugerte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;

/**
 * Spring Boot app to run test UIs. The theme (Lumo/Aura) is selected per session in {@link MainLayout}.
 */
@SpringBootApplication
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addLink("stylesheet", MainLayout.Theme.current().getClientSideRepresentation());
    }

}