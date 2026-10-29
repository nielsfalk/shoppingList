package ui

import androidx.compose.runtime.Composable
import i18n.UiTexts
import model.DailyList
import model.Item
import model.SuggestionOrder
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

class DailyListActions(
    val onResetToDefault: () -> Unit,
    val onAddItem: () -> Unit,
    val onShare: () -> Unit,
    val onToggle: (String) -> Unit,
    val onDelete: (String) -> Unit,
)

@Composable
fun DailyListScreen(
    dailyList: DailyList,
    suggestionOrder: SuggestionOrder,
    texts: UiTexts,
    actions: DailyListActions,
) {
    Div(attrs = { classes("daily-list-screen") }) {
        Div(attrs = { classes("daily-list-header") }) {
            H2 { Text(dailyList.date.toString()) }
            Button(attrs = {
                classes("icon-button")
                attr("aria-label", texts.resetToDefault)
                onClick { actions.onResetToDefault() }
            }) {
                Text("↻")
            }
            Button(attrs = {
                classes("icon-button")
                attr("aria-label", texts.share)
                onClick { actions.onShare() }
            }) {
                Text("⎘")
            }
            Button(attrs = {
                classes("icon-button")
                attr("aria-label", texts.addItem)
                onClick { actions.onAddItem() }
            }) {
                Text("+")
            }
        }
        Div(attrs = { classes("shopping-list") }) {
            dailyList.groupedItems(suggestionOrder).forEach { (department, items) ->
                H3(attrs = { classes("department-heading") }) {
                    Text(department?.let(texts.departmentNames::get) ?: texts.otherDepartment)
                }
                items.forEach { item -> DailyListItem(item, texts.delete, actions.onToggle, actions.onDelete) }
            }
            val completedItems = dailyList.completedItems(suggestionOrder)
            if (completedItems.isNotEmpty()) {
                H3(attrs = { classes("department-heading") }) {
                    Text(texts.completed)
                }
                completedItems.forEach { item ->
                    DailyListItem(item, texts.delete, actions.onToggle, actions.onDelete)
                }
            }
        }
    }
}

@Composable
private fun DailyListItem(item: Item, deleteLabel: String, onToggle: (String) -> Unit, onDelete: (String) -> Unit) {
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
            Text("✕")
        }
    }
}
