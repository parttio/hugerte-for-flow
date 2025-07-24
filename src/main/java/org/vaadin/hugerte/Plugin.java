package org.vaadin.hugerte;

public enum Plugin {
    //@formatter:off    
    ADVLIST("advlist"),
    ANCHOR("anchor"),
    AUTOLINK("autolink"),
    AUTORESIZE("autoresize"),
    AUTOSAVE("autosave"),
    CHARMAP("charmap"),
    CODE("code"),
    CODESAMPLE("codesample"),
//    COLORPICKER("colorpicker"), // removed from hugerte ???
//    CONTEXTMENU("contextmenu"),  // removed from hugerte ???
    DIRECTIONALITY("directionality"),
    EMOTICONS("emoticons"),
//    FULLPAGE("fullpage"),  // removed from hugerte ???
    FULLSCREEN("fullscreen"),
    HELP("help"),
//    HR("hr"),  // removed from hugerte ???
    IMAGE("image"),
//    IMAGE_TOOLS("imagetools"), // removed from hugerte ???
    IMPORT_CSS("importcss"),
    INSERT_DATETIME("insertdatetime"),
//    LEGACYOUTPUT("legacyoutput"),  // removed from hugerte ???
    LINK("link"),
    LISTS("lists"),
    MEDIA("media"),
    NONBREAKING("nonbreaking"),
//    NONEDITABLE("noneditable"), // removed from hugerte ???
    PAGEBREAK("pagebreak"),
//    PASTE("paste"),  // removed from hugerte ???
    PREVIEW("preview"),
//    PRINT("print"),  // removed from hugerte ???
    QUICKBARS("quickbars"),
    SAVE("save"),
    SEARCH_REPLACE("searchreplace"),
//    SPELLCHECKER("spellchecker"),  // removed from hugerte ???
//    TABFOCUS("tabfocus"),  // removed from hugerte ???
    TABLE("table"),
    TEMPLATE("template"),
//    TEXT_COLOR("textcolor"),  // removed from hugerte ???
//    TEXT_PATTERN("textpattern"),  // removed from hugerte ???
    VISUAL_BLOCKS("visualblocks"),
    VISUAL_CHARS("visualchars"),
    WORDCOUNT("wordcount");
    //@formatter:on

    public final String pluginLabel;

    private Plugin(String pluginLabel) {
        this.pluginLabel = pluginLabel;
    }

}
