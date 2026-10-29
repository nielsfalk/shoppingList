package storage

import kotlinx.datetime.LocalDate
import model.DailyList
import model.DefaultList
import model.dailyListFor

private const val DAILY_LIST_KEY_PREFIX = "day:"

class DailyListRepository(private val storage: Storage) {
    fun save(dailyList: DailyList) {
        storage.set("$DAILY_LIST_KEY_PREFIX${dailyList.date}", dailyList)
    }

    fun load(date: LocalDate): DailyList? =
        storage.get("$DAILY_LIST_KEY_PREFIX$date")

    fun storedDays(): List<LocalDate> = storage.keys()
        .asSequence()
        .filter { it.startsWith(DAILY_LIST_KEY_PREFIX) }
        .mapNotNull { key ->
            runCatching { LocalDate.parse(key.removePrefix(DAILY_LIST_KEY_PREFIX)) }.getOrNull()
        }
        .sorted()
        .toList()

    fun loadOrCreate(date: LocalDate, defaultList: DefaultList): DailyList {
        val existing = load(date)
        val dailyList = dailyListFor(date, existing, defaultList)
        if (existing?.date != date) {
            save(dailyList)
        }
        return dailyList
    }
}
