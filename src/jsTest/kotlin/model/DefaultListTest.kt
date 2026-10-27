package model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultListTest {
    @Test
    fun movesItemsUpAndDownWithoutCrossingListBoundaries() {
        val defaultList = DefaultList(items = listOf("Bread", "Water", "Apples"))

        assertEquals(listOf("Water", "Bread", "Apples"), defaultList.moveUp(" water ").items)
        assertEquals(listOf("Bread", "Apples", "Water"), defaultList.moveDown("WATER").items)
        assertEquals(defaultList, defaultList.moveUp("Bread"))
        assertEquals(defaultList, defaultList.moveDown("Apples"))
        assertEquals(defaultList, defaultList.moveUp("Missing"))
    }

    @Test
    fun deletesAnItemByTrimmedCaseInsensitiveName() {
        val defaultList = DefaultList(items = listOf("Bread", "Water", "Apples"))

        assertEquals(listOf("Bread", "Apples"), defaultList.delete(" water ").items)
        assertEquals(defaultList, defaultList.delete("Missing"))
    }

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