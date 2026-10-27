package model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DailyList(
    val date: LocalDate,
    val items: List<Item>,
) {
    fun sortedItems(): List<Item> = items.filterNot { it.checked } + items.filter { it.checked }

    fun toggle(name: String): DailyList {
        val normalizedName = name.normalizedName()
        return copy(
            items = items.map { item ->
                if (item.name.normalizedName() == normalizedName) item.copy(checked = !item.checked) else item
            },
        )
    }

    fun delete(name: String): DailyList {
        val normalizedName = name.normalizedName()
        return copy(items = items.filterNot { it.name.normalizedName() == normalizedName })
    }
}