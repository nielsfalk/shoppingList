package i18n

import kotlinx.browser.localStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class LanguagePreferenceTest {
    @Test
    fun prefersSupportedStoredLanguageOverBrowserLocale() {
        assertEquals("de", selectLanguage("de", "en-US"))
        assertEquals("de", selectLanguage("DE", "en-US"))
        assertEquals("de", selectLanguage("fr", "de-DE"))
        assertEquals("en", selectLanguage(null, null))
    }

    @Test
    fun storesAndReadsManualLanguagePreference() {
        val previousValue = localStorage.getItem("shoppingList:language")

        try {
            localStorage.removeItem("shoppingList:language")
            assertNull(storedLanguage())

            storeLanguage("de")
            assertEquals("de", storedLanguage())
            assertEquals("de", currentLanguage())

            storeLanguage("en")
            assertEquals("en", currentLanguage())
            assertFailsWith<IllegalArgumentException> { storeLanguage("fr") }
            assertEquals("en", storedLanguage())
        } finally {
            if (previousValue == null) {
                localStorage.removeItem("shoppingList:language")
            } else {
                localStorage.setItem("shoppingList:language", previousValue)
            }
        }
    }
}