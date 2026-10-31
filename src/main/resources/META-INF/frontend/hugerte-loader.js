/**
 * @license
 * Copyright (c) 2025 Team Parttio and Vaadin Ltd.
 * https://github.com/parttio/hugerte-for-flow/
 * This program is available under Apache License Version 2.0, available at https://vaadin.com/license/
 */

/*
 * Loads the HugeRTE engine from the "hugerte" npm package, bundled by Vite.
 *
 * The core, its theme, model, icons and the default skins are always needed and thus imported statically. The core
 * must come first, as all other HugeRTE scripts register themselves to the global "hugerte" object.
 *
 * HugeRTE normally loads plugins, skins and language files via URLs relative to its own script. When bundled, there
 * is no such base URL, so the ones an editor config refers to are imported (as separate chunks, to not bundle all of
 * them into every application) before calling hugerte.init(). Skin and content CSS is registered via
 * hugerte.Resource by the imported *.js files, which HugeRTE then prefers over fetching CSS files.
 *
 * Plugins, skins or languages not known here fall back to HugeRTE's default URL based loading.
 */
import 'hugerte';
import 'hugerte/models/dom/model.js';
import 'hugerte/themes/silver/theme.js';
import 'hugerte/icons/default/icons.js';
import 'hugerte/skins/ui/oxide/skin.js';
import 'hugerte/skins/ui/oxide/skin.shadowdom.js';
import 'hugerte/skins/ui/oxide/content.js';
import 'hugerte/skins/ui/oxide/content.inline.js';
import 'hugerte/skins/content/default/content.js';

const PLUGINS = {
    accordion: () => import('hugerte/plugins/accordion/plugin.js'),
    advlist: () => import('hugerte/plugins/advlist/plugin.js'),
    anchor: () => import('hugerte/plugins/anchor/plugin.js'),
    autolink: () => import('hugerte/plugins/autolink/plugin.js'),
    autoresize: () => import('hugerte/plugins/autoresize/plugin.js'),
    autosave: () => import('hugerte/plugins/autosave/plugin.js'),
    charmap: () => import('hugerte/plugins/charmap/plugin.js'),
    code: () => import('hugerte/plugins/code/plugin.js'),
    codesample: () => import('hugerte/plugins/codesample/plugin.js'),
    directionality: () => import('hugerte/plugins/directionality/plugin.js'),
    emoticons: () => import('hugerte/plugins/emoticons/plugin.js'),
    fullscreen: () => import('hugerte/plugins/fullscreen/plugin.js'),
    help: () => import('hugerte/plugins/help/plugin.js'),
    image: () => import('hugerte/plugins/image/plugin.js'),
    importcss: () => import('hugerte/plugins/importcss/plugin.js'),
    insertdatetime: () => import('hugerte/plugins/insertdatetime/plugin.js'),
    link: () => import('hugerte/plugins/link/plugin.js'),
    lists: () => import('hugerte/plugins/lists/plugin.js'),
    media: () => import('hugerte/plugins/media/plugin.js'),
    nonbreaking: () => import('hugerte/plugins/nonbreaking/plugin.js'),
    pagebreak: () => import('hugerte/plugins/pagebreak/plugin.js'),
    preview: () => import('hugerte/plugins/preview/plugin.js'),
    quickbars: () => import('hugerte/plugins/quickbars/plugin.js'),
    save: () => import('hugerte/plugins/save/plugin.js'),
    searchreplace: () => import('hugerte/plugins/searchreplace/plugin.js'),
    table: () => import('hugerte/plugins/table/plugin.js'),
    template: () => import('hugerte/plugins/template/plugin.js'),
    visualblocks: () => import('hugerte/plugins/visualblocks/plugin.js'),
    visualchars: () => import('hugerte/plugins/visualchars/plugin.js'),
    wordcount: () => import('hugerte/plugins/wordcount/plugin.js'),
};

