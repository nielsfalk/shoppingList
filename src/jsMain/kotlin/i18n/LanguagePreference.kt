package i18n

import kotlinx.browser.localStorage
import kotlinx.browser.window

private const val LANGUAGE_STORAGE_KEY = "shoppingList:language"

fun storedLanguage(): String? = localStorage.getItem(LANGUAGE_STORAGE_KEY)

fun storeLanguage(languageCode: String) {
    require(languageCode in languages) { "Unsupported language: $languageCode" }
    localStorage.setItem(LANGUAGE_STORAGE_KEY, languageCode)
}

fun currentLanguage(): String = selectLanguage(storedLanguage(), window.navigator.language)