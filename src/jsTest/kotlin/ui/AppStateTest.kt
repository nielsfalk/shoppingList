package ui

import i18n.En
import i18n.Language
import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import model.DailyList
import model.Item
import storage.Storage
import kotlin.random.Random
import kotlin.test.assertTrue as assert

class AppStateTest : FreeSpec({
    "switching the language persists the choice and loads its initial list" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val persistedLanguages = mutableListOf<Language>()
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguage = Language.DE, languagePersistence = {
                persistedLanguages += it
            }, todayDate = date)

            assert(state.dailyList.date == date)
            assert(state.dailyList.items.map { it.name } == Language.DE.data.initialList)
            val firstItem = state.dailyList.items.first()
            state.toggleItem(firstItem.name)
            assert(state.dailyList.items.first().checked)
            assert(storage.get<DailyList>("day:$date") == state.dailyList)

            state.switchLanguage(Language.EN)

            assert(state.languageCode == "en")
            assert(state.languageData == En)
            assert(state.defaultList.items == Language.EN.data.initialList)
            assert(state.dailyList.items.first().name == firstItem.name)
            assert(state.dailyList.items.first().checked)

            state.deleteItem(firstItem.name)

            assert(!state.dailyList.items.any { it.name == firstItem.name })
            assert(storage.get<DailyList>("day:$date") == state.dailyList)
            assert(persistedLanguages == listOf(Language.EN))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "adds items to the daily and default list and persists both" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = date)

            state.addToDailyList("  New daily item ")
            state.addToDefaultList("New default item")

            assert(state.dailyList.items.takeLast(2).map { it.name } == listOf("New daily item", "New default item"))
            assert(state.defaultList.items.last() == "New default item")
            assert(storage.get<DailyList>("day:$date") == state.dailyList)
            assert(storage.get<List<String>>("defaultList") == state.defaultList.items)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "resets today to the current default list unchecked and persists it" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = date)
            state.deleteDefaultItem(state.defaultList.items.first())
            state.addToDefaultList("Tea")
            state.toggleItem("Water")

            state.resetToDefault()

            val expectedItems = state.defaultList.items.map(::Item)
            assert(state.dailyList.items == expectedItems)
            assert(storage.get<DailyList>("day:$date") == state.dailyList)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "deletes a default item, persists the default list and keeps today's list" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val initialList = Language.EN.data.initialList

        try {
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = LocalDate.parse("2026-10-27"))

            assert(storage.getString("defaultList") == null)

            val dailyListBeforeDelete = state.dailyList
            state.deleteDefaultItem(initialList[0])

            assert(state.defaultList.items == initialList.drop(1))
            assert(storage.get<List<String>>("defaultList") == initialList.drop(1))
            assert(state.dailyList == dailyListBeforeDelete)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "resets the default list to the initial list and removes the stored list" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val initialList = Language.EN.data.initialList

        try {
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = LocalDate.parse("2026-10-27"))
            state.deleteDefaultItem(initialList[0])
            state.addDefaultItem("New default item")

            state.resetDefaultList()

            assert(state.defaultList.items == initialList)
            assert(storage.getString("defaultList") == null)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "exposes stored days and lists of past days" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val today = LocalDate.parse("2026-10-27")
        val pastDay = LocalDate.parse("2026-10-20")

        try {
            storage.set("day:$pastDay", DailyList(pastDay, listOf(Item("Bread", checked = true))))
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = today)

            assert(state.storedDays() == setOf(pastDay, today))
            assert(state.dailyListOn(pastDay)?.items == listOf(Item("Bread", checked = true)))
            assert(state.dailyListOn(today) == state.dailyList)
            assert(state.dailyListOn(LocalDate.parse("2026-10-21")) == null)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "copies a past day to today unchecked and ignores today and missing days" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val today = LocalDate.parse("2026-10-27")
        val pastDay = LocalDate.parse("2026-10-20")

        try {
            storage.set("day:$pastDay", DailyList(pastDay, listOf(Item("Past item", checked = true), Item("water"))))
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = today)
            val initialItems = state.dailyList.items

            state.copyToToday(pastDay)

            assert(state.dailyList.items == initialItems + Item("Past item"))
            assert(storage.get<DailyList>("day:$today") == state.dailyList)

            val afterCopy = state.dailyList
            state.copyToToday(today)
            state.copyToToday(LocalDate.parse("2026-10-21"))

            assert(state.dailyList == afterCopy)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "updateToday advances the date and loads or creates the new day's list" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val date1 = LocalDate.parse("2026-10-27")
        val date2 = LocalDate.parse("2026-10-28")

        try {
            val state = AppState(storage, initialLanguage = Language.EN, languagePersistence = {}, todayDate = date1)
            assert(state.today == date1)
            assert(state.dailyList.date == date1)

            state.updateToday(date2)

            assert(state.today == date2)
            assert(state.dailyList.date == date2)
            assert(storage.get<DailyList>("day:$date2") == state.dailyList)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
})
