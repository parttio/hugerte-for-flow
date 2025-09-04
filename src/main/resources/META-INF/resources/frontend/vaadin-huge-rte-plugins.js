// > Part of the npm integration (not yet working, therefore commented out)
// // Map of all known HugeRTE plugins → import function
// export const HUGE_RTE_PLUGIN_IMPORTERS = {
//     accordion:     () => import('hugerte/plugins/accordion'),
//     advlist:       () => import('hugerte/plugins/advlist'),
//     anchor:        () => import('hugerte/plugins/anchor'),
//     autolink:      () => import('hugerte/plugins/autolink'),
//     autoresize:    () => import('hugerte/plugins/autoresize'),
//     autosave:      () => import('hugerte/plugins/autosave'),
//     charmap:       () => import('hugerte/plugins/charmap'),
//     code:          () => import('hugerte/plugins/code'),
//     codesample:    () => import('hugerte/plugins/codesample'),
//     directionality:() => import('hugerte/plugins/directionality'),
//     emoticons:     () => import('hugerte/plugins/emoticons'),
//     fullscreen:    () => import('hugerte/plugins/fullscreen'),
//     help:          () => import('hugerte/plugins/help'),
//     image:         () => import('hugerte/plugins/image'),
//     importcss:     () => import('hugerte/plugins/importcss'),
//     insertdatetime:() => import('hugerte/plugins/insertdatetime'),
//     link:          () => import('hugerte/plugins/link'),
//     lists:         () => import('hugerte/plugins/lists'),
//     media:         () => import('hugerte/plugins/media'),
//     nonbreaking:   () => import('hugerte/plugins/nonbreaking'),
//     pagebreak:     () => import('hugerte/plugins/pagebreak'),
//     preview:       () => import('hugerte/plugins/preview'),
//     quickbars:     () => import('hugerte/plugins/quickbars'),
//     save:          () => import('hugerte/plugins/save'),
//     searchreplace: () => import('hugerte/plugins/searchreplace'),
//     table:         () => import('hugerte/plugins/table'),
//     template:      () => import('hugerte/plugins/template'),
//     visualblocks:  () => import('hugerte/plugins/visualblocks'),
//     visualchars:   () => import('hugerte/plugins/visualchars'),
//     wordcount:     () => import('hugerte/plugins/wordcount'),
// };
//
// // Extra assets required by some plugins:
// // - help → keynav i18n files
// // - emoticons → emoji database
// const HELP_KEYNAV_I18N_IMPORTERS = {
//     en: () => import('hugerte/plugins/help/js/i18n/keynav/en.js'),
//     // add more languages if needed
// };
//
// const EMOTICONS_DB_IMPORTER = () =>
//     import('hugerte/plugins/emoticons/js/emojis.min.js');
//
// /**
//  * Dynamically loads HugeRTE plugins (and their required assets) before calling hugerte.init().
//  *
//  * @param {string[]|string} plugins Array or space-separated string of plugin names
//  * @param {object} opts Optional configuration (e.g. { language: 'en' })
//  */
// export async function loadHugeRtePlugins(plugins, opts = {}) {
//     const names = Array.isArray(plugins)
//         ? plugins
//         : String(plugins || '').trim().split(/\s+/).filter(Boolean);
//
//     const tasks = [];
//
//     // Load plugin modules
//     for (const name of names) {
//         const importer = HUGE_RTE_PLUGIN_IMPORTERS[name];
//         if (importer) {
//             tasks.push(importer());
//         } else {
//             console.warn(`[HugeRTE] Plugin "${name}" not found in importer map.`);
//         }
//     }
//
//     // Load help plugin assets (i18n keynav) if needed
//     if (names.includes('help')) {
//         const lang = (opts.language || 'en').toLowerCase();
//         const helpImporter = HELP_KEYNAV_I18N_IMPORTERS[lang] || HELP_KEYNAV_I18N_IMPORTERS.en;
//         tasks.push(helpImporter());
//     }
//
//     // Load emoticons database if needed
//     if (names.includes('emoticons')) {
//         tasks.push(EMOTICONS_DB_IMPORTER());
//     }
//
//     await Promise.all(tasks);
// }
// < Part of the npm integration (not yet working, therefore commented out)