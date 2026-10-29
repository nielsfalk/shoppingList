package storage

import i18n.Language
import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import model.DailyList
import model.DefaultList
import model.Item
import kotlin.random.Random
import kotlin.test.assertTrue as assert

class RepositoryTest : FreeSpec({
    "DailyListRepository saves, loads and retrieves stored days" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val repo = DailyListRepository(storage)
        val date1 = LocalDate.parse("2026-10-25")
        val date2 = LocalDate.parse("2026-10-26")

        try {
            assert(repo.load(date1) == null)
            assert(repo.storedDays().isEmpty())

            val list1 = DailyList(date1, listOf(Item("Apple")))
            repo.save(list1)

            assert(repo.load(date1) == list1)
            assert(repo.storedDays() == listOf(date1))

            val defaultList = DefaultList(listOf("Bread"))
            val created = repo.loadOrCreate(date2, defaultList)
            assert(created.date == date2)
            assert(created.items == listOf(Item("Bread")))
            assert(repo.storedDays() == listOf(date1, date2))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "DefaultListRepository loads initial list when not modified and persists modifications" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val repo = DefaultListRepository(storage)
        val initial = listOf("Bread", "Milk")

        try {
            val loaded = repo.load(initial)
            assert(loaded.items == initial)
            assert(storage.getString("defaultList") == null)

            val modified = DefaultList(listOf("Bread", "Milk", "Eggs"))
            repo.save(modified, initial)
            assert(storage.getString("defaultList") != null)
            assert(repo.load(initial) == modified)

            repo.save(DefaultList(initial), initial)
            assert(storage.getString("defaultList") == null)
            assert(repo.load(initial).items == initial)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "LanguageRepository stores and retrieves language preferences" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val repo = LanguageRepository(storage)

        try {
            assert(repo.storedLanguage() == null)

            repo.storeLanguage(Language.DE)
            assert(repo.storedLanguage() == "de")

            repo.storeLanguage(Language.EN)
            assert(repo.storedLanguage() == "en")
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
})
