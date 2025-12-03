package org.vaadin.hugerte;

public enum Menubar implements ClientSideReference {
    //@formatter:off
    FILE("file"),
    EDIT("edit"),
    VIEW("view"),
    INSERT("insert"),
    FORMAT("format"),
    TOOLS("tools"),
    TABLE("table"),
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
