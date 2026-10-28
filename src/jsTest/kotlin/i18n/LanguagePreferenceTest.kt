package i18n

import io.kotest.core.spec.style.FreeSpec
import kotlinx.browser.localStorage
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue as assert

class LanguagePreferenceTest : FreeSpec({
    "prefers a supported stored language over the browser locale" {
        assert(selectLanguage("de", "en-US") == "de")
        assert(selectLanguage("DE", "en-US") == "de")
        assert(selectLanguage("fr", "de-DE") == "de")
        assert(selectLanguage(null, null) == "en")
    }

    "stores and reads a manual language preference" {
        val previousValue = localStorage.getItem("shoppingList:language")

        try {
            localStorage.removeItem("shoppingList:language")
            assert(storedLanguage() == null)

            storeLanguage("de")
            assert(storedLanguage() == "de")
            assert(currentLanguage() == "de")

            storeLanguage("en")
            assert(currentLanguage() == "en")
            assertFailsWith<IllegalArgumentException> { storeLanguage("fr") }
            assert(storedLanguage() == "en")
        } finally {
            if (previousValue == null) {
                localStorage.removeItem("shoppingList:language")
            } else {
                localStorage.setItem("shoppingList:language", previousValue)
            }
        }
    }
})
