package org.vaadin.hugerte;

/**
 * Menus of the editor's menubar, to be used with {@link HugeRte#configureMenubar(Menubar...)}. Plugins add their
 * items to the matching menus.
 */
public enum Menubar implements ClientSideReference {
    //@formatter:off
    /** The "File" menu. */
    FILE("file"),
    /** The "Edit" menu. */
    EDIT("edit"),
    /** The "View" menu. */
    VIEW("view"),
    /** The "Insert" menu. */
    INSERT("insert"),
    /** The "Format" menu. */
    FORMAT("format"),
    /** The "Tools" menu. */
    TOOLS("tools"),
    /** The "Table" menu. Requires the {@link Plugin#TABLE} plugin. */
    TABLE("table"),
    /** The "Help" menu. Requires the {@link Plugin#HELP} plugin. */
    HELP("help");
    //@formatter:on

    private final String clientSideRepresentation;

    Menubar(String clientSideRepresentation) {
        this.clientSideRepresentation = clientSideRepresentation;
    }

    @Override
    public String getClientSideRepresentation() {
        return clientSideRepresentation;
    }
}
