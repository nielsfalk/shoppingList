package model

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.assertTrue as assert

class DailyListTest : FreeSpec({
    "sorts unchecked items first without changing their relative order" {
        val checkedFirst = Item(name = "Checked first", checked = true)
        val uncheckedFirst = Item(name = "Unchecked first")
        val checkedSecond = Item(name = "Checked second", checked = true)
        val uncheckedSecond = Item(name = "Unchecked second")
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(checkedFirst, uncheckedFirst, checkedSecond, uncheckedSecond),
        )

        val sortedItems = dailyList.sortedItems()

        assert(sortedItems == listOf(uncheckedFirst, uncheckedSecond, checkedFirst, checkedSecond))
        assert(dailyList.items == listOf(checkedFirst, uncheckedFirst, checkedSecond, uncheckedSecond))
    }

    "toggles and deletes items by trimmed case-insensitive name" {
        val water = Item(name = "Water")
        val milk = Item(name = "Oat milk", checked = true)
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(water, milk),
        )

        assert(dailyList.toggle("  wAtEr ").items == listOf(water.copy(checked = true), milk))
        assert(dailyList.delete(" OAT MILK ").items == listOf(water))
        assert(dailyList.toggle("Missing") == dailyList)
        assert(dailyList.delete("Missing") == dailyList)
    }

    "adds items with the duplicate rule" {
        val checkedWater = Item(name = "Water", checked = true)
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(checkedWater),
        )

        val uncheckedDuplicate = dailyList.add(" WATER ")

        assert(uncheckedDuplicate.items == listOf(checkedWater.copy(checked = false)))
        assert(dailyList.items == listOf(checkedWater))
        assert(uncheckedDuplicate.add("water") == uncheckedDuplicate)
        assert(dailyList.add(" Oat milk ").items == listOf(checkedWater, Item(name = "Oat milk")))
        assert(dailyList.add("  ") == dailyList)
    }

    "creates today's list from the default list when no list exists" {
        val today = LocalDate.parse("2026-10-27")
        val defaultList = DefaultList(items = listOf("Water", "Oat milk"))
        val expected = DailyList(
            date = today,
            items = listOf(Item(name = "Water"), Item(name = "Oat milk")),
        )
        val existing = DailyList(today, listOf(Item(name = "Existing", checked = true)))
        val anotherDay = DailyList(LocalDate.parse("2026-10-26"), listOf(Item(name = "Yesterday")))

        assert(dailyListFor(today, null, defaultList) == expected)
        assert(dailyListFor(today, existing, defaultList) == existing)
        assert(dailyListFor(today, anotherDay, defaultList) == expected)
        assert(defaultList.items == listOf("Water", "Oat milk"))
    }

    "copies old items to today unchecked using the duplicate rule" {
        val today = LocalDate.parse("2026-10-27")
        val current = DailyList(
            today,
            listOf(Item(name = "Water", checked = true), Item(name = "Bread")),
        )
        val old = DailyList(
            LocalDate.parse("2026-10-26"),
            listOf(
                Item(name = " water ", checked = true),
                Item(name = "Bread", checked = true),
                Item(name = "Oat milk", checked = true),
            ),
        )

        val copied = current.copyFrom(old)

        assert(copied == DailyList(today, listOf(Item(name = "Water"), Item(name = "Bread"), Item(name = "Oat milk"))))
        assert(current.items.first() == Item(name = "Water", checked = true))
        assert(old.items.last() == Item(name = "Oat milk", checked = true))
    }

    "serializes to the stored JSON shape" {
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(
                Item(name = "Wasser"),
                Item(name = "Senf", checked = true),
            ),
        )
        val expectedJson = """{"date":"2026-10-27","items":[{"name":"Wasser","checked":false},{"name":"Senf","checked":true}]}"""
        val json = Json { encodeDefaults = true }

        assert(json.encodeToString(dailyList) == expectedJson)
        assert(json.decodeFromString<DailyList>(expectedJson) == dailyList)
    }
})
