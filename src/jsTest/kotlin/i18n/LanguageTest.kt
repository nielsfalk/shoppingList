package i18n

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class LanguageTest : FreeSpec({
    @Data("locale" ,  "expectedLanguage") LanguageSelection {
          "de"     ǀǀ "de"
          "de-DE"  ǀǀ "de"
          "en-US"  ǀǀ "en"
          null     ǀǀ "en"
          ""       ǀǀ "en"
          "fr-FR"  ǀǀ "en"
    }.each {
        "selects $expectedLanguage for the browser locale '$locale'" {
            assert(selectLanguage(locale) == expectedLanguage)
        }
    }

    @Data("code" ,  "expectedLanguageData") LanguageDataLookup {
          "de"   ǀǀ De
          "DE"   ǀǀ De
          "en"   ǀǀ En
          "fr"   ǀǀ En
    }.each {
        "looks up the language data for '$code'" {
            assert(languageData(code) === expectedLanguageData)
        }
    }
})
