package org.vaadin.hugerte;

/**
 * Thrown, when a conflict in the configuration has been detected. This is for instance the case, when
 * a plugin and another configuration should not be used in combination (e.g. config "resize" and plugin "autoresize").
 */
public class ConfigurationConflictException extends RuntimeException {
    public ConfigurationConflictException(String message) {
        super(message);
    }
}