// key: skin name, resourceId: the identifier the skin's *.js files use for hugerte.Resource
const UI_SKINS = {
    'oxide': {
        resourceId: 'default',
        load: () => Promise.resolve() // imported statically
    },
    'oxide-dark': {
        resourceId: 'dark',
        load: () => Promise.all([
            import('hugerte/skins/ui/oxide-dark/skin.js'),
            import('hugerte/skins/ui/oxide-dark/skin.shadowdom.js'),
            import('hugerte/skins/ui/oxide-dark/content.js'),
            import('hugerte/skins/ui/oxide-dark/content.inline.js'),
        ])
    },
    'hugerte-5': {
        resourceId: 'hugerte-5',
        load: () => Promise.all([
            import('hugerte/skins/ui/hugerte-5/skin.js'),
            import('hugerte/skins/ui/hugerte-5/skin.shadowdom.js'),
            import('hugerte/skins/ui/hugerte-5/content.js'),
            import('hugerte/skins/ui/hugerte-5/content.inline.js'),
        ])
    },
    'hugerte-5-dark': {
        resourceId: 'hugerte-5-dark',
        load: () => Promise.all([
            import('hugerte/skins/ui/hugerte-5-dark/skin.js'),
            import('hugerte/skins/ui/hugerte-5-dark/skin.shadowdom.js'),
            import('hugerte/skins/ui/hugerte-5-dark/content.js'),
            import('hugerte/skins/ui/hugerte-5-dark/content.inline.js'),
        ])
    },
};

const CONTENT_SKINS = {
    'default': () => Promise.resolve(), // imported statically
    'dark': () => import('hugerte/skins/content/dark/content.js'),
    'document': () => import('hugerte/skins/content/document/content.js'),
    'writer': () => import('hugerte/skins/content/writer/content.js'),
    'hugerte-5': () => import('hugerte/skins/content/hugerte-5/content.js'),
    'hugerte-5-dark': () => import('hugerte/skins/content/hugerte-5-dark/content.js'),
};

const EMOTICON_DATABASES = {
    emojis: () => import('hugerte/plugins/emoticons/js/emojis.js'),
    emojiimages: () => import('hugerte/plugins/emoticons/js/emojiimages.js'),
};

