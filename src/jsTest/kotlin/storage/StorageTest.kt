package storage

import io.kotest.core.spec.style.FreeSpec
import kotlinx.browser.localStorage
import model.Item
import kotlin.random.Random
import kotlin.test.assertTrue as assert

class StorageTest : FreeSpec({
    "namespaces keys and serializes values as JSON" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")

        try {
            storage.set("item", Item("Water"))

            assert(storage.get<Item>("item") == Item("Water"))
            assert(storage.getString("item") == "{\"name\":\"Water\",\"checked\":false}")
            assert(storage.keys() == listOf("item"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "removes namespaced values without touching other keys" {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val unrelatedKey = "shoppingList:other:${Random.nextLong()}"

        try {
            localStorage.setItem(unrelatedKey, "keep")
            storage.setString("temporary", "value")

            storage.remove("temporary")

            assert(storage.getString("temporary") == null)
            assert(localStorage.getItem(unrelatedKey) == "keep")
            assert(storage.keys().isEmpty())
        } finally {
            localStorage.removeItem(unrelatedKey)
        }
    }
})
