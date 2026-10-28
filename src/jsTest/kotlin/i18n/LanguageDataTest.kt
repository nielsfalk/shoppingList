package i18n

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class LanguageDataTest : FreeSpec({
    @Data("languageData" ,  "expectedInitialList"                                                           , "expectedDailyList" , "expectedCopyToToday"        ) DocumentedLanguageData {
          De             ǀǀ listOf("Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken")    ǀ "Tagesliste"        ǀ "In heutige Liste übernehmen"
          En             ǀǀ listOf("Toilet paper", "Water", "Mustard", "Oat milk", "Apples", "Rolled oats") ǀ "Daily list"        ǀ "Copy to today"
    }.each {
        "'${languageData.ui.dailyList}' language data matches the documented initial list and has valid lists" {
            assert(languageData.initialList == expectedInitialList)
            assertValidList(languageData.initialList)
            assertValidList(languageData.suggestions)
            assert(languageData.ui.dailyList == expectedDailyList)
            assert(languageData.ui.copyToToday == expectedCopyToToday)
        }
    }
})

private fun assertValidList(items: List<String>) {
    assert(items.all { it.isNotBlank() })
    assert(items.size == items.distinctBy { it.trim().lowercase() }.size)
}
