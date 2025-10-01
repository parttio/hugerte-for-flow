/**
 * @license
 * Copyright (c) 2025 Team Parttio and Vaadin Ltd.
 * https://github.com/parttio/hugerte-for-flow/
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
import {html, LitElement} from 'lit';
import {defineCustomElement} from '@vaadin/component-base/src/define.js';
import {ElementMixin} from '@vaadin/component-base/src/element-mixin.js';
import {PolylitMixin} from '@vaadin/component-base/src/polylit-mixin.js';
import {FieldMixin} from '@vaadin/field-base/src/field-mixin.js';
import {FocusMixin} from '@vaadin/a11y-base/src/focus-mixin.js';
import {KeyboardMixin} from '@vaadin/a11y-base/src/keyboard-mixin.js';
import {inputFieldShared} from '@vaadin/field-base/src/styles/input-field-shared-styles.js';
import {registerStyles, ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';


import {diff_match_patch} from 'diff-match-patch';


// > Part of the npm integration (not yet working, therefore commented out)
// --- HugeRTE core ---
// @see https://github.com/hugerte/hugerte-docs
// import basic scripts
// import hugerte from 'hugerte';
// import 'hugerte/models/dom';
// import 'hugerte/icons/default';
// import 'hugerte/themes/silver';
// import 'hugerte/skins/content/default/content.js';
// import skin and content css to prevent 404 issues
// to be checked later, if this can be improved
// import 'hugerte/skins/ui/oxide/skin.min.css';
// import oxideContentCss from 'hugerte/skins/ui/oxide/content.min.css?raw';
// import defaultContentCss from 'hugerte/skins/content/default/content.min.css?raw';
// import { loadHugeRtePlugins } from './vaadin-huge-rte-plugins.js';
// < Part of the npm integration (not yet working, therefore commented out)

registerStyles('vaadin-huge-rte', inputFieldShared, { moduleId: 'vaadin-huge-rte-styles' });

class HugeRte extends FieldMixin(FocusMixin(ThemableMixin(ElementMixin(PolylitMixin(LitElement))))) {

    // can be overridden by the server using #setConfig
    rawInitialConfig = {};

    // will be overridden by the server on attachment time
    initialConfig = {};

    _lastSyncedValue = "";
    _lastSyncedValueTimestamp = 0;
    _valueChangeMode = "change";

    _valueChangeTimeout = 2_000;

    static properties = {
        disabled: {
            type: Boolean,
            reflectToAttribute: true
        },
        readonly: {
            type: Boolean,
            reflectToAttribute: true
        }
    }

    /** @protected */
    render() {
        console.info("render");
        return html`
            <style id="vaadin-themable-mixin-style">

                /* LUMO Styles adapted from text field - start*/

                :host([hidden]) {
                    display: none !important;
                }

                :host(:not([has-label])) [part='label'] {
                    display: none;
                }

                [part='label'] {
                    align-self: flex-start;
                    color: var(--vaadin-input-field-label-color, var(--lumo-secondary-text-color));
                    font-weight: var(--vaadin-input-field-label-font-weight, 500);
                    font-size: var(--vaadin-input-field-label-font-size, var(--lumo-font-size-s));
                    transition: color 0.2s;
                    line-height: 1;
                    padding-inline-start: calc(var(--lumo-border-radius-m) / 4);
                    padding-inline-end: 1em;
                    padding-bottom: 0.5em;
                    /* As a workaround for diacritics being cut off, add a top padding and a
                    negative margin to compensate */
                    padding-top: 0.25em;
                    margin-top: -0.25em;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    position: relative;
                    max-width: 100%;
                    box-sizing: border-box;
                }

                :host([focused]:not([readonly])) [part='label'] {
                    color: var(--vaadin-input-field-focused-label-color, var(--lumo-primary-text-color));
                }

                :host(:hover:not([readonly]):not([focused])) [part='label'] {
                    color: var(--vaadin-input-field-hovered-label-color, var(--lumo-body-text-color));
                }

                /* Touch device adjustment */
                @media (pointer: coarse) {
                    :host(:hover:not([readonly]):not([focused])) [part='label'] {
                        color: var(--vaadin-input-field-label-color, var(--lumo-secondary-text-color));
                    }
                }

                :host([has-label])::before {
                    margin-top: calc(var(--lumo-font-size-s) * 1.5);
                }

                :host([has-label][theme~='small'])::before {
                    margin-top: calc(var(--lumo-font-size-xs) * 1.5);
                }

                :host([has-label]) {
                    padding-top: var(--lumo-space-m);
                }

                :host([has-label]) ::slotted([slot='tooltip']) {
                    --vaadin-tooltip-offset-bottom: calc((var(--lumo-space-m) - var(--lumo-space-xs)) * -1);
                }

                :host([required]) [part='required-indicator']::after {
                    content: var(--lumo-required-field-indicator, '\\2022');
                    transition: opacity 0.2s;
                    color: var(--lumo-required-field-indicator-color, var(--lumo-primary-text-color));
                    position: absolute;
                    right: 0;
                    width: 1em;
                    text-align: center;
                }

                :host([invalid]) [part='required-indicator']::after {
                    color: var(--lumo-required-field-indicator-color, var(--lumo-error-text-color));
                }

                [part='error-message'] {
                    margin-left: calc(var(--lumo-border-radius-m) / 4);
                    font-size: var(--vaadin-input-field-error-font-size, var(--lumo-font-size-xs));
                    line-height: var(--lumo-line-height-xs);
                    font-weight: var(--vaadin-input-field-error-font-weight, 400);
                    color: var(--vaadin-input-field-error-color, var(--lumo-error-text-color));
                    will-change: max-height;
                    transition: 0.4s max-height;
                    max-height: 5em;
                }

                :host([has-error-message]) [part='error-message']::before,
                :host([has-error-message]) [part='error-message']::after {
                    content: '';
                    display: block;
                    height: 0.4em;
                }

                :host(:not([invalid])) [part='error-message'] {
                    max-height: 0;
                    overflow: hidden;
                }

                /* RTL specific styles */
                :host([dir='rtl']) [part='required-indicator']::after {
                    right: auto;
                    left: 0;
                }

                :host([dir='rtl']) [part='error-message'] {
                    margin-left: 0;
                    margin-right: calc(var(--lumo-border-radius-m) / 4);
                }

                :host {
                    --_helper-spacing: var(--vaadin-input-field-helper-spacing, 0.4em);
                }

                :host([has-helper]) [part='helper-text']::before {
                    content: '';
                    display: block;
                    height: var(--_helper-spacing);
                }

                [part='helper-text'] {
                    display: block;
                    color: var(--vaadin-input-field-helper-color, var(--lumo-secondary-text-color));
                    font-size: var(--vaadin-input-field-helper-font-size, var(--lumo-font-size-xs));
                    line-height: var(--lumo-line-height-xs);
                    font-weight: var(--vaadin-input-field-helper-font-weight, 400);
                    margin-left: calc(var(--lumo-border-radius-m) / 4);
                    transition: color 0.2s;
                }

                :host(:hover:not([readonly])) [part='helper-text'] {
                    color: var(--lumo-body-text-color);
                }

                :host([disabled]) {
                    color: var(--lumo-disabled-text-color);
                    -webkit-text-fill-color: var(--lumo-disabled-text-color);
                }

                :host([has-helper][theme~='helper-above-field']) [part='helper-text']::before {
                    display: none;
                }

                :host([has-helper][theme~='helper-above-field']) [part='helper-text']::after {
                    content: '';
                    display: block;
                    height: var(--_helper-spacing);
                }

                :host([has-helper][theme~='helper-above-field']) [part='label'] {
                    order: 0;
                    padding-bottom: var(--_helper-spacing);
                }

                :host([has-helper][theme~='helper-above-field']) [part='helper-text'] {
                    order: 1;
                }

                :host([has-helper][theme~='helper-above-field']) [part='label'] + * {
                    order: 2;
                }

                :host([has-helper][theme~='helper-above-field']) [part='error-message'] {
                    order: 3;
                }

                /* Touch device adjustment */
                @media (pointer: coarse) {
                    :host(:hover:not([readonly]):not([focused]):not([disabled])) [part='input-field']::after {
                        opacity: 0;
                    }

                    :host(:active:not([readonly]):not([focused]):not([disabled])) [part='input-field']::after {
                        opacity: 0.2;
                    }
                }
                
                [part='input-field'] {
                    flex-grow: 1;
                    display: flex;
                    flex-direction: column;
                }

                /* Trigger when not focusing using the keyboard */
                :host([focused]:not([focus-ring]):not([readonly])) [part='input-field']::after {
                    transform: scaleX(0);
                    transition-duration: 0.15s, 1s;
                }

                /* Opt-in focus-ring when using pointer devices */
                /* This applies a focus-ring as box-shadow when the element is focused, but
                   the ring is only visible / has a width when the respective CSS property is
                   "enabled" using a value of 1 */
                :host([focused]) [part='input-field'] {
                    /* Borders are implemented using box-shadows as well. To avoid overriding 
                       the border on focus, even if the pointer focus-ring is disabled, we need to:
                       - Duplicate the border box shadow for this rule
                       - Remove the border (by using width of 0) when the focus-ring is visible,
                         which is the same behavior as for the keyboard focus-ring below
                       - Apply the border when the focus ring is not visible
                    */
                    --_pointer-focus-visible: clamp(0, var(--lumo-input-field-pointer-focus-visible, 0), 1);
                    --_conditional-border-width: calc(calc(1 - var(--_pointer-focus-visible)) * var(--_input-border-width));
                    --_conditional-focus-ring-width: calc(var(--_pointer-focus-visible) * var(--_focus-ring-width));
                    box-shadow: inset 0 0 0 var(--_conditional-border-width) var(--_input-border-color),
                    0 0 0 var(--_conditional-focus-ring-width) var(--_focus-ring-color);
                }

                /* Focus-ring when using keyboard navigation */
                :host([focus-ring]) [part='input-field'] {
                    box-shadow: 0 0 0 var(--_focus-ring-width) var(--_focus-ring-color);
                }

                /* Read-only style */
                :host([readonly]) {
                    --vaadin-input-field-border-color: transparent;
                }

                /* Disabled style */
                :host([disabled]) {
                    pointer-events: none;
                    --vaadin-input-field-border-color: var(--lumo-contrast-20pct);
                }

                :host([disabled]) [part='label'],
                :host([disabled]) [part='input-field'] ::slotted([slot$='fix']) {
                    color: var(--lumo-disabled-text-color);
                    -webkit-text-fill-color: var(--lumo-disabled-text-color);
                }

                :host([disabled]) [part='input-field'] ::slotted(:not([slot$='fix'])) {
                    color: var(--_disabled-value-color);
                    -webkit-text-fill-color: var(--_disabled-value-color);
                }

                /* Invalid style */
                :host([invalid]) {
                    --vaadin-input-field-border-color: var(--lumo-error-color);
                    --_focus-ring-color: var(--lumo-error-color-50pct);
                }

                /* LUMO Styles adapted from text field - end*/


                /* Styles customizations for the Huge RTE*/
                [part='label'] {
                    flex-shrink: 0;
                }
                
                .vaadin-huge-rte-container {
                    align-self: stretch;
                    flex-grow: 1;
                }
            </style>
            <div class="vaadin-huge-rte-container">
                <div part="label" @click="${this.focus}">
                    <slot name="label"></slot>
                    <span part="required-indicator" aria-hidden="true"></span>
                </div>

                <div class="inputs-wrapper" part="input-field">
                    <slot id="slot"></slot>
                </div>

                <div part="helper-text">
                    <slot name="helper"></slot>
                </div>

                <div part="error-message">
                    <slot name="error-message"></slot>
                </div>
            </div>

            <slot name="tooltip"></slot>
        `;
    }

    ready() {
        super.ready();

        // implement tooltip support later - might make sense to allow more distinct tooltip handling
        // than just one tooltip, for instance for buttons and so on, especially, since hugerte brings some
        // own tooltips
        // this._tooltipController = new TooltipController(this);
        // this.addController(this._tooltipController);
        // this._tooltipController.setShouldShow(target => {
        //     // const inputs = target.inputs || [];
        //     // return !inputs.some((el) => el.opened);
        //     return true;
        // });
    }

    firstUpdated() {
    }

    async connectedCallback() {
        super.connectedCallback();

        // not sure if this might be an issue, if called here already, since "render" has not yet happend
        // if so, add a check for "has been rendered" for reattachments and also add a call of init editor to
        // first update.
        await this._initEditor();
    }

    disconnectedCallback() {
        if (this.editor) {
            this.editor.remove();
            this.lightDomContainer.remove();

            delete this.lightDomContainer;
            delete this.editor;
        }

        if (this.beforeUnloadHandler) {
            window.removeEventListener('beforeunload', this.beforeUnloadHandler);
        }

        super.disconnectedCallback();
    }

     _initEditor() {
        if (!this.editor) {
            this.lightDomContainer = document.createElement('div');
            this.append(this.lightDomContainer); // will be put into the default slot

            let rawConfig = {};
            if (typeof this.rawInitialConfig === "string") {
                rawConfig = eval("(" + (this.rawInitialConfig?.trim() || "{}") + ")");
            } else if (typeof this.rawInitialConfig === "object") {
                rawConfig = this.rawInitialConfig;
            }

            // create combined config, with based config being overriden by any additiona configurations
            const config = {
                resize: false,
                height: 250,
                ...rawConfig, // raw initial config is a string
                ...this.initialConfig,
                suffix: '.min',
                promotion: false,
                branding: false, // hides the hugerte branding in the editor

                // > Part of the npm integration (not yet working, therefore commented out)
                // skin: false,
                // content_css: false, // due to the postcss lit plugin we cannot pass in the min css files directly
                // content_style: [oxideContentCss, defaultContentCss].join('\n'),
                // < Part of the npm integration (not yet working, therefore commented out)

                target: this.lightDomContainer,

                // readonly: !this.enabled, // comes form the field mixin
                setup: (editor) => {
                    this.editor = editor;

                    editor.on('init', () => {
                        // TODO check if still needed in V25
                        if (this.isInDialog()) {
                            // This is inside a shadowroot (Dialog in Vaadin)
                            // and needs some hacks to make this nagigateable with keyboard
                            if (this.tabIndex < 0) {
                                // make the wrapping element also focusable
                                this.setAttribute("tabindex", 0);
                            }
                            // on focus to wrapping element, pass forward to editor
                            this.addEventListener("focus", () => {
                                editor.focus();
                            });
                            // Move aux element as child from body to element to fix menus in modal Dialog
                            Array.from(document.getElementsByClassName('tox-hugerte-aux')).forEach(aux => {
                                if (!aux.dontmove) {
                                    aux.parentElement.removeChild(aux);
                                    // Fix to allow menu grow outside Dialog
                                    aux.style.position = 'absolute';
                                    this.editor = editor;
                                    this.appendChild(aux);
                                }
                            });
                        } else {
                            const aux = document.getElementsByClassName('tox-hugerte-aux')[0];
                            aux.dontmove = true;
                        }

                        // HugeRTE appends its skin css into the head when gets loaded. Flow applies its css beforehand,
                        // which means, that our custom Lumo css is overridden by the default css of the RTE. Thus our
                        // customization might be ignored and needs additional ugly customization like !importan or
                        // more specific selectors.
                        // Therefore we have this small workaround here to ensure, that our css is loaded AFTER
                        // the hugerte skin css.
                        // Nevertheless, this might break in the future, so for the long term, it might be useful to create
                        // our own lumo skin at some point so that we do not have to hack around this situation.
                        // TODO check if still needed
                        // const ourStyles = document.head.querySelector("[href*='vaadin-huge-rte.css']");
                        // if (ourStyles) {
                        //     document.head.append(ourStyles);
                        // } else {
                        //     console.error("Could not find 'vaadin-huge-rte.css'. Has it been renamed or moved?");
                        // }

                        if (this._lastSyncedValue) {
                            editor.setContent(this._lastSyncedValue);
                            this.onValueChange(); // flush updated value - needed, if the initial value is not original hugerte html
                        }
                    });

                    editor.on('change', e => {
                        this.onValueChangeIfMode("change");
                    });

                    editor.on('blur', e => {
                        this.closeToolbarOverflowMenu();
                        this.onValueChangeIfMode("blur");

                        this.dispatchEvent(new CustomEvent("_blur"));
                    });

                    editor.on('focus', e => this.dispatchEvent(new CustomEvent("_focus")));

                    editor.on('input', e => {
                        if (this.valueChangeMode === "timeout") {
                            clearTimeout(this._valueChangeHandleForTimeout);
                            this._valueChangeHandleForTimeout = setTimeout(this.onValueChange.bind(this), this.valueChangeTimeout);
                        }

                        return this.dispatchEvent(new Event('input'));
                    });
                }
            };


            // > Part of the npm integration (not yet working, therefore commented out)
            // load all configured plugins dynamically
            // if (config.plugins?.length) {
            //     await loadHugeRtePlugins(config.plugins);
            // }
            // < Part of the npm integration (not yet working, therefore commented out)

            hugerte.init(config);
        }
    }

    set value(value) {
        this._lastSyncedValue = value ?? "";

        if (this.editor) {
            this.editor.setContent(this._lastSyncedValue);
        }
    }

    get value() {
        return this.editor?.getContent() ?? this._lastSyncedValue;
    }

    /**
     * Calls #onValueChange(), if the given string matches the current value change mode.
     * @param expectedValueChangeMode expected value change mode
     */
    onValueChangeIfMode(expectedValueChangeMode) {
        if (this.valueChangeMode === expectedValueChangeMode) {
            this.onValueChange();
        }
    }

    /**
     * This method is to be called when ever a value change should be triggered. It will calculate the current value
     * delta, update the "old value" property and send an event to the server.
     */
    onValueChange() {
        let now = Date.now();
        if (this._lastSyncedValueTimestamp < now - 50) { // explicit throttle to prevent too many events fired at all
            this._lastSyncedValueTimestamp = now;

            const currentValue = this.editor?.getContent() ?? this._lastSyncedValue;

            // init lib
            const dmp = new diff_match_patch();
            const patch = dmp.patch_make(this._lastSyncedValue, currentValue);
            const delta = dmp.patch_toText(patch);

            this._lastSyncedValue = currentValue;

            if (delta) {
                this.dispatchEvent(new CustomEvent("_value-delta", {
                    detail: {
                        delta
                    }
                }));
            }
        }
    }

    set valueChangeMode(newValueChangeMode) {
        if(!newValueChangeMode) {
            newValueChangeMode = "change";
        }
        if(this._valueChangeMode !== newValueChangeMode) {
            this._valueChangeMode = newValueChangeMode;

            if (this._valueChangeMode !== "timeout" && this._valueChangeHandleForTimeout) {
                this.stopValueChangeTimeout();
            }

            if(this._valueChangeMode === "interval") {
                this.startValueChangInterval();
            } else if (this._valueChangeHandleForInterval) {
                this.stopValueChangeInterval();
            }

        }
    }

    get valueChangeMode() {
        return this._valueChangeMode;
    }

    set valueChangeTimeout(newTimeout) {
        if (!newTimeout || newTimeout < 0) {
            throw new Error("valueChangeTimeout must be greater than 0");
        }

        if(this._valueChangeTimeout !== newTimeout) {
            this._valueChangeTimeout = newTimeout;

            if (this._valueChangeHandleForTimeout) {
                this.stopValueChangeTimeout();
            } else if (this._valueChangeHandleForInterval) {
                this.startValueChangInterval(); // also stops the current interval
            }

        }
    }

    get valueChangeTimeout() {
        return this._valueChangeTimeout;
    }

    stopValueChangeTimeout() {
        this.onValueChange(); // flush value to server
        clearTimeout(this._valueChangeHandleForTimeout);
        delete this._valueChangeHandleForTimeout;
    }

    stopValueChangeInterval() {
        this.onValueChange(); // flush value to server
        window.clearInterval(this._valueChangeHandleForInterval);
        delete this._valueChangeHandleForInterval;
    }

    startValueChangInterval() {
        if (this._valueChangeHandleForInterval) {
            this.stopValueChangeInterval();
        }

        this._valueChangeHandleForInterval = setInterval(this.onValueChange.bind(this), this.valueChangeTimeout);
    }

    /**
     * Replaces the current selection with the given html snippet. If nothing
     * is selected, the content will be added at the caret's position
     * @param html
     */
    replaceSelectionContent(html) {
        this.editor.selection.setContent(html);
        this.onValueChange();
    }

    focus() {
        if (this.isInDialog()) {
            setTimeout(() => this.editor.focus(), 150)
        } else {
            this.editor.focus();
        }
    }

    // setEnabled(enabled) {
    //     // Debounce is needed if mode is attempted to be changed more than once
    //     // during the attach
    //     if (this.readonlyTimeout) {
    //         clearTimeout(this.readonlyTimeout);
    //     }
    //
    //     this.readonlyTimeout = setTimeout(() => {
    //         this.editor.mode.set(enabled ? 'design' : 'readonly');
    //     }, 20);
    // }

    isInDialog() {
        let inDialog = false;
        let parent = this.parentElement;
        while (parent != null) {
            if (parent.tagName.indexOf("VAADIN-DIALOG") === 0) {
                inDialog = true;
                break;
            }
            parent = parent.parentElement;
        }

        return inDialog;
    }

    saveOnClose() {
        if (!this.beforeUnloadHandler) {
            this.beforeUnloadHandler = (event) => {
                const blurEvent = new Event("blur");
                c.dispatchEvent(blurEvent);

                this.syncValue();
            };

            window.addEventListener("beforeunload", this.beforeUnloadHandler);
        }
    }

    closeToolbarOverflowMenu() {
        if (this.editor.queryCommandState('ToggleToolbarDrawer')) {
            this.editor.execCommand('ToggleToolbarDrawer');
        }
    }

    updated(changedProperties) {
        super.updated(changedProperties);
        if (changedProperties.has("disabled") || changedProperties.has("readonly")) {
            this.updateReadonlyMode();
        }
    }

    updateReadonlyMode() {
        this.editor.mode.set((this.disabled || this.readonly) ? 'readonly' : 'design');
    }

    static get is() {
        return 'vaadin-huge-rte';
    }

}

defineCustomElement(HugeRte);

export {HugeRte};
