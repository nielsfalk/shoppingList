package model

class SuggestionOrder(suggestionsByDepartment: Map<Department, List<String>>) {
    private data class Position(
        val department: Department,
        val index: Int,
    )

    private val positionsByName = mutableMapOf<String, Position>().apply {
        var index = 0
        Department.entries.forEach { department ->
            suggestionsByDepartment[department].orEmpty().forEach { name ->
                val normalizedName = name.normalizedName()
                if (normalizedName !in this) this[normalizedName] = Position(department, index)
                index++
            }
        }
    }

    fun departmentOf(name: String): Department? = positionsByName[name.normalizedName()]?.department

    fun positionOf(name: String): Int? = positionsByName[name.normalizedName()]?.index

    fun <T> sorted(items: List<T>, name: (T) -> String): List<T> =
        items.sortedBy { positionOf(name(it)) ?: Int.MAX_VALUE }
}