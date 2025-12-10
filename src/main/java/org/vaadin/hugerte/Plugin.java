package org.vaadin.hugerte;

public enum Plugin implements ClientSideReference {
    //@formatter:off    
    ACCORDION("accordion"),
    ADVLIST("advlist"),
    ANCHOR("anchor"),
    AUTOLINK("autolink"),

    /**
     * <p>
     *      Enables the "autoresize" plugin. This will let take the editor as much space as necessary to display
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
     * </p>
     */
    AUTORESIZE("autoresize"),
    AUTOSAVE("autosave"),
    CHARMAP("charmap"),
    CODE("code"),
    CODESAMPLE("codesample"),
    DIRECTIONALITY("directionality"),
    EMOTICONS("emoticons"),
    FULLSCREEN("fullscreen"),
    HELP("help"),
    IMAGE("image"),
    IMPORT_CSS("importcss"),
    INSERT_DATETIME("insertdatetime"),
    LINK("link"),
    LISTS("lists"),
    MEDIA("media"),
    NONBREAKING("nonbreaking"),
    PAGEBREAK("pagebreak"),
    PREVIEW("preview"),
    QUICKBARS("quickbars"),
    SAVE("save"),
    SEARCH_REPLACE("searchreplace"),
    TABLE("table"),
    TEMPLATE("template"),
    VISUAL_BLOCKS("visualblocks"),
    VISUAL_CHARS("visualchars"),
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
