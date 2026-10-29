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

    fun delete(name: String): DefaultList {
        val normalizedName = name.normalizedName()
        return copy(items = items.filterNot { it.normalizedName() == normalizedName })
    }

    fun differsFrom(initial: List<String>): Boolean = items != initial
}

fun addToDefaultList(defaultList: DefaultList, dailyList: DailyList, name: String): Pair<DefaultList, DailyList> =
    defaultList.add(name) to dailyList.add(name)