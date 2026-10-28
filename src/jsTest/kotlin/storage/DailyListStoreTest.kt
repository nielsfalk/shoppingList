package storage

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import model.DailyList
import model.Item
import kotlin.random.Random
import kotlin.test.assertTrue as assert

class DailyListStoreTest : FreeSpec({
    "saves and loads daily lists by date" {
        val storage = newTestStorage()
        val date = LocalDate.parse("2026-10-27")
        val dailyList = DailyList(date, listOf(Item("Water"), Item("Mustard", checked = true)))

        try {
            saveDailyList(storage, dailyList)

            assert(loadDailyList(storage, date) == dailyList)
            assert(loadDailyList(storage, LocalDate.parse("2026-10-26")) == null)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "returns stored days in chronological order and skips invalid keys" {
        val storage = newTestStorage()
        val dates = listOf("2026-10-27", "2026-01-09", "2025-12-31")

        try {
            dates.forEach { date -> saveDailyList(storage, DailyList(LocalDate.parse(date), emptyList())) }
            storage.setString("day:not-a-date", "invalid")
            storage.setString("defaultList", "not a daily list")

            assert(storedDays(storage) == listOf("2025-12-31", "2026-01-09", "2026-10-27").map(LocalDate::parse))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
})

private fun newTestStorage() = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
