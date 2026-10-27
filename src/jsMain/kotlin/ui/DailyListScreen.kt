package ui

import androidx.compose.runtime.Composable
import model.DailyList
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun DailyListScreen(dailyList: DailyList, onToggle: (String) -> Unit) {
    Div(attrs = { classes("daily-list-screen") }) {
        H2 { Text(dailyList.date.toString()) }
        Div(attrs = { classes("shopping-list") }) {
            dailyList.sortedItems().forEach { item ->
                Button(attrs = {
                    classes("shopping-item")
                    attr("aria-pressed", item.checked.toString())
                    onClick { onToggle(item.name) }
                }) {
                    Text("${if (item.checked) "☑" else "☐"} ${item.name}")
                }
            }
        }
    }
}