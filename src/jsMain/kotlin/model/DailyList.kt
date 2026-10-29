package model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class DailyList(
    val date: LocalDate,
    val items: List<Item>,
) {
    fun sortedItems(order: SuggestionOrder): List<Item> =
        order.sorted(items.filterNot { it.checked }, Item::name) + completedItems(order)

    fun groupedItems(order: SuggestionOrder): List<Pair<Department?, List<Item>>> {
        val uncheckedItems = order.sorted(items.filterNot { it.checked }, Item::name)
        val itemsByDepartment = uncheckedItems.groupBy { order.departmentOf(it.name) }
        return (Department.entries + null).mapNotNull { department ->
            itemsByDepartment[department]?.let { department to it }
        }
    }

    fun completedItems(order: SuggestionOrder): List<Item> =
        order.sorted(items.filter { it.checked }, Item::name)

    fun toggle(name: String): DailyList {
        val targetKey = name.toItemKey()
        return copy(
            items = items.map { item ->
                if (item.key == targetKey) item.copy(checked = !item.checked) else item
            },
        )
    }

    fun delete(name: String): DailyList {
        val targetKey = name.toItemKey()
        return copy(items = items.filterNot { it.key == targetKey })
    }

    fun add(name: String): DailyList {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return this

        val targetKey = trimmedName.toItemKey()
        val existingIndex = items.indexOfFirst { it.key == targetKey }
        if (existingIndex < 0) return copy(items = items + Item(trimmedName))

        val existingItem = items[existingIndex]
        if (!existingItem.checked) return this
        return copy(
            items = items.mapIndexed { index, item ->
                if (index == existingIndex) item.copy(checked = false) else item
            },
        )
    }

    fun copyFrom(old: DailyList): DailyList = old.items.fold(this) { dailyList, item -> dailyList.add(item.name) }

    fun differsFrom(defaultList: DefaultList): Boolean = items != defaultList.items.map(::Item)
}

fun dailyListFor(today: LocalDate, existing: DailyList?, defaultList: DefaultList): DailyList {
    if (existing?.date == today) return existing
    return DailyList(today, defaultList.items.map(::Item))
}

fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())