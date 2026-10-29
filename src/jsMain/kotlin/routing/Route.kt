package routing

import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Item

sealed interface Route {
    data class DailyList(val sharedItems: List<Item>? = null) : Route
    data object DefaultList : Route
    data class History(val date: LocalDate? = null) : Route
}

private val routeJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

external fun encodeURIComponent(uri: String): String
external fun decodeURIComponent(encodedURI: String): String

fun encodeSharedItems(items: List<Item>): String =
    encodeURIComponent(routeJson.encodeToString(items))

fun decodeSharedItems(param: String): List<Item>? = runCatching {
    routeJson.decodeFromString<List<Item>>(decodeURIComponent(param))
}.getOrNull()

fun parseRoute(hash: String): Route {
    val cleanHash = hash.removePrefix("#").trim('/')
    val pathPart = cleanHash.substringBefore('?')
    val queryPart = cleanHash.substringAfter('?', "")
    val segments = pathPart.split('/').filter(String::isNotEmpty)

    val itemsParam = if (queryPart.isNotEmpty()) {
        queryPart.split('&')
            .firstOrNull { it.startsWith("items=") }
            ?.substringAfter("items=")
    } else null

    val sharedItems = itemsParam?.let(::decodeSharedItems)

    return when {
        segments.isEmpty() -> Route.DailyList(sharedItems)
        segments == listOf("default") -> Route.DefaultList
        segments.firstOrNull() == "history" -> Route.History(segments.getOrNull(1)?.let { date ->
            runCatching { LocalDate.parse(date) }.getOrNull()
        })
        else -> Route.DailyList(sharedItems)
    }
}

fun Route.toHash(): String = when (this) {
    is Route.DailyList -> sharedItems?.let { "#/?items=${encodeSharedItems(it)}" } ?: "#/"
    Route.DefaultList -> "#/default"
    is Route.History -> date?.let { "#/history/$it" } ?: "#/history"
}