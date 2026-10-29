package i18n

import kotlinx.browser.localStorage
import kotlinx.browser.window

private const val LANGUAGE_STORAGE_KEY = "shoppingList:language"

fun storedLanguage(): String? = localStorage.getItem(LANGUAGE_STORAGE_KEY)

fun storeLanguage(language: Language) {
    localStorage.setItem(LANGUAGE_STORAGE_KEY, language.code)
}

fun currentLanguage(): Language = Language.select(storedLanguage(), window.navigator.language)
