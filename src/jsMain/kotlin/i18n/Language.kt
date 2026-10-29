package i18n

enum class Language(val code: String, val data: LanguageData) {
    DE("de", De),
    EN("en", En);

    companion object {
        fun fromCode(code: String?): Language? =
            entries.firstOrNull { it.code == code?.lowercase() }

        fun select(browserLanguage: String?): Language {
            val code = browserLanguage?.substringBefore('-')
            return fromCode(code) ?: EN
        }

        fun select(storedLanguage: String?, browserLanguage: String?): Language =
            fromCode(storedLanguage) ?: select(browserLanguage)
    }
}
