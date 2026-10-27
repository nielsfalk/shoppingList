package storage

import kotlinx.datetime.LocalDate
import model.DailyList
import model.Item
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DailyListStoreTest {
    @Test
    fun savesAndLoadsDailyListsByDate() {
        val storage = newTestStorage()
        val date = LocalDate.parse("2026-10-27")
        val dailyList = DailyList(date, listOf(Item("Water"), Item("Mustard", checked = true)))

        try {
            saveDailyList(storage, dailyList)

            assertEquals(dailyList, loadDailyList(storage, date))
            assertNull(loadDailyList(storage, LocalDate.parse("2026-10-26")))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun returnsStoredDaysInChronologicalOrderAndSkipsInvalidKeys() {
        val storage = newTestStorage()
        val dates = listOf("2026-10-27", "2026-01-09", "2025-12-31")

        try {
            dates.forEach { date -> saveDailyList(storage, DailyList(LocalDate.parse(date), emptyList())) }
            storage.setString("day:not-a-date", "invalid")
            storage.setString("defaultList", "not a daily list")

            assertEquals(
                listOf("2025-12-31", "2026-01-09", "2026-10-27").map(LocalDate::parse),
                storedDays(storage),
            )
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    private fun newTestStorage() = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
}