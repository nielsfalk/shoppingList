package model

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlin.test.assertTrue as assert

class DefaultListTest : FreeSpec({
    "moves items up and down without crossing list boundaries" {
        val defaultList = DefaultList(items = listOf("Bread", "Water", "Apples"))

        assert(defaultList.moveUp(" water ").items == listOf("Water", "Bread", "Apples"))
        assert(defaultList.moveDown("WATER").items == listOf("Bread", "Apples", "Water"))
        assert(defaultList.moveUp("Bread") == defaultList)
        assert(defaultList.moveDown("Apples") == defaultList)
        assert(defaultList.moveUp("Missing") == defaultList)
    }

    "deletes an item by trimmed case-insensitive name" {
        val defaultList = DefaultList(items = listOf("Bread", "Water", "Apples"))

        assert(defaultList.delete(" water ").items == listOf("Bread", "Apples"))
        assert(defaultList.delete("Missing") == defaultList)
    }

    "detects differences from the initial list" {
        val initialList = listOf("Water", "Oat milk", "Apples")

        assert(!DefaultList(initialList).differsFrom(initialList))
        assert(DefaultList(initialList + "Bread").differsFrom(initialList))
        assert(DefaultList(initialList.dropLast(1)).differsFrom(initialList))
        assert(DefaultList(initialList.reversed()).differsFrom(initialList))
    }

    "add to default list updates both lists and applies duplicate rules" {
        val defaultList = DefaultList(items = listOf("Water"))
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(Item(name = "Water", checked = true)),
        )

        val (updatedDefaultList, updatedDailyList) = addToDefaultList(defaultList, dailyList, " Oat milk ")

        assert(updatedDefaultList.items == listOf("Water", "Oat milk"))
        assert(dailyList.items == listOf(Item(name = "Water", checked = true)))
        assert(updatedDailyList.items == listOf(Item(name = "Water", checked = true), Item(name = "Oat milk")))

        val (duplicateDefaultList, duplicateDailyList) = addToDefaultList(defaultList, dailyList, " water ")

        assert(duplicateDefaultList == defaultList)
        assert(duplicateDailyList.items == listOf(Item(name = "Water")))
        assert(defaultList.add(" ") == defaultList)
    }
})
