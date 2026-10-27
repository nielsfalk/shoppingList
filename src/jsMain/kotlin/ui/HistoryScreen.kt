package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import i18n.UiTexts
import kotlinx.datetime.LocalDate
import model.toCalendarMonth
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun HistoryScreen(today: LocalDate, texts: UiTexts) {
    var month by remember { mutableStateOf(today.toCalendarMonth()) }

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
                if (day == null) {
                    Span(attrs = { classes("calendar-day", "calendar-day-empty") })
                } else {
                    Span(attrs = {
                        classes("calendar-day")
                        if (day == today) classes("calendar-day-today")
                    }) { Text(day.day.toString()) }
                }
            }
        }
    }
}
