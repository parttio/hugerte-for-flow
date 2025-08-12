/**
 * @license
 * Copyright (c) 2019 - 2025 Vaadin Ltd.
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */
import {html, LitElement} from 'lit';
import {defineCustomElement} from '@vaadin/component-base/src/define.js';
import {ElementMixin} from '@vaadin/component-base/src/element-mixin.js';
import {PolylitMixin} from '@vaadin/component-base/src/polylit-mixin.js';
import {ThemableMixin} from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import { FieldMixin } from '@vaadin/field-base/src/field-mixin.js';
import "./hugerte_addon/hugerte/hugerte.min.js";

// import { CustomFieldMixin } from './vaadin-custom-field-mixin.js';
// import { customFieldStyles } from './vaadin-custom-field-styles.js';

class HugeRte extends FieldMixin(FocusMixin(KeyboardMixin(ThemableMixin(ElementMixin(PolylitMixin(LitElement)))))) {

    // can be overridden by the server using #setConfig
    baseConfig = {};

    // will be overridden by the server on attachment time
    config = {};

    changeMode = 'change';

    lastSyncedValue = "";

    static get is() {
        return 'vaadin-huge-rte';
    }

    static get styles() {
        return customFieldStyles;
    }

    /** @protected */
    render() {
        return html`
            <div class="vaadin-huge-rte-container">
                <div part="label" @click="${this.focus}">
                    <slot name="label"></slot>
                    <span part="required-indicator" aria-hidden="true"></span>
                </div>

                <div class="inputs-wrapper" part="input-fields">
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

        this._initEditor();

        this._tooltipController = new TooltipController(this);
        this.addController(this._tooltipController);
        this._tooltipController.setShouldShow(() => {
            // const inputs = target.inputs || [];
            // return !inputs.some((el) => el.opened);
            return true;
        });
    }

    disconnectedCallback() {
        if(this.beforeUnloadHandler) {
            window.removeEventListener('beforeunload', this.beforeUnloadHandler);
        }

        super.disconnectedCallback();
    }

    _initEditor() {
        let target = document.createElement('div');
        this.append(target); // will be put into the default slot

        // create combined config, with based config being overriden by any additiona configurations
        const config = {
            ...this.baseConfig,
            ...this.config,
            suffix: '.min',
            promotion: false,
            target,
            readonly: !this.enabled, // comes form the field mixin
            setup: (ed) => {
                this.editor = ed;

                ed.on('init', () => {
                    if (this.isInDialog()) {
                        // This is inside a shadowroot (Dialog in Vaadin)
                        // and needs some hacks to make this nagigateable with keyboard
                        if (c.tabIndex < 0) {
                            // make the wrapping element also focusable
                            c.setAttribute("tabindex", 0);
                        }
                        // on focus to wrapping element, pass forward to editor
                        c.addEventListener("focus", () => {
                            ed.focus();
                        });
                        // Move aux element as child from body to element to fix menus in modal Dialog
                        Array.from(document.getElementsByClassName('tox-hugerte-aux')).forEach(aux => {
                            if (!aux.dontmove) {
                                aux.parentElement.removeChild(aux);
                                // Fix to allow menu grow outside Dialog
                                aux.style.position = 'absolute';
                                c.editor = ed;
                                c.appendChild(aux);
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
                    const ourStyles = document.head.querySelector("[href*='hugerteLumo.css']");
                    if (ourStyles) {
                        document.head.append(ourStyles);
                    } else {
                        console.error("Could not find 'hugerteLumo.css'. Has it been renamed or moved?");
                    }
                });

                ed.on('change', () => {
                    if (this.changeMode === 'timeout') {
                        this.syncValue();
                    }
                });
                ed.on('blur', () => {
                    const blurEvent = new Event("tblur");
                    c.dispatchEvent(blurEvent);
                    c.removeAttribute("focused");
                    this.syncValue();
                });
                ed.on('focus', () => {
                    const event = new Event("tfocus");
                    c.setAttribute("focused", "");
                    c.dispatchEvent(event);
                });

                ed.on('input', () => {
                    if (this.changeMode === 'timeout') {
                        this.syncValue();
                    }
                });

            }
        };

        ta.innerHTML = initialContent;

        hugerte.init(config);
    }

    syncValue(){
        const event = new Event("tchange");
        event.htmlString = this.editor.getContent();

        if (this.lastSyncedValue !== event.htmlString) {
            this.dispatchEvent(event);
            this.lastSyncedValue = event.htmlString;
        }
    }

    setEditorContent(html) {
        // Delay setting the content, otherwise there is issue during reattach
        setTimeout(() => {
            this.lastSyncedValue = this.editor.setContent(html, {format: 'html'});
        }, 50);
    }

    replaceSelectionContent(html) {
        this.editor.selection.setContent(html);
        syncValue();
    }

    focus() {
        if (this.isInDialog()) {
            setTimeout(() => this.editor.focus(), 150)
        } else {
            this.editor.focus();
        }
    }

    setEnabled(enabled) {
        // Debounce is needed if mode is attempted to be changed more than once
        // during the attach
        if (this.readonlyTimeout) {
            clearTimeout(this.readonlyTimeout);
        }

        this.readonlyTimeout = setTimeout(() => {
            this.editor.mode.set(enabled ? 'design' : 'readonly');
        }, 20);
    }

    setMode(newChangeMode) {
        this.changeMode = newChangeMode;
    }

    isInDialog() {
        let inDialog = false;
        let parent = c.parentElement;
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
                const blurEvent = new Event("tblur");
                c.dispatchEvent(blurEvent);
                const changeEvent = new Event("tchange");
                changeEvent.htmlString = c.$connector.editor.getContent();
                c.dispatchEvent(changeEvent);
            };

            window.addEventListener("beforeunload", this.beforeUnloadHandler);
        }
    }

    closeToolbarOverflowMenu() {
        if (this.editor.queryCommandState('ToggleToolbarDrawer')) {
            this.editor.execCommand('ToggleToolbarDrawer');
        }
    }

}

defineCustomElement(HugeRte);

export {HugeRte};
