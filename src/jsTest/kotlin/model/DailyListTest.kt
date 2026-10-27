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