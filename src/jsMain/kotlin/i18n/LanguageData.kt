package i18n

data class UiTexts(
    val dailyList: String,
    val defaultList: String,
    val history: String,
    val addItem: String,
    val addToDailyList: String,
    val addToDefaultList: String,
    val delete: String,
    val resetToDefault: String,
    val copyToToday: String,
    val moveUp: String,
    val moveDown: String,
    val previousMonth: String,
    val nextMonth: String,
    val monthNames: List<String>,
    val weekdayNames: List<String>,
    val noList: String,
)

data class LanguageData(
    val ui: UiTexts,
    val initialList: List<String>,
    val suggestions: List<String>,
)