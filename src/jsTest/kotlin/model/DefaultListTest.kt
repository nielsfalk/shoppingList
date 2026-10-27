package model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultListTest {
    @Test
    fun addToDefaultListUpdatesBothListsAndAppliesDuplicateRules() {
        val defaultList = DefaultList(items = listOf("Water"))
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(Item(name = "Water", checked = true)),
        )

        val (updatedDefaultList, updatedDailyList) = addToDefaultList(defaultList, dailyList, " Oat milk ")

        assertEquals(listOf("Water", "Oat milk"), updatedDefaultList.items)
        assertEquals(listOf(Item(name = "Water", checked = true)), dailyList.items)
        assertEquals(listOf(Item(name = "Water", checked = true), Item(name = "Oat milk")), updatedDailyList.items)

        val (duplicateDefaultList, duplicateDailyList) = addToDefaultList(defaultList, dailyList, " water ")

        assertEquals(defaultList, duplicateDefaultList)
        assertEquals(listOf(Item(name = "Water")), duplicateDailyList.items)
        assertEquals(defaultList, defaultList.add(" "))
    }
}