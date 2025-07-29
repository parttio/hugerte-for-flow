package org.vaadin.hugerte.imageuploads;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.vaadin.hugerte.HugeRte;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Example helper if your Rest controller accepting images
 * wants to talk back to the UI (in example we show a Vaadin Notification).
 * <p>
 *     In case you need to talk to specific component, the best
 *     practice is to associate both UI and the component for the
 *     id (calling getUI() is not thread safe due to a design flaw).
 * </p>
 */
@Component
@SessionScope
public class ActiveEditors {

    private Map<String,UI> idToUI = new HashMap<>();

    /**
     * Generates an id for the UI, that should be used in the REST
     * controller to identify the UI (and to send messages back)
     *
     * @param editor the component
     * @return the id
     */
    public String register(HugeRte editor) {
        String id = UUID.randomUUID().toString();
        idToUI.put(id, editor.getUI().get());
        return id;
    }

    private UI forId(String id) {
        return idToUI.get(id);
    }

    /**
     * Deregisters the editor UI to eagerly free memory.
     *
     * @param editor
     */
    public void deRegister(HugeRte editor) {
        UI ui = editor.getUI().get();
        var iterator = idToUI.entrySet().iterator();
        while(iterator.hasNext()) {
            if(iterator.next().getValue() == ui)
                iterator.remove();
        }
    }

    public void notifyOfSavedImage(String id, String originalFilename, boolean scaledDown) {
        forId(id).access(() -> {
            String scaledMsg = scaledDown ? "The images was quite big for web. But no worries, it was scaled down to 1280px wide." : "";
            Notification.show("File (%s) saved! %s".formatted(originalFilename, scaledMsg));
        });
    }
}
