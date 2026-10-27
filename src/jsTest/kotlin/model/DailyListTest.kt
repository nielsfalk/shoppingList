package model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class DailyListTest {
    @Test
    fun sortsUncheckedItemsFirstWithoutChangingTheirRelativeOrder() {
        val checkedFirst = Item(name = "Checked first", checked = true)
        val uncheckedFirst = Item(name = "Unchecked first")
        val checkedSecond = Item(name = "Checked second", checked = true)
        val uncheckedSecond = Item(name = "Unchecked second")
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(checkedFirst, uncheckedFirst, checkedSecond, uncheckedSecond),
        )

        assertEquals(
            listOf(uncheckedFirst, uncheckedSecond, checkedFirst, checkedSecond),
            dailyList.sortedItems(),
        )
        assertEquals(listOf(checkedFirst, uncheckedFirst, checkedSecond, uncheckedSecond), dailyList.items)
    }

    @Test
    fun togglesAndDeletesItemsByTrimmedCaseInsensitiveName() {
        val water = Item(name = "Water")
        val milk = Item(name = "Oat milk", checked = true)
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(water, milk),
        )

        assertEquals(listOf(water.copy(checked = true), milk), dailyList.toggle("  wAtEr ").items)
        assertEquals(listOf(water), dailyList.delete(" OAT MILK ").items)
        assertEquals(dailyList, dailyList.toggle("Missing"))
        assertEquals(dailyList, dailyList.delete("Missing"))
    }

    @Test
    fun addsItemsWithTheDuplicateRule() {
        val checkedWater = Item(name = "Water", checked = true)
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(checkedWater),
        )

        val uncheckedDuplicate = dailyList.add(" WATER ")

        assertEquals(listOf(checkedWater.copy(checked = false)), uncheckedDuplicate.items)
        assertEquals(listOf(checkedWater), dailyList.items)
        assertEquals(uncheckedDuplicate, uncheckedDuplicate.add("water"))
        assertEquals(listOf(checkedWater, Item(name = "Oat milk")), dailyList.add(" Oat milk ").items)
        assertEquals(dailyList, dailyList.add("  "))
    }

    @Test
    fun createsTodaysListFromTheDefaultWhenNoListExists() {
        val today = LocalDate.parse("2026-10-27")
        val defaultList = DefaultList(items = listOf("Water", "Oat milk"))
        val expected = DailyList(
            date = today,
            items = listOf(Item(name = "Water"), Item(name = "Oat milk")),
        )
        val existing = DailyList(today, listOf(Item(name = "Existing", checked = true)))
        val anotherDay = DailyList(LocalDate.parse("2026-10-26"), listOf(Item(name = "Yesterday")))

        assertEquals(expected, dailyListFor(today, null, defaultList))
        assertEquals(existing, dailyListFor(today, existing, defaultList))
        assertEquals(expected, dailyListFor(today, anotherDay, defaultList))
        assertEquals(listOf("Water", "Oat milk"), defaultList.items)
    }

    @Test
    fun serializesToTheStoredJsonShape() {
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(
                Item(name = "Wasser"),
                Item(name = "Senf", checked = true),
            ),
        )
        val expectedJson = """{"date":"2026-10-27","items":[{"name":"Wasser","checked":false},{"name":"Senf","checked":true}]}"""
        val json = Json { encodeDefaults = true }

        assertEquals(expectedJson, json.encodeToString(dailyList))
        assertEquals(dailyList, json.decodeFromString(expectedJson))
    }
}