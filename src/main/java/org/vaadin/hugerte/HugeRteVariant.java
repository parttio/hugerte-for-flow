package org.vaadin.hugerte;

import com.vaadin.flow.component.shared.ThemeVariant;

public enum HugeRteVariant implements ThemeVariant {

    /**
     * By default, when focused, the editor shows a colorized border around the editor area. The color is by
     * default either the primary color or an error color for an invalid editor. With this theme variant, the
     * focused indicator border will not be shown at all.
     */
    NO_EDITOR_FOCUS_HIGHLIGHT("no-editor-focus-highlight"),

    /**
     * By default, an invalid editor will set an error color to its editor area background, similar to other
     * Vaadin input fields. This theme variant disables this behavior. The editor will then only apply an error color
     * to the focus border.
     */
    NO_INVALID_BACKGROUND("no-invalid-background");

    private final String variantName;

    HugeRteVariant(String variantName) {
        this.variantName = variantName;
    }

    @Override
    public String getVariantName() {
        return variantName;
    }
}
