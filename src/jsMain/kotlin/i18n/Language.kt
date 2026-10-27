package i18n

val languages = mapOf("de" to De, "en" to En)

fun selectLanguage(browserLanguage: String?): String {
    val languageCode = browserLanguage
        ?.substringBefore('-')
        ?.lowercase()
    return languageCode?.takeIf { it in languages } ?: "en"
}

fun selectLanguage(storedLanguage: String?, browserLanguage: String?): String {
    val storedLanguageCode = storedLanguage?.lowercase()?.takeIf { it in languages }
    return storedLanguageCode ?: selectLanguage(browserLanguage)
}

fun languageData(languageCode: String): LanguageData = languages[languageCode.lowercase()] ?: En