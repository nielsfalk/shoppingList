package i18n

import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class LanguageTest : FreeSpec({
    "selects a supported language from the browser locale or falls back to English" {
        assert(selectLanguage("de") == "de")
        assert(selectLanguage("de-DE") == "de")
        assert(selectLanguage("en-US") == "en")
        assert(selectLanguage(null) == "en")
        assert(selectLanguage("") == "en")
        assert(selectLanguage("fr-FR") == "en")
    }

    "looks up language data and falls back to English for unknown codes" {
        assert(languageData("de") === De)
        assert(languageData("DE") === De)
        assert(languageData("en") === En)
        assert(languageData("fr") === En)
    }
})
