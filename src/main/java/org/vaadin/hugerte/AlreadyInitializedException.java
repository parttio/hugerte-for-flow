package org.vaadin.hugerte;

/**
 * Thrown, when the editor already has been initialized. For instance, the configuration must not be changed
 * afterwards and is a typical source for this exception.
 */
public class AlreadyInitializedException extends RuntimeException {
    public AlreadyInitializedException() {
        super("Cannot apply configuration to the editor, it already has been initialized. You need to " +
              "detach it first");
    }
}
