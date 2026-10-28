package storage

import io.kotest.core.spec.style.FreeSpec
import model.DefaultList
import kotlin.random.Random
import kotlin.test.assertTrue as assert

class DefaultListStoreTest : FreeSpec({
    "saves and loads a customized default list in order" {
        val storage = newTestStorage()
        val initialList = listOf("Water", "Mustard", "Oat milk")
        val customizedList = DefaultList(listOf("Mustard", "Bread", "Water"))

        try {
            saveDefaultList(storage, customizedList, initialList)

            assert(loadDefaultList(storage, initialList) == customizedList)
            assert(storage.get<List<String>>("defaultList") == listOf("Mustard", "Bread", "Water"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "removes the stored list when it matches the initial list and uses the initial list when missing" {
        val storage = newTestStorage()
        val initialList = listOf("Water", "Mustard", "Oat milk")
        val initialDefaultList = DefaultList(initialList)

        try {
            assert(loadDefaultList(storage, initialList) == initialDefaultList)
            storage.set("defaultList", listOf("Bread"))

            saveDefaultList(storage, initialDefaultList, initialList)

            assert(storage.getString("defaultList") == null)
            assert(loadDefaultList(storage, initialList) == initialDefaultList)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
})

private fun newTestStorage() = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
