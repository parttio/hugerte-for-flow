/**
 * @license
 * Copyright (c) 2025 Team Parttio and Vaadin Ltd.
 * https://github.com/parttio/hugerte-for-flow/
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
import {css, html, LitElement} from 'lit';
import {defineCustomElement} from '@vaadin/component-base/src/define.js';
import {ElementMixin} from '@vaadin/component-base/src/element-mixin.js';
import {PolylitMixin} from '@vaadin/component-base/src/polylit-mixin.js';
import {FieldMixin} from '@vaadin/field-base/src/field-mixin.js';
import {ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import {inputFieldShared} from '@vaadin/vaadin-lumo-styles/mixins/input-field-shared.js';
import {SlotStylesMixin} from '@vaadin/component-base/src/slot-styles-mixin.js';
import {ThemeDetectionMixin} from "@vaadin/vaadin-themable-mixin/vaadin-theme-detection-mixin.js";

import {diff_match_patch} from 'diff-match-patch';

class HugeRte extends SlotStylesMixin(
    FieldMixin(
        ThemableMixin(
            ElementMixin(
                ThemeDetectionMixin(
                    PolylitMixin(LitElement))))))
    // FocusMixin is explicitly not used, as it would mess up the focused attribute
{

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
        },
        focused: {
            type: Boolean,
            reflectToAttribute: true
        }
    }

    static get styles() {
        return [inputFieldShared, css`
            :host {
                display: flex;
                flex-direction: column;
            }

            .vaadin-huge-rte-container {
                align-self: stretch;
                flex-grow: 1;

                display: flex;
                flex-direction: column;
                row-gap: var(--lumo-space-xs);
            }
        `];
    }

    /** @protected */
    render() {
        return html`
            <div class="vaadin-huge-rte-container vaadin-field-container">
                <div part="label" @click="${this.focus}">
                    <slot name="label"></slot>
                    <span part="required-indicator" aria-hidden="true"></span>
                </div>

                <div part="input-field">
                    <slot></slot>
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

                        if (this._lastSyncedValue) {
                            editor.setContent(this._lastSyncedValue);
                            this.onValueChange(); // flush updated value - needed, if the initial value is not original hugerte html
                        }

                        this.updateReadonlyMode();
                    });

                    editor.on('change', e => {
                        this.onValueChangeIfMode("change");
                    });

                    // we need to fire custom focus and blur events and trigger the Vaadin events on the
                    // client. If we would simply forward them, it would lead to a mass of focus and blur events.
                    editor.on('focus', e => {
                        this.focused = true;

                        this.dispatchEvent(new CustomEvent("_focus"));
                    });

                    editor.on('blur', e => {
                        this.focused = false;

                        this.closeToolbarOverflowMenu();
                        this.onValueChangeIfMode("blur");

                        this.dispatchEvent(new CustomEvent("_blur"));
                    });

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
        this.editor?.mode.set((this.disabled || this.readonly) ? 'readonly' : 'design');
    }

    static get is() {
        return 'vaadin-huge-rte';
    }

}

defineCustomElement(HugeRte);

export {HugeRte};
