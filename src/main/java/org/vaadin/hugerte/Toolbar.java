package org.vaadin.hugerte;

public enum Toolbar implements ClientSideReference {
    //@formatter:off
    SEPARATOR("|"),
    UNDO("undo"),
    REDO("redo"),
    ACCORDION("accordion"),
    ACCORDION_REMOVE("accordionremove"),
    BLOCKS("blocks"),
    BOLD("bold"),
    ITALIC("italic"),
    UNDERLINE("underline"),
    STRIKETHROUGH("strikethrough"),
    FORECOLOR("forecolor"),
    BACKCOLOR("backcolor"),
    ALIGN_LEFT("alignleft"),
    ALIGN_CENTER("aligncenter"),
    ALIGN_RIGHT("alignright"),
    ALIGN_JUSTIFY("alignjustify"),
    FONT_FAMILY("fontfamily"),
    FONTSIZE("fontsize"),
    BLOCKQUOTE("blockquote"),
    NUMLIST("numlist"),
    BULLIST("bullist"),
    OUTDENT("outdent"),
    INDENT("indent"),
    REMOVE_FORMAT("removeformat"),
    HELP("help"),
    TABLE("table"),
    TABLE_DELETE("tabledelete"),
    TABLE_PROPS("tableprops"),
    TABLE_ROWPROPS("tablerowprops"),
    TABLE_CELLPROPS("tablecellprops"),
    TABLE_INSERT_ROW_BEFORE("tableinsertrowbefore"),
    TABLE_INSERT_ROW_AFTER("tableinsertrowafter"),
    TABLE_DELETE_ROW("tabledeleterow"),
    TABLE_INSERT_COL_BEFORE("tableinsertcolbefore"),
    TABLE_INSERT_COL_AFTER("tableinsertcolafter"),
    EMOTICONS("emoticons"),
    LINK("link"),
    IMAGE("image"),
    MEDIA("media"),
    PRINT("print"),
    INSERT_DATETIME("insertdatetime"),
    LINE_HEIGHT("lineheight"),
    CHARMAP("charmap"),
    CODE("code"),
    FULLSCREEN("fullscreen"),
    PREVIEW("preview"),
    SAVE("save"),
    PAGEBREAK("pagebreak"),
    ANCHOR("anchor"),
    CODE_SAMPLE("codesample"),
    LTR("ltr"),
    RTL("rtl");
    //@formatter:on

    private final String clientSideRepresentation;

    Toolbar(String clientSideRepresentation) {
        this.clientSideRepresentation = clientSideRepresentation;
    }

    @Override
    public String getClientSideRepresentation() {
        return clientSideRepresentation;
    }
}
