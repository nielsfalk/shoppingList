package ui

import i18n.En
import i18n.languageData
import kotlinx.datetime.LocalDate
import model.DailyList
import model.Item
import storage.Storage
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class AppStateTest {
    @Test
    fun switchingLanguagePersistsChoiceAndLoadsItsInitialList() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val persistedLanguages = mutableListOf<String>()
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguageCode = "de", languagePersistence = {
                persistedLanguages += it
            }, todayDate = date)

            assertEquals(date, state.dailyList.date)
            assertEquals(languageData("de").initialList, state.dailyList.items.map { it.name })
            val firstItem = state.dailyList.items.first()
            state.toggleItem(firstItem.name)
            assertEquals(true, state.dailyList.items.first().checked)
            assertEquals(state.dailyList, storage.get<DailyList>("day:$date"))

            state.switchLanguage("en")

            assertEquals("en", state.languageCode)
            assertEquals(En, state.languageData)
            assertEquals(languageData("en").initialList, state.defaultList.items)
            assertEquals(firstItem.name, state.dailyList.items.first().name)
            assertEquals(true, state.dailyList.items.first().checked)

            state.deleteItem(firstItem.name)

            assertEquals(false, state.dailyList.items.any { it.name == firstItem.name })
            assertEquals(state.dailyList, storage.get<DailyList>("day:$date"))
            assertEquals(listOf("en"), persistedLanguages)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun addsItemsToDailyAndDefaultListAndPersistsBoth() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = date)

            state.addToDailyList("  Bread ")
            state.addToDefaultList("Tofu")

            assertEquals(listOf("Bread", "Tofu"), state.dailyList.items.takeLast(2).map { it.name })
            assertEquals("Tofu", state.defaultList.items.last())
            assertEquals(state.dailyList, storage.get<DailyList>("day:$date"))
            assertEquals(state.defaultList.items, storage.get<List<String>>("defaultList"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun reordersDefaultListAndRemovesStoredListWhenItMatchesInitialAgain() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val initialList = languageData("en").initialList

        try {
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = LocalDate.parse("2026-10-27"))

            state.moveDefaultItemDown(initialList[0])

            assertEquals(listOf(initialList[1], initialList[0]), state.defaultList.items.take(2))
            assertEquals(state.defaultList.items, storage.get<List<String>>("defaultList"))

            state.moveDefaultItemUp(initialList[0])

            assertEquals(initialList, state.defaultList.items)
            assertEquals(null, storage.getString("defaultList"))

            val dailyListBeforeDelete = state.dailyList
            state.deleteDefaultItem(initialList[0])

            assertEquals(initialList.drop(1), state.defaultList.items)
            assertEquals(initialList.drop(1), storage.get<List<String>>("defaultList"))
            assertEquals(dailyListBeforeDelete, state.dailyList)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun exposesStoredDaysAndListsOfPastDays() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val today = LocalDate.parse("2026-10-27")
        val pastDay = LocalDate.parse("2026-10-20")

        try {
            storage.set("day:$pastDay", DailyList(pastDay, listOf(Item("Bread", checked = true))))
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = today)

            assertEquals(setOf(pastDay, today), state.storedDays())
            assertEquals(listOf(Item("Bread", checked = true)), state.dailyListOn(pastDay)?.items)
            assertEquals(state.dailyList, state.dailyListOn(today))
            assertEquals(null, state.dailyListOn(LocalDate.parse("2026-10-21")))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun copiesPastDayToTodayUncheckedAndIgnoresTodayAndMissingDays() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val today = LocalDate.parse("2026-10-27")
        val pastDay = LocalDate.parse("2026-10-20")

        try {
            storage.set("day:$pastDay", DailyList(pastDay, listOf(Item("Bread", checked = true), Item("water"))))
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = today)
            val initialItems = state.dailyList.items

            state.copyToToday(pastDay)

            assertEquals(initialItems + Item("Bread"), state.dailyList.items)
            assertEquals(state.dailyList, storage.get<DailyList>("day:$today"))

            val afterCopy = state.dailyList
            state.copyToToday(today)
            state.copyToToday(LocalDate.parse("2026-10-21"))

            assertEquals(afterCopy, state.dailyList)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
}
