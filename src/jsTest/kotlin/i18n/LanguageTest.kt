package i18n

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class LanguageTest {
    @Test
    fun selectsSupportedLanguageFromBrowserLocaleOrFallsBackToEnglish() {
        assertEquals("de", selectLanguage("de"))
        assertEquals("de", selectLanguage("de-DE"))
        assertEquals("en", selectLanguage("en-US"))
        assertEquals("en", selectLanguage(null))
        assertEquals("en", selectLanguage(""))
        assertEquals("en", selectLanguage("fr-FR"))
    }

    @Test
    fun looksUpLanguageDataAndFallsBackToEnglishForUnknownCodes() {
        assertSame(De, languageData("de"))
        assertSame(De, languageData("DE"))
        assertSame(En, languageData("en"))
        assertSame(En, languageData("fr"))
    }
}