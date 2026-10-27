package storage

import kotlinx.browser.localStorage
import model.Item
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StorageTest {
    @Test
    fun namespacesKeysAndSerializesValuesAsJson() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")

        try {
            storage.set("item", Item("Water"))

            assertEquals(Item("Water"), storage.get<Item>("item"))
            assertEquals("{\"name\":\"Water\",\"checked\":false}", storage.getString("item"))
            assertEquals(listOf("item"), storage.keys())
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun removesNamespacedValuesWithoutTouchingOtherKeys() {
        val prefix = "shoppingList:test:${Random.nextLong()}:"
        val storage = Storage(prefix = prefix)
        val unrelatedKey = "shoppingList:other:${Random.nextLong()}"

        try {
            localStorage.setItem(unrelatedKey, "keep")
            storage.setString("temporary", "value")
            storage.remove("temporary")

            assertNull(storage.getString("temporary"))
            assertEquals("keep", localStorage.getItem(unrelatedKey))
            assertEquals(emptyList(), storage.keys())
        } finally {
            localStorage.removeItem(unrelatedKey)
        }
    }
}