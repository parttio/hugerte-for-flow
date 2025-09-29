package org.vaadin.hugerte;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.hugerte.imageuploads.ActiveEditors;

@Route
@Menu
public class ImageUploadsEnabledView extends Div {

    protected __HugeRte hugeRte;

    @Autowired
    ActiveEditors activeEditors;

    public ImageUploadsEnabledView() {
        hugeRte = new __HugeRte();
        hugeRte.configure("plugins", "code", "link", "image");
        hugeRte.configure("toolbar", "toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | outdent indent' | link image");
        hugeRte.setValue("<p>Voi <strong>jorma</strong>!<p>");
        hugeRte.setHeight("700px");
        add(hugeRte);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Generate an id for this UI + use /upload rest
        // end point save uploaded/dragged images.
        // Id is only needed if you rest controller needs
        // to talk back to the UI somehow
        String id = activeEditors.register(hugeRte);
        hugeRte.configure("images_upload_url", "/upload/" + id);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        activeEditors.deRegister(hugeRte);
        super.onDetach(detachEvent);
    }
}
