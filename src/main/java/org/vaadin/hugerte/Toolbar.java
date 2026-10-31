package org.vaadin.hugerte;

/**
 * Toolbar items of the editor, to be used with {@link HugeRte#configureToolbar(Toolbar...)}. Many of the items need a
 * matching plugin to be enabled with {@link HugeRte#configurePlugins(Plugin...)}, otherwise they are not shown.
 */
public enum Toolbar implements ClientSideReference {
    //@formatter:off
    /** A separator between toolbar groups. */
    SEPARATOR("|"),
    /** Undoes the last change. */
    UNDO("undo"),
    /** Redoes the last undone change. */
    REDO("redo"),
    /** Inserts an accordion. Requires the {@link Plugin#ACCORDION} plugin. */
    ACCORDION("accordion"),
    /** Removes the selected accordion. Requires the {@link Plugin#ACCORDION} plugin. */
    ACCORDION_REMOVE("accordionremove"),
    /** Drop-down for the block format, e.g. paragraph or heading. */
    BLOCKS("blocks"),
    /** Toggles bold. */
    BOLD("bold"),
    /** Toggles italic. */
    ITALIC("italic"),
    /** Toggles underline. */
    UNDERLINE("underline"),
    /** Toggles strikethrough. */
    STRIKETHROUGH("strikethrough"),
    /** Sets the text color. */
    FORECOLOR("forecolor"),
    /** Sets the background color of the text. */
    BACKCOLOR("backcolor"),
    /** Aligns the block to the left. */
    ALIGN_LEFT("alignleft"),
    /** Centers the block. */
    ALIGN_CENTER("aligncenter"),
    /** Aligns the block to the right. */
    ALIGN_RIGHT("alignright"),
    /** Justifies the block. */
    ALIGN_JUSTIFY("alignjustify"),
    /** Drop-down for the font family. */
    FONT_FAMILY("fontfamily"),
    /** Drop-down for the font size. */
    FONTSIZE("fontsize"),
    /** Toggles a block quote. */
    BLOCKQUOTE("blockquote"),
    /** Toggles a numbered list. The {@link Plugin#LISTS} and {@link Plugin#ADVLIST} plugins enhance it. */
    NUMLIST("numlist"),
    /** Toggles a bullet list. The {@link Plugin#LISTS} and {@link Plugin#ADVLIST} plugins enhance it. */
    BULLIST("bullist"),
    /** Decreases the indentation. */
    OUTDENT("outdent"),
    /** Increases the indentation. */
    INDENT("indent"),
    /** Removes the formatting of the selection. */
    REMOVE_FORMAT("removeformat"),
    /** Opens the help dialog. Requires the {@link Plugin#HELP} plugin. */
    HELP("help"),
    /** Inserts a table or opens the table menu. Requires the {@link Plugin#TABLE} plugin. */
    TABLE("table"),
    /** Deletes the table. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_DELETE("tabledelete"),
    /** Opens the table properties. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_PROPS("tableprops"),
    /** Opens the row properties. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_ROWPROPS("tablerowprops"),
    /** Opens the cell properties. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_CELLPROPS("tablecellprops"),
    /** Inserts a row before the current one. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_INSERT_ROW_BEFORE("tableinsertrowbefore"),
    /** Inserts a row after the current one. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_INSERT_ROW_AFTER("tableinsertrowafter"),
    /** Deletes the current row. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_DELETE_ROW("tabledeleterow"),
    /** Inserts a column before the current one. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_INSERT_COL_BEFORE("tableinsertcolbefore"),
    /** Inserts a column after the current one. Requires the {@link Plugin#TABLE} plugin. */
    TABLE_INSERT_COL_AFTER("tableinsertcolafter"),
    /** Inserts an emoticon. Requires the {@link Plugin#EMOTICONS} plugin. */
    EMOTICONS("emoticons"),
    /** Inserts or edits a link. Requires the {@link Plugin#LINK} plugin. */
    LINK("link"),
    /** Inserts or edits an image. Requires the {@link Plugin#IMAGE} plugin. */
    IMAGE("image"),
    /** Inserts or edits embedded media, e.g. a video. Requires the {@link Plugin#MEDIA} plugin. */
    MEDIA("media"),
    /** Prints the content. */
    PRINT("print"),
    /** Inserts the current date or time. Requires the {@link Plugin#INSERT_DATETIME} plugin. */
    INSERT_DATETIME("insertdatetime"),
    /** Drop-down for the line height. */
    LINE_HEIGHT("lineheight"),
    /** Inserts a special character. Requires the {@link Plugin#CHARMAP} plugin. */
    CHARMAP("charmap"),
    /** Opens the HTML source code for editing. Requires the {@link Plugin#CODE} plugin. */
    CODE("code"),
    /** Toggles the full screen mode. Requires the {@link Plugin#FULLSCREEN} plugin. */
    FULLSCREEN("fullscreen"),
    /** Opens a preview of the content. Requires the {@link Plugin#PREVIEW} plugin. */
    PREVIEW("preview"),
    /** Saves the content. Requires the {@link Plugin#SAVE} plugin. */
    SAVE("save"),
    /** Inserts a page break. Requires the {@link Plugin#PAGEBREAK} plugin. */
    PAGEBREAK("pagebreak"),
    /** Inserts an anchor. Requires the {@link Plugin#ANCHOR} plugin. */
    ANCHOR("anchor"),
    /** Inserts or edits a code sample. Requires the {@link Plugin#CODESAMPLE} plugin. */
    CODE_SAMPLE("codesample"),
    /** Sets the text direction to left-to-right. Requires the {@link Plugin#DIRECTIONALITY} plugin. */
    LTR("ltr"),
    /** Sets the text direction to right-to-left. Requires the {@link Plugin#DIRECTIONALITY} plugin. */
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
