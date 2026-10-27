package i18n

val languages = mapOf("de" to De, "en" to En)

fun selectLanguage(browserLanguage: String?): String {
    val languageCode = browserLanguage
        ?.substringBefore('-')
        ?.lowercase()
    return languageCode?.takeIf { it in languages } ?: "en"
}

fun languageData(languageCode: String): LanguageData = languages[languageCode.lowercase()] ?: En