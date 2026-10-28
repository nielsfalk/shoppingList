package model

import de.nielsfalk.dataTables.Data
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

    @Data("items"                                        ,  "expectedDifference") DifferenceFromInitialList {
          listOf("Water", "Oat milk", "Apples")          ǀǀ false
          listOf("Water", "Oat milk", "Apples", "Bread") ǀǀ true
          listOf("Water", "Oat milk")                    ǀǀ true
          listOf("Apples", "Oat milk", "Water")          ǀǀ true
    }.each {
        "the default list $items differs from the initial list: $expectedDifference" {
            val initialList = listOf("Water", "Oat milk", "Apples")

            assert(DefaultList(items).differsFrom(initialList) == expectedDifference)
        }
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
