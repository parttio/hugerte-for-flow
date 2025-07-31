package org.vaadin.hugerte;

public enum Plugin {
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
     *     Be aware, that the editor might be smaller than a fixed height that you have set. To prevent any issues
     *     regarding that, the editor will remove the "height" inline styling. Also be aware, that
     *     the editor will take all available space, when growing. This may lead to unintuitive scrolling behavior.
     * </p><p>
     *     To prevent the toolbar of the editor from being "scrolled out" of the visible area of the field, you need
     *     to configure a max-height for the editor by providing the configuration flag {@code max-height}:
     *     <pre>
     *HugeRte myRte = new HugeRte();
     *myRte.configurePlugins(Plugin.AUTORESIZE);
     *myRte.configure("max_height", 400);
     *     </pre>
     *     Please note, that the editor only accepts numerical values. That value is interpreted as pixels and cannot
     *     be changed, while the editor is attached. This is a limitation of the underlying editor engine. When
     *     providing a max-height this way, it is not necessary to also provide a normal css max-height.
     * </p>
     *
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

    public final String pluginLabel;

    private Plugin(String pluginLabel) {
        this.pluginLabel = pluginLabel;
    }

}
