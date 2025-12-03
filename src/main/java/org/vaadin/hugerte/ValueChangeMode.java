package org.vaadin.hugerte;

/**
 * Enumeration of value change modes for the HugeRTE.
 */
public enum ValueChangeMode implements ClientSideReference {


    /**
     * Syncs the value with the server on every "change" event, that is fired by the editor. This usually
     * happens either when a new line is added, whole words are deleted at once or the editor loses the focus.
     */
    ON_CHANGE("change"),

    /**
     * Syncs the value with the server, when the editor loses the focus ("blur event").
     */
    ON_BLUR("blur"),

    /**
     * <p>
     * Syncs the value after the last change, when a certain amount of time has passed. If before that timeout any
     * additional changes are made, the timeout is reset and starts again.
     * </p><p>
     * This is the equivalent to Vaadin's native {@link com.vaadin.flow.data.value.ValueChangeMode#LAZY}
     * </p>
     */
    TIMEOUT("timeout"),

    /**
     * <p>
     * Syncs the value periodically regardless of any user events, as long as there are changes to sync.
     * </p><p>
     * This is the equivalent to Vaadin's native {@link com.vaadin.flow.data.value.ValueChangeMode#TIMEOUT}
     * </p>
     */
    INTERVAL("interval");

    private final String clientSideRepresentation;

    ValueChangeMode(String clientSideRepresentation) {
        this.clientSideRepresentation = clientSideRepresentation;
    }


    @Override
    public String getClientSideRepresentation() {
        return clientSideRepresentation;
    }

    /**
     * Interprets the given string as the client side representation of an enum and returns the matching instance.
     * @param clientSide client side representation
     * @return instance
     * @throws IllegalArgumentException on any unknown string
     */
    public static ValueChangeMode fromClientSide(String clientSide) {
        for (ValueChangeMode mode : values()) {
            if (mode.clientSideRepresentation.equals(clientSide)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown value change mode: " + clientSide);
    }
}