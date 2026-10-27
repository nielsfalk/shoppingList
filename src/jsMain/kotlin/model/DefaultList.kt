package model

data class DefaultList(
    val items: List<String>,
) {
    fun add(name: String): DefaultList {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return this

        val normalizedName = trimmedName.normalizedName()
        if (items.any { it.normalizedName() == normalizedName }) return this
        return copy(items = items + trimmedName)
    }

    fun moveUp(name: String): DefaultList = move(name, -1)

    fun moveDown(name: String): DefaultList = move(name, 1)

    fun delete(name: String): DefaultList {
        val normalizedName = name.normalizedName()
        return copy(items = items.filterNot { it.normalizedName() == normalizedName })
    }

    fun differsFrom(initial: List<String>): Boolean = items != initial

    private fun move(name: String, offset: Int): DefaultList {
        val currentIndex = items.indexOfFirst { it.normalizedName() == name.normalizedName() }
        val targetIndex = currentIndex + offset
        if (currentIndex < 0 || targetIndex !in items.indices) return this

        val movedItems = items.toMutableList()
        movedItems[currentIndex] = items[targetIndex]
        movedItems[targetIndex] = items[currentIndex]
        return copy(items = movedItems)
    }
}

fun addToDefaultList(defaultList: DefaultList, dailyList: DailyList, name: String): Pair<DefaultList, DailyList> =
    defaultList.add(name) to dailyList.add(name)