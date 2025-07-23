package org.vaadin.hugerte;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.hugerte.imageuploads.ActiveEditors;

@Route
@Menu
public class ImagePickerCallback extends Div {

    protected HugeRte hugeRte;

    public static class FileCallbackTinyMce extends HugeRte {
        public FileCallbackTinyMce() {
            // This is RAW JS!! Other options are evaluated as JSON on top of this
            setConfig("""
                {
                    file_picker_callback: (callback, value, meta) => {
                        // save callback for later use
                        editor._image_callback = callback;
                        // trigger some Vaadin function (that opens a dialog)
                        editor.$server.filePickerCallback(meta.filetype);
                    }
                }
                """);
            configure("plugins", "code", "link", "image");
            configure("toolbar", "toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | outdent indent' | link image");
            setValue("<p>Voi <strong>jorma</strong>!<p>");
            setHeight("700px");
        }

        @ClientCallable
        public void filePickerCallback(String filetype) {

            // Same callback for images and links, filetype should be checked in real implementation

            Dialog dialog = new Dialog("File picker callback");

            dialog.add(new VerticalLayout(){{
                // Using Button here, but you can use any component
                for(int i = 0; i < 5; i++) {
                    int finalI = i;
                    add(new Button("image"+i+".jpg") {{
                        addClickListener(e -> {
                            FileCallbackTinyMce.this.getElement().executeJs("this._image_callback('http://example.com/image" + finalI + ".jpg');");
                            dialog.close();
                        });
                    }});
                }
            }});

            dialog.open();
            // Make sure the Vaadin dialog is on top of the Tinymce dialog
            getElement().executeJs("""
                    document.querySelector("vaadin-dialog-overlay").style.zIndex = 1000000;
                    """);
        }
    }

    @Autowired
    ActiveEditors activeEditors;

    public ImagePickerCallback() {
        hugeRte = new FileCallbackTinyMce();

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
