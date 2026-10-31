package org.vaadin.hugerte;

import java.util.Objects;

/**
 * This enumeration represents all available languages for the HugeRTE.
 */
public enum Language implements ClientSideReference {
    /**
     * Default language. Has no code, since there is no lang file for it, and is optional to be used. So we provide it
     * for the sake of completeness.
     * <p>
     * You can also set "null" on {@link HugeRte#configureLanguage(Language)}
     * </p>
     */
    ENGLISH(null),

    // Languages
    /** Arabic, {@code ar}. */
    ARABIC("ar"),
    /** Azerbaijani, {@code az}. */
    AZERBAIJANI("az"),
    /** Belarusian, {@code be}. */
    BELARUSIAN("be"),
    /** Catalan, {@code ca}. */
    CATALAN("ca"),
    /** Czech, {@code cs}. */
    CZECH("cs"),
    /** Welsh, {@code cy}. */
    WELSH("cy"),
    /** Danish, {@code da}. */
    DANISH("da"),
    /** German, {@code de}. */
    GERMAN("de"),
    /** Greek, {@code el}. */
    GREEK("el"),
    /** Esperanto, {@code eo}. */
    ESPERANTO("eo"),
    /** Spanish, {@code es}. */
    SPANISH("es"),
    /** Estonian, {@code et}. */
    ESTONIAN("et"),
    /** Basque, {@code eu}. */
    BASQUE("eu"),
    /** Persian, {@code fa}. */
    PERSIAN("fa"),
    /** Finnish, {@code fi}. */
    FINNISH("fi"),
    /** Irish, {@code ga}. */
    IRISH("ga"),
    /** Galician, {@code gl}. */
    GALICIAN("gl"),
    /** Croatian, {@code hr}. */
    CROATIAN("hr"),
    /** Hungarian, {@code hu_HU}. */
    HUNGARIAN("hu_HU"), // No generic Hungarian code provided, using hu_HU
    /** Armenian, {@code hy}. */
    ARMENIAN("hy"),
    /** Indonesian, {@code id}. */
    INDONESIAN("id"),
    /** Italian, {@code it}. */
    ITALIAN("it"),
    /** Japanese, {@code ja}. */
    JAPANESE("ja"),
    /** Kabyle, {@code kab}. */
    KABYLE("kab"),
    /** Kazakh, {@code kk}. */
    KAZAKH("kk"),
    /** Kurdish, {@code ku}. */
    KURDISH("ku"),
    /** Lithuanian, {@code lt}. */
    LITHUANIAN("lt"),
    /** Latvian, {@code lv}. */
    LATVIAN("lv"),
    /** Nepali, {@code ne}. */
    NEPALI("ne"),
    /** Dutch, {@code nl}. */
    DUTCH("nl"),
    /** Occitan, {@code oc}. */
    OCCITAN("oc"),
    /** Polish, {@code pl}. */
    POLISH("pl"),
    /** Portuguese, {@code pt_PT}. */
    PORTUGUESE("pt_PT"), // Using pt_PT as generic, as pt_BR is also present
    /** Romanian, {@code ro}. */
    ROMANIAN("ro"),
    /** Russian, {@code ru}. */
    RUSSIAN("ru"),
    /** Slovak, {@code sk}. */
    SLOVAK("sk"),
    /** Albanian, {@code sq}. */
    ALBANIAN("sq"),
    /** Serbian, {@code sr}. */
    SERBIAN("sr"),
    /** Tajik, {@code tg}. */
    TAJIK("tg"),
    /** Turkish, {@code tr}. */
    TURKISH("tr"),
    /** Uighur, {@code ug}. */
    UIGHUR("ug"),
    /** Ukrainian, {@code uk}. */
    UKRAINIAN("uk"),
    /** Uzbek, {@code uz}. */
    UZBEK("uz"),
    /** Vietnamese, {@code vi}. */
    VIETNAMESE("vi"),
    /** Tamil, {@code ta}. */
    TAMIL("ta"), // No region specified, treating as generic language

    // Regional variations
    /** Arabic (Saudi Arabia), {@code ar_SA}. */
    ARABIC_SAUDI_ARABIA("ar_SA"),
    /** Bulgarian (Bulgaria), {@code bg_BG}. */
    BULGARIAN_BULGARIA("bg_BG"),
    /** Bengali (Bangladesh), {@code bn_BD}. */
    BENGALI_BANGLADESH("bn_BD"),
    /** Spanish (Mexico), {@code es_MX}. */
    SPANISH_MEXICO("es_MX"),
    /** French (France), {@code fr_FR}. */
    FRENCH_FRANCE("fr_FR"),
    /** Hebrew (Israel), {@code he_IL}. */
    HEBREW_ISRAEL("he_IL"),
    /** Icelandic (Iceland), {@code is_IS}. */
    ICELANDIC_ICELAND("is_IS"),
    /** Georgian (Georgia), {@code ka_GE}. */
    GEORGIAN_GEORGIA("ka_GE"),
    /** Korean (Korea), {@code ko_KR}. */
    KOREAN_KOREA("ko_KR"),
    /** Norwegian (Norway), {@code nb_NO}. */
    NORWEGIAN_NORWAY("nb_NO"),
    /** Dutch (Belgium), {@code nl_BE}. */
    DUTCH_BELGIUM("nl_BE"),
    /** Portuguese (Brazil), {@code pt_BR}. */
    PORTUGUESE_BRAZIL("pt_BR"),
    /** Slovenian (Slovenia), {@code sl_SI}. */
    SLOVENIAN_SLOVENIA("sl_SI"),
    /** Swedish (Sweden), {@code sv_SE}. */
    SWEDISH_SWEDEN("sv_SE"),
    /** Thai (Thailand), {@code th_TH}. */
    THAI_THAILAND("th_TH"),
    /** Chinese (China), {@code zh_CN}. */
    CHINESE_CHINA("zh_CN"),
    /** Chinese (Hong Kong), {@code zh_HK}. */
    CHINESE_HONG_KONG("zh_HK"),
    /** Chinese (Macao), {@code zh_MO}. */
    CHINESE_MACAO("zh_MO"),
    /** Chinese (Singapore), {@code zh_SG}. */
    CHINESE_SINGAPORE("zh_SG"),
    /** Chinese (Taiwan), {@code zh_TW}. */
    CHINESE_TAIWAN("zh_TW");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    /**
     * Returns the language or country code represented by this instance.
     * Will be {@code null} for {@link #ENGLISH}.
     * @return code
     */
    public String getCode() {
        return code;
    }

    @Override
    public String getClientSideRepresentation() {
        return getCode();
    }

    /**
     * Returns the Language enum member corresponding to the given code.
     *
     * @param code The language or country code string (e.g., "de", "fr_FR").
     * @return The Language enum member.
     * @throws IllegalArgumentException If the code is not found.
     */
    public static Language fromCode(String code) {
        for (Language lang : Language.values()) {
            if (Objects.equals(lang.code, code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("No Language enum member found for code: " + code);
    }
}