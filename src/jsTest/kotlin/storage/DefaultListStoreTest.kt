package storage

import model.DefaultList
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultListStoreTest {
    @Test
    fun savesAndLoadsCustomizedDefaultListInOrder() {
        val storage = newTestStorage()
        val initialList = listOf("Water", "Mustard", "Oat milk")
        val customizedList = DefaultList(listOf("Mustard", "Bread", "Water"))

        try {
            saveDefaultList(storage, customizedList, initialList)

            assertEquals(customizedList, loadDefaultList(storage, initialList))
            assertEquals(listOf("Mustard", "Bread", "Water"), storage.get<List<String>>("defaultList"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun removesStoredListWhenItMatchesInitialAndUsesInitialWhenMissing() {
        val storage = newTestStorage()
        val initialList = listOf("Water", "Mustard", "Oat milk")
        val initialDefaultList = DefaultList(initialList)

        try {
            assertEquals(initialDefaultList, loadDefaultList(storage, initialList))
            storage.set("defaultList", listOf("Bread"))

            saveDefaultList(storage, initialDefaultList, initialList)

            assertNull(storage.getString("defaultList"))
            assertEquals(initialDefaultList, loadDefaultList(storage, initialList))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    private fun newTestStorage() = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
}