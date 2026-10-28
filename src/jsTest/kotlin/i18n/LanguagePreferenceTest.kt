package i18n

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlinx.browser.localStorage
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue as assert

class LanguagePreferenceTest : FreeSpec({
    @Data("storedLanguage" , "browserLocale" ,  "expectedLanguage") LanguagePreference {
          "de"             ǀ "en-US"         ǀǀ "de"
          "DE"             ǀ "en-US"         ǀǀ "de"
          "fr"             ǀ "de-DE"         ǀǀ "de"
          null             ǀ null            ǀǀ "en"
    }.each {
        "selects $expectedLanguage for the stored language '$storedLanguage' and the browser locale '$browserLocale'" {
            assert(selectLanguage(storedLanguage, browserLocale) == expectedLanguage)
        }
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
