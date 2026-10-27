package ui

import androidx.compose.runtime.Composable
import model.DailyList
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun DailyListScreen(
    dailyList: DailyList,
    deleteLabel: String,
    resetToDefaultLabel: String,
    onResetToDefault: () -> Unit,
    onToggle: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Div(attrs = { classes("daily-list-screen") }) {
        Div(attrs = { classes("daily-list-header") }) {
            H2 { Text(dailyList.date.toString()) }
            Button(attrs = {
                classes("icon-button")
                attr("aria-label", resetToDefaultLabel)
                onClick { onResetToDefault() }
            }) {
                Text("↻")
            }
        }
        Div(attrs = { classes("shopping-list") }) {
            dailyList.sortedItems().forEach { item ->
                Div(attrs = { classes("shopping-list-item") }) {
                    Button(attrs = {
                        classes("shopping-item")
                        attr("aria-pressed", item.checked.toString())
                        onClick { onToggle(item.name) }
                    }) {
                        Text("${if (item.checked) "☑" else "☐"} ${item.name}")
                    }
                    Button(attrs = {
                        classes("delete-button")
                        attr("aria-label", "$deleteLabel ${item.name}")
                        onClick { onDelete(item.name) }
                    }) {
                        Text(deleteLabel)
                    }
                }
            }
        }
    }
}