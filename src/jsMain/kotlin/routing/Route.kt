package routing

import kotlinx.datetime.LocalDate

sealed interface Route {
    data object DailyList : Route
    data object DefaultList : Route
    data class History(val date: LocalDate? = null) : Route
}

fun parseRoute(hash: String): Route {
    val segments = hash.removePrefix("#").trim('/').split('/').filter(String::isNotEmpty)
    return when {
        segments.isEmpty() -> Route.DailyList
        segments == listOf("default") -> Route.DefaultList
        segments.firstOrNull() == "history" -> Route.History(segments.getOrNull(1)?.let { date ->
            runCatching { LocalDate.parse(date) }.getOrNull()
        })
        else -> Route.DailyList
    }
}

fun Route.toHash(): String = when (this) {
    Route.DailyList -> "#/"
    Route.DefaultList -> "#/default"
    is Route.History -> date?.let { "#/history/$it" } ?: "#/history"
}