package org.vaadin.hugerte;

/**
 * Plugins of the editor, to be enabled with {@link HugeRte#configurePlugins(Plugin...)}. Many {@link Toolbar} and
 * {@link Menubar} items need a matching plugin.
 */
public enum Plugin implements ClientSideReference {
    //@formatter:off    
    /** Adds collapsible accordion sections. */
    ACCORDION("accordion"),
    /** Adds more list styles, e.g. roman numbers, to the list toolbar items. Requires {@link #LISTS}. */
    ADVLIST("advlist"),
    /** Adds inserting anchors (named targets for links). */
    ANCHOR("anchor"),
    /** Converts typed URLs automatically to links. */
    AUTOLINK("autolink"),

    /**
     * <p>
     *      Enables the "autoresize" plugin. This will let the editor take as much space as necessary to display
     *      all the content.
     * </p><p>
     *     Please note, that the normal height settings may conflict with the editor's behavior.
     * </p><p>
     *     To prevent visual glitches or misbehavior, e.g. the toolbar of the editor from being "scrolled out"
     *     of the visible area of the field, you should apply min-max height restrictions on the editor directly, e.g.
     * </p><pre>
     *HugeRte myRte = new HugeRte();
     *myRte.configurePlugins(Plugin.AUTORESIZE);
     *myRte.setEditorMaxHeight(400);
     * </pre>
     */
    AUTORESIZE("autoresize"),
    /** Stores the content to the browser's local storage and asks to restore it when the editor is opened again. */
    AUTOSAVE("autosave"),
    /** Adds a dialog for inserting special characters. */
    CHARMAP("charmap"),
    /** Adds a dialog for editing the HTML source code. */
    CODE("code"),
    /** Adds inserting and editing code samples with syntax highlighting. */
    CODESAMPLE("codesample"),
    /** Adds toolbar items for the text direction (left-to-right, right-to-left). */
    DIRECTIONALITY("directionality"),
    /** Adds a dialog for inserting emoticons. */
    EMOTICONS("emoticons"),
    /** Adds a full screen mode for the editor. */
    FULLSCREEN("fullscreen"),
    /** Adds a help dialog with keyboard shortcuts and plugin information. */
    HELP("help"),
    /** Adds inserting and editing images. */
    IMAGE("image"),
    /** Imports CSS classes from the content CSS to the format menus. */
    IMPORT_CSS("importcss"),
    /** Adds inserting the current date and time. */
    INSERT_DATETIME("insertdatetime"),
    /** Adds inserting and editing links. */
    LINK("link"),
    /** Normalizes the list behavior, e.g. for indenting list items. */
    LISTS("lists"),
    /** Adds embedding media, e.g. videos. */
    MEDIA("media"),
    /** Adds inserting non-breaking spaces. */
    NONBREAKING("nonbreaking"),
    /** Adds inserting page breaks. */
    PAGEBREAK("pagebreak"),
    /** Adds a preview of the content. */
    PREVIEW("preview"),
    /** Adds context toolbars for selected text and empty lines. */
    QUICKBARS("quickbars"),
    /** Adds a save toolbar item. It calls the function given in the "save_onsavecallback" option. */
    SAVE("save"),
    /** Adds a search and replace dialog. */
    SEARCH_REPLACE("searchreplace"),
    /** Adds inserting and editing tables. */
    TABLE("table"),
    /** Adds inserting predefined templates. The templates are configured with the "templates" option. */
    TEMPLATE("template"),
    /** Adds showing block elements visually, e.g. paragraphs and headings. */
    VISUAL_BLOCKS("visualblocks"),
    /** Adds showing invisible characters, e.g. non-breaking spaces. */
    VISUAL_CHARS("visualchars"),
    /** Adds a word count to the status bar. */
    WORDCOUNT("wordcount");
    //@formatter:on

    private final String clientSideRepresentation;

    Plugin(String clientSideRepresentation) {
        this.clientSideRepresentation = clientSideRepresentation;
    }

    @Override
    public String getClientSideRepresentation() {
        return clientSideRepresentation;
    }
}
