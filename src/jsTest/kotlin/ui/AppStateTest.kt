package ui

import i18n.En
import i18n.languageData
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
        val persistedLanguages = mutableListOf<String>()
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguageCode = "de", languagePersistence = {
                persistedLanguages += it
            }, todayDate = date)

            assert(state.dailyList.date == date)
            assert(state.dailyList.items.map { it.name } == languageData("de").initialList)
            val firstItem = state.dailyList.items.first()
            state.toggleItem(firstItem.name)
            assert(state.dailyList.items.first().checked)
            assert(storage.get<DailyList>("day:$date") == state.dailyList)

            state.switchLanguage("en")

            assert(state.languageCode == "en")
            assert(state.languageData == En)
            assert(state.defaultList.items == languageData("en").initialList)
            assert(state.dailyList.items.first().name == firstItem.name)
            assert(state.dailyList.items.first().checked)

            state.deleteItem(firstItem.name)

            assert(!state.dailyList.items.any { it.name == firstItem.name })
            assert(storage.get<DailyList>("day:$date") == state.dailyList)
            assert(persistedLanguages == listOf("en"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "adds items to the daily and default list and persists both" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val date = LocalDate.parse("2026-10-27")

        try {
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = date)

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
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = date)
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

    "reorders the default list and removes the stored list when it matches the initial list again" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val initialList = languageData("en").initialList

        try {
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = LocalDate.parse("2026-10-27"))

            state.moveDefaultItemDown(initialList[0])

            assert(state.defaultList.items.take(2) == listOf(initialList[1], initialList[0]))
            assert(storage.get<List<String>>("defaultList") == state.defaultList.items)

            state.moveDefaultItemUp(initialList[0])

            assert(state.defaultList.items == initialList)
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

    "exposes stored days and lists of past days" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val today = LocalDate.parse("2026-10-27")
        val pastDay = LocalDate.parse("2026-10-20")

        try {
            storage.set("day:$pastDay", DailyList(pastDay, listOf(Item("Bread", checked = true))))
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = today)

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
            val state = AppState(storage, initialLanguageCode = "en", languagePersistence = {}, todayDate = today)
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
})
