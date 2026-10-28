package model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class SuggestionsTest {
    @Test
    fun suggestsDefaultItemsNotInTheDailyListWithoutDuplicateLanguageSuggestions() {
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(Item("Bread"), Item(" water "), Item("Bread", checked = true)),
        )
        val defaultList = DefaultList(listOf("Bread", "Water", "Tofu", "Oat milk"))

        val suggestions = suggestionsFor(
            dailyList,
            defaultList,
            listOf("Tofu", "oat milk", "Apples", "Apples"),
        )

        assertEquals(listOf("Tofu", "Oat milk", "Apples"), suggestions)
    }
}