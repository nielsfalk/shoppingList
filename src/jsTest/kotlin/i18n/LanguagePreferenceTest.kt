package i18n

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlinx.browser.localStorage
import kotlin.test.assertTrue as assert

class LanguagePreferenceTest : FreeSpec({
    @Data("storedLanguage" , "browserLocale" ,  "expectedLanguage") LanguagePreference {
          "de"             ǀ "en-US"         ǀǀ Language.DE
          "DE"             ǀ "en-US"         ǀǀ Language.DE
          "fr"             ǀ "de-DE"         ǀǀ Language.DE
          null             ǀ null            ǀǀ Language.EN
    }.each {
        "selects $expectedLanguage for the stored language '$storedLanguage' and the browser locale '$browserLocale'" {
            assert(Language.select(storedLanguage, browserLocale) == expectedLanguage)
        }
    }

    "stores and reads a manual language preference" {
        val previousValue = localStorage.getItem("shoppingList:language")

        try {
            localStorage.removeItem("shoppingList:language")
            assert(storedLanguage() == null)

            storeLanguage(Language.DE)
            assert(storedLanguage() == "de")
            assert(currentLanguage() == Language.DE)

            storeLanguage(Language.EN)
            assert(currentLanguage() == Language.EN)
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
