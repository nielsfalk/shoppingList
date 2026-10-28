package i18n

import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class LanguageDataTest : FreeSpec({
    "German language data matches the documented initial list and has valid lists" {
        assert(De.initialList == listOf("Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken"))
        assertValidList(De.initialList)
        assertValidList(De.suggestions)
        assert(De.ui.dailyList == "Tagesliste")
        assert(De.ui.copyToToday == "In heutige Liste übernehmen")
    }

    "English language data matches the documented initial list and has valid lists" {
        assert(En.initialList == listOf("Toilet paper", "Water", "Mustard", "Oat milk", "Apples", "Rolled oats"))
        assertValidList(En.initialList)
        assertValidList(En.suggestions)
        assert(En.ui.dailyList == "Daily list")
        assert(En.ui.copyToToday == "Copy to today")
    }
})

private fun assertValidList(items: List<String>) {
    assert(items.all { it.isNotBlank() })
    assert(items.size == items.distinctBy { it.trim().lowercase() }.size)
}
