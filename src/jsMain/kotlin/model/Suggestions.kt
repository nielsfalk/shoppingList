package model

fun suggestionsFor(dailyList: DailyList, defaultList: DefaultList, languageSuggestions: List<String>): List<String> {
    val dailyItemKeys = dailyList.items.mapTo(mutableSetOf()) { it.key }
    val suggestions = mutableListOf<String>()
    val seenKeys = mutableSetOf<ItemKey>()

    (defaultList.items.filterNot { it.toItemKey() in dailyItemKeys } + languageSuggestions).forEach { suggestion ->
        if (seenKeys.add(suggestion.toItemKey())) suggestions += suggestion
    }

    return suggestions
}