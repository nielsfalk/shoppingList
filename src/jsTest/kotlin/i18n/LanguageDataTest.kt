package i18n

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LanguageDataTest {
    @Test
    fun germanLanguageDataMatchesTheDocumentedInitialListAndHasValidLists() {
        assertEquals(
            listOf("Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken"),
            De.initialList,
        )
        assertValidList(De.initialList)
        assertValidList(De.suggestions)
        assertEquals("Tagesliste", De.ui.dailyList)
        assertEquals("In heutige Liste übernehmen", De.ui.copyToToday)
    }

    private fun assertValidList(items: List<String>) {
        assertTrue(items.all { it.isNotBlank() })
        assertEquals(items.size, items.distinctBy { it.trim().lowercase() }.size)
    }
}