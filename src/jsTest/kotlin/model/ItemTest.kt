package model

import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class ItemTest : FreeSpec({
    "ItemKey trims and normalizes to lower case" {
        val key1 = ItemKey.of("  Apples  ")
        val key2 = "apples".toItemKey()
        val key3 = Item("APPLES").key

        assert(key1 == key2)
        assert(key2 == key3)
        assert(key1.value == "apples")
    }
})
