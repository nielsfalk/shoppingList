package i18n

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import model.Department
import model.SuggestionOrder
import kotlin.test.assertTrue as assert

class LanguageDataTest : FreeSpec({
    @Data("languageData" , "expectedDailyList" , "expectedCopyToToday"        ) DocumentedLanguageData {
          De             ǀ "Tagesliste"        ǀ "In heutige Liste übernehmen"
          En             ǀ "Daily list"        ǀ "Copy to today"
    }.each {
        "'${languageData.ui.dailyList}' language data has valid initial and suggestion lists" {
            assertValidList(languageData.initialList)
            assertValidList(languageData.suggestions)
            val suggestionNames = languageData.suggestions.map { it.trim().lowercase() }.toSet()
            assert(languageData.initialList.all { it.trim().lowercase() in suggestionNames })
            assert(languageData.suggestionsByDepartment.keys == Department.entries.toSet())
            assert(languageData.suggestionsByDepartment.values.all { it.isNotEmpty() })
            assert(languageData.ui.dailyList == expectedDailyList)
            assert(languageData.ui.copyToToday == expectedCopyToToday)
        }
    }

    "every department has a non-blank translated name in every language" {
        assert(Language.entries.all { language ->
            language.data.ui.departmentNames.keys == Department.entries.toSet() &&
                language.data.ui.departmentNames.values.all { it.isNotBlank() } &&
                language.data.ui.otherDepartment.isNotBlank() &&
                language.data.ui.completed.isNotBlank()
        })
    }

    "the initial list of every language is sorted by the suggestion order" {
        assert(Language.entries.all { language ->
            SuggestionOrder(language.data.suggestionsByDepartment).sorted(language.data.initialList) { it } == language.data.initialList
        })
    }
})

private fun assertValidList(items: List<String>) {
    assert(items.all { it.isNotBlank() })
    assert(items.size == items.distinctBy { it.trim().lowercase() }.size)
}
