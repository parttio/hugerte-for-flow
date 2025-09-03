package org.vaadin.hugerte;

public enum ValueChangeMode {
    ON_CHANGE("change"),
    ON_BLUR("blur"),
    TIMEOUT("timeout");

    private final String clientSide;

    ValueChangeMode(String clientSide) {
        this.clientSide = clientSide;
    }

    public String getClientSide() {
        return clientSide;
    }

    public static ValueChangeMode fromClientSide(String clientSide) {
        for (ValueChangeMode mode : values()) {
            if (mode.clientSide.equals(clientSide)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown value change mode: " + clientSide);
    }
}