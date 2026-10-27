package storage

import kotlinx.datetime.LocalDate
import model.DailyList

private const val DAILY_LIST_KEY_PREFIX = "day:"

fun saveDailyList(storage: Storage, dailyList: DailyList) {
    storage.set("$DAILY_LIST_KEY_PREFIX${dailyList.date}", dailyList)
}

fun loadDailyList(storage: Storage, date: LocalDate): DailyList? =
    storage.get("$DAILY_LIST_KEY_PREFIX$date")

fun storedDays(storage: Storage): List<LocalDate> = storage.keys()
    .asSequence()
    .filter { it.startsWith(DAILY_LIST_KEY_PREFIX) }
    .mapNotNull { key ->
        runCatching { LocalDate.parse(key.removePrefix(DAILY_LIST_KEY_PREFIX)) }.getOrNull()
    }
    .sorted()
    .toList()