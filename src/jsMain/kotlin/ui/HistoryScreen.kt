package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import i18n.UiTexts
import kotlinx.datetime.LocalDate
import model.DailyList
import model.SuggestionOrder
import model.toCalendarMonth
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import routing.Route
import routing.toHash

@Composable
fun HistoryScreen(
    today: LocalDate,
    selectedDate: LocalDate?,
    storedDays: Set<LocalDate>,
    selectedList: DailyList?,
    texts: UiTexts,
    suggestionOrder: SuggestionOrder,
    onCopyToToday: (LocalDate) -> Unit,
) {
    var month by remember(selectedDate) { mutableStateOf((selectedDate ?: today).toCalendarMonth()) }

    Div(attrs = { classes("history-screen") }) {
        Div(attrs = { classes("calendar-header") }) {
            Button(attrs = {
                classes("month-button")
                attr("aria-label", texts.previousMonth)
                onClick { month = month.previous() }
            }) { Text("◀") }
            H2(attrs = { classes("calendar-title") }) {
                Text("${texts.monthNames[month.month - 1]} ${month.year}")
            }
            Button(attrs = {
                classes("month-button")
                attr("aria-label", texts.nextMonth)
                onClick { month = month.next() }
            }) { Text("▶") }
        }
        Div(attrs = { classes("calendar-grid") }) {
            texts.weekdayNames.forEach { weekday ->
                Span(attrs = { classes("calendar-weekday") }) { Text(weekday) }
            }
            month.weeks().flatten().forEach { day ->
                when {
                    day == null -> Span(attrs = { classes("calendar-day", "calendar-day-empty") })
                    day in storedDays -> A(href = Route.History(day).toHash(), attrs = {
                        classes(*dayClasses(day, today, selectedDate, stored = true))
                        if (day == selectedDate) attr("aria-current", "date")
                    }) { Text(day.day.toString()) }
                    else -> Span(attrs = {
                        classes(*dayClasses(day, today, selectedDate, stored = false))
                    }) { Text(day.day.toString()) }
                }
            }
        }
        if (selectedDate != null) {
            ReadOnlyDailyList(selectedDate, selectedList, texts, suggestionOrder, canCopy = selectedDate < today, onCopyToToday)
        }
    }
}

@Composable
private fun ReadOnlyDailyList(
    date: LocalDate,
    dailyList: DailyList?,
    texts: UiTexts,
    suggestionOrder: SuggestionOrder,
    canCopy: Boolean,
    onCopyToToday: (LocalDate) -> Unit,
) {
    Div(attrs = { classes("history-day") }) {
        H3 { Text(date.toString()) }
        if (dailyList == null) {
            P { Text(texts.noList) }
        } else {
            Ul(attrs = { classes("shopping-list", "read-only-list") }) {
                dailyList.sortedItems(suggestionOrder).forEach { item ->
                    Li(attrs = {
                        classes("read-only-item")
                        if (item.checked) classes("checked")
                    }) {
                        Text("${if (item.checked) "☑" else "☐"} ${item.name}")
                    }
                }
            }
            if (canCopy) {
                Button(attrs = {
                    classes("copy-button")
                    onClick { onCopyToToday(date) }
                }) {
                    Text(texts.copyToToday)
                }
            }
        }
    }
}

private fun dayClasses(day: LocalDate, today: LocalDate, selectedDate: LocalDate?, stored: Boolean): Array<String> =
    listOfNotNull(
        "calendar-day",
        "calendar-day-stored".takeIf { stored },
        "calendar-day-today".takeIf { day == today },
        "calendar-day-selected".takeIf { day == selectedDate },
    ).toTypedArray()
