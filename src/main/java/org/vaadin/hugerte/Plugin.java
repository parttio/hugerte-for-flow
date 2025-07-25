package org.vaadin.hugerte;

public enum Plugin {
    //@formatter:off    
    ADVLIST("advlist"),
    ANCHOR("anchor"),
    AUTOLINK("autolink"),

    /**
     * Enables the "autoresize" plugin. That will make the editor take as less space as needed to
     * show all the content. Please be aware, that the editor might be smaller than a fixed height
     * you have set.
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
