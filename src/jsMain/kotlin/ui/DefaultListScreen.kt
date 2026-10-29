package ui

import androidx.compose.runtime.Composable
import i18n.UiTexts
import model.DefaultList
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun DefaultListScreen(
    defaultList: DefaultList,
    initialList: List<String>,
    texts: UiTexts,
    onDelete: (String) -> Unit,
    onAdd: (String) -> Unit,
    onReset: () -> Unit,
) {
    Div(attrs = { classes("default-list-header") }) {
        Button(attrs = {
            classes("icon-button")
            attr("aria-label", texts.resetDefaultList)
            if (!defaultList.differsFrom(initialList)) attr("disabled", "")
            onClick { onReset() }
        }) {
            Text("↻")
        }
    }
    Div(attrs = { classes("default-list-screen", "shopping-list") }) {
        defaultList.items.forEach { name ->
            Div(attrs = { classes("shopping-list-item") }) {
                Span(attrs = { classes("default-item-name") }) { Text(name) }
                Button(attrs = {
                    classes("delete-button")
                    attr("aria-label", "${texts.delete} $name")
                    onClick { onDelete(name) }
                }) {
                    Text("✕")
                }
            }
        }
        ItemNameInput(
            label = texts.addToDefaultList,
            buttons = listOf(
                SubmitButton(texts.addToDefaultList, "★", onAdd),
            ),
        )
    }
}
