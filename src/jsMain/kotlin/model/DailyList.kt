package model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DailyList(
    val date: LocalDate,
    val items: List<Item>,
) {
    fun sortedItems(): List<Item> = items.filterNot { it.checked } + items.filter { it.checked }
}