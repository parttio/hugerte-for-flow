package org.vaadin.hugerte;

/**
 * Directions, that can be set for the editor to be resizable. Please note, that "only horizonzal" resizing is
 * not supported.
 *
 * @see <a href="https://www.tiny.cloud/docs/tinymce/latest/editor-size-options/#resize">Official TinyMCE docs</a>
 */
public enum ResizeDirection {
    /**
     * Resizing is disabled.
     */
    NONE,

    /**
     * Enables vertical resizing.
     */
    VERTICALLY,

    /**
     * Enables vertical and horizontal resizing.
     */
    BOTH;
}