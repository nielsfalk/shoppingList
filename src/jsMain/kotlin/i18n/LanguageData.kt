package i18n

import model.Department

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
    val departmentNames: Map<Department, String>,
)

data class LanguageData(
    val ui: UiTexts,
    val initialList: List<String>,
    val suggestionsByDepartment: Map<Department, List<String>>,
) {
    val suggestions: List<String> = Department.entries.flatMap { suggestionsByDepartment[it].orEmpty() }
}