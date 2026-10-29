package model

class SuggestionOrder(
    suggestionsByDepartment: Map<Department, List<String>>,
    aliases: Map<String, String> = emptyMap(),
) {
    private data class Position(
        val department: Department,
        val index: Int,
    )

    private val aliasesByKey = aliases.mapKeys { it.key.toItemKey() }.mapValues { it.value.toItemKey() }

    private val positionsByKey = mutableMapOf<ItemKey, Position>().apply {
        var index = 0
        Department.entries.forEach { department ->
            suggestionsByDepartment[department].orEmpty().forEach { name ->
                val key = name.toItemKey()
                if (key !in this) this[key] = Position(department, index)
                index++
            }
        }
    }

    private fun resolveKey(key: ItemKey): ItemKey = aliasesByKey[key] ?: key

    fun departmentOf(name: String): Department? = positionsByKey[resolveKey(name.toItemKey())]?.department

    fun positionOf(name: String): Int? = positionsByKey[resolveKey(name.toItemKey())]?.index

    fun <T> sorted(items: List<T>, name: (T) -> String): List<T> =
        items.sortedBy { positionOf(name(it)) ?: Int.MAX_VALUE }
}