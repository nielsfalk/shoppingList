package i18n

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class LanguageTest : FreeSpec({
    @Data("locale" ,  "expectedLanguage") LanguageSelection {
          "de"     ǀǀ Language.DE
          "de-DE"  ǀǀ Language.DE
          "en-US"  ǀǀ Language.EN
          null     ǀǀ Language.EN
          ""       ǀǀ Language.EN
          "fr-FR"  ǀǀ Language.EN
    }.each {
        "selects $expectedLanguage for the browser locale '$locale'" {
            assert(Language.select(locale) == expectedLanguage)
        }
    }

    @Data("code" ,  "expectedLanguage") LanguageLookup {
          "de"   ǀǀ Language.DE
          "DE"   ǀǀ Language.DE
          "en"   ǀǀ Language.EN
          "fr"   ǀǀ null
    }.each {
        "looks up the language for '$code'" {
            assert(Language.fromCode(code) == expectedLanguage)
        }
    }
})
