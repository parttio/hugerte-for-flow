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
    ARABIC("ar"),
    AZERBAIJANI("az"),
    BELARUSIAN("be"),
    CATALAN("ca"),
    CZECH("cs"),
    WELSH("cy"),
    DANISH("da"),
    GERMAN("de"),
    GREEK("el"),
    ESPERANTO("eo"),
    SPANISH("es"),
    ESTONIAN("et"),
    BASQUE("eu"),
    PERSIAN("fa"),
    FINNISH("fi"),
    IRISH("ga"),
    GALICIAN("gl"),
    CROATIAN("hr"),
    HUNGARIAN("hu_HU"), // No generic Hungarian code provided, using hu_HU
    ARMENIAN("hy"),
    INDONESIAN("id"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KABYLE("kab"),
    KAZAKH("kk"),
    KURDISH("ku"),
    LITHUANIAN("lt"),
    LATVIAN("lv"),
    NEPALI("ne"),
    DUTCH("nl"),
    OCCITAN("oc"),
    POLISH("pl"),
    PORTUGUESE("pt_PT"), // Using pt_PT as generic, as pt_BR is also present
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SLOVAK("sk"),
    ALBANIAN("sq"),
    SERBIAN("sr"),
    TAJIK("tg"),
    TURKISH("tr"),
    UIGHUR("ug"),
    UKRAINIAN("uk"),
    UZBEK("uz"),
    VIETNAMESE("vi"),
    TAMIL("ta"), // No region specified, treating as generic language

    // Regional variations
    ARABIC_SAUDI_ARABIA("ar_SA"),
    BULGARIAN_BULGARIA("bg_BG"),
    BENGALI_BANGLADESH("bn_BD"),
    SPANISH_MEXICO("es_MX"),
    FRENCH_FRANCE("fr_FR"),
    HEBREW_ISRAEL("he_IL"),
    ICELANDIC_ICELAND("is_IS"),
    GEORGIAN_GEORGIA("ka_GE"),
    KOREAN_KOREA("ko_KR"),
    NORWEGIAN_NORWAY("nb_NO"),
    DUTCH_BELGIUM("nl_BE"),
    PORTUGUESE_BRAZIL("pt_BR"),
    SLOVENIAN_SLOVENIA("sl_SI"),
    SWEDISH_SWEDEN("sv_SE"),
    THAI_THAILAND("th_TH"),
    CHINESE_CHINA("zh_CN"),
    CHINESE_HONG_KONG("zh_HK"),
    CHINESE_MACAO("zh_MO"),
    CHINESE_SINGAPORE("zh_SG"),
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