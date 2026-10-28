package model

fun suggestionsFor(dailyList: DailyList, defaultList: DefaultList, languageSuggestions: List<String>): List<String> {
    val dailyItemNames = dailyList.items.mapTo(mutableSetOf()) { it.name.normalizedName() }
    val suggestions = mutableListOf<String>()
    val seenNames = mutableSetOf<String>()

    (defaultList.items.filterNot { it.normalizedName() in dailyItemNames } + languageSuggestions).forEach { suggestion ->
        if (seenNames.add(suggestion.normalizedName())) suggestions += suggestion
    }

    return suggestions
}