package i18n

data class UiTexts(
    val dailyList: String,
    val defaultList: String,
    val history: String,
    val addToDailyList: String,
    val addToDefaultList: String,
    val delete: String,
    val copyToToday: String,
    val moveUp: String,
    val moveDown: String,
)

data class LanguageData(
    val ui: UiTexts,
    val initialList: List<String>,
    val suggestions: List<String>,
)