package storage

import i18n.Language
import kotlinx.browser.window

private const val LANGUAGE_STORAGE_KEY = "language"

class LanguageRepository(private val storage: Storage = Storage()) {
    fun storedLanguage(): String? = storage.getString(LANGUAGE_STORAGE_KEY)

    fun storeLanguage(language: Language) {
        storage.setString(LANGUAGE_STORAGE_KEY, language.code)
    }

    fun currentLanguage(): Language = Language.select(storedLanguage(), window.navigator.language)
}