// keyboard navigation help pages of the help plugin
const HELP_KEYNAV = {
    ar: () => import('hugerte/plugins/help/js/i18n/keynav/ar.js'),
    bg_BG: () => import('hugerte/plugins/help/js/i18n/keynav/bg_BG.js'),
    ca: () => import('hugerte/plugins/help/js/i18n/keynav/ca.js'),
    cs: () => import('hugerte/plugins/help/js/i18n/keynav/cs.js'),
    da: () => import('hugerte/plugins/help/js/i18n/keynav/da.js'),
    de: () => import('hugerte/plugins/help/js/i18n/keynav/de.js'),
    el: () => import('hugerte/plugins/help/js/i18n/keynav/el.js'),
    en: () => import('hugerte/plugins/help/js/i18n/keynav/en.js'),
    es: () => import('hugerte/plugins/help/js/i18n/keynav/es.js'),
    eu: () => import('hugerte/plugins/help/js/i18n/keynav/eu.js'),
    fa: () => import('hugerte/plugins/help/js/i18n/keynav/fa.js'),
    fi: () => import('hugerte/plugins/help/js/i18n/keynav/fi.js'),
    fr_FR: () => import('hugerte/plugins/help/js/i18n/keynav/fr_FR.js'),
    he_IL: () => import('hugerte/plugins/help/js/i18n/keynav/he_IL.js'),
    hi: () => import('hugerte/plugins/help/js/i18n/keynav/hi.js'),
    hr: () => import('hugerte/plugins/help/js/i18n/keynav/hr.js'),
    hu_HU: () => import('hugerte/plugins/help/js/i18n/keynav/hu_HU.js'),
    id: () => import('hugerte/plugins/help/js/i18n/keynav/id.js'),
    it: () => import('hugerte/plugins/help/js/i18n/keynav/it.js'),
    ja: () => import('hugerte/plugins/help/js/i18n/keynav/ja.js'),
    kk: () => import('hugerte/plugins/help/js/i18n/keynav/kk.js'),
    ko_KR: () => import('hugerte/plugins/help/js/i18n/keynav/ko_KR.js'),
    ms: () => import('hugerte/plugins/help/js/i18n/keynav/ms.js'),
    nb_NO: () => import('hugerte/plugins/help/js/i18n/keynav/nb_NO.js'),
    nl: () => import('hugerte/plugins/help/js/i18n/keynav/nl.js'),
    pl: () => import('hugerte/plugins/help/js/i18n/keynav/pl.js'),
    pt_BR: () => import('hugerte/plugins/help/js/i18n/keynav/pt_BR.js'),
    pt_PT: () => import('hugerte/plugins/help/js/i18n/keynav/pt_PT.js'),
    ro: () => import('hugerte/plugins/help/js/i18n/keynav/ro.js'),
    ru: () => import('hugerte/plugins/help/js/i18n/keynav/ru.js'),
    sk: () => import('hugerte/plugins/help/js/i18n/keynav/sk.js'),
    sl_SI: () => import('hugerte/plugins/help/js/i18n/keynav/sl_SI.js'),
    sv_SE: () => import('hugerte/plugins/help/js/i18n/keynav/sv_SE.js'),
    th_TH: () => import('hugerte/plugins/help/js/i18n/keynav/th_TH.js'),
    tr: () => import('hugerte/plugins/help/js/i18n/keynav/tr.js'),
    uk: () => import('hugerte/plugins/help/js/i18n/keynav/uk.js'),
    vi: () => import('hugerte/plugins/help/js/i18n/keynav/vi.js'),
    zh_CN: () => import('hugerte/plugins/help/js/i18n/keynav/zh_CN.js'),
    zh_TW: () => import('hugerte/plugins/help/js/i18n/keynav/zh_TW.js'),
};

function toNameList(value) {
    if (Array.isArray(value)) {
        return value;
    }

    return typeof value === "string" ? value.split(/[\s,]+/).filter(name => name) : [];
}

/**
 * Loads all bundled HugeRTE resources the given config refers to. The config is adapted, so that HugeRTE
 * uses the bundled skins instead of trying to load them from URLs.
 * @param config editor config that is going to be passed to hugerte.init()
 * @returns {Promise<object>} the global hugerte object
 */
export async function loadHugeRte(config) {
    const loaders = [];

    const plugins = toNameList(config.plugins);
    plugins.filter(name => PLUGINS[name]).forEach(name => loaders.push(PLUGINS[name]()));

    if (plugins.includes('emoticons') && !config.emoticons_database_url) {
        const database = EMOTICON_DATABASES[config.emoticons_database ?? 'emojis'];
        if (database) {
            loaders.push(database());
        }
    }

    const language = config.language;
    const loadLanguage = language && language !== 'en' && !config.language_url;
    if (loadLanguage) {
        // unknown languages are left to HugeRTE, which then reports the failed language loading
        loaders.push(import(`./hugerte-langs/${language}.js`).catch(() => undefined));
    }

    if (plugins.includes('help')) {
        loaders.push(HELP_KEYNAV.en());
        if (loadLanguage && HELP_KEYNAV[language]) {
            loaders.push(HELP_KEYNAV[language]());
        }
    }

    // a custom skin_url means the skin is served by the application
    const skin = UI_SKINS[config.skin ?? 'oxide'];
    if (skin && !config.skin_url) {
        // HugeRTE looks up bundled skin css by the skin_url as identifier
        config.skin_url = skin.resourceId;
        loaders.push(skin.load());
    }

    toNameList(config.content_css ?? 'default')
        .filter(name => CONTENT_SKINS[name])
        .forEach(name => loaders.push(CONTENT_SKINS[name]()));

    await Promise.all(loaders);

    return window.hugerte;
}
