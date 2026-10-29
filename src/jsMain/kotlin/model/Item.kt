package model

import kotlinx.serialization.Serializable

value class ItemKey private constructor(val value: String) {
    companion object {
        fun of(name: String): ItemKey = ItemKey(name.trim().lowercase())
    }
}

val Item.key: ItemKey get() = ItemKey.of(name)
fun String.toItemKey(): ItemKey = ItemKey.of(this)

@Serializable
data class Item(
    val name: String,
    val checked: Boolean = false,
)