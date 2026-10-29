package storage

import kotlinx.datetime.LocalDate
import model.DailyList

fun saveDailyList(storage: Storage, dailyList: DailyList) =
    DailyListRepository(storage).save(dailyList)

fun loadDailyList(storage: Storage, date: LocalDate): DailyList? =
    DailyListRepository(storage).load(date)

fun storedDays(storage: Storage): List<LocalDate> =
    DailyListRepository(storage).storedDays()