package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import i18n.UiTexts
import model.DefaultList
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun DefaultListScreen(
    defaultList: DefaultList,
    texts: UiTexts,
    onMoveUp: (String) -> Unit,
    onMoveDown: (String) -> Unit,
    onDelete: (String) -> Unit,
    onAdd: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    Div(attrs = { classes("default-list-screen", "shopping-list") }) {
        defaultList.items.forEachIndexed { index, name ->
            Div(attrs = { classes("shopping-list-item") }) {
                Span(attrs = { classes("default-item-name") }) { Text(name) }
                Button(attrs = {
                    classes("move-button")
                    attr("aria-label", "${texts.moveUp} $name")
                    if (index == 0) attr("disabled", "")
                    onClick { onMoveUp(name) }
                }) {
                    Text("▲")
                }
                Button(attrs = {
                    classes("move-button")
                    attr("aria-label", "${texts.moveDown} $name")
                    if (index == defaultList.items.lastIndex) attr("disabled", "")
                    onClick { onMoveDown(name) }
                }) {
                    Text("▼")
                }
                Button(attrs = {
                    classes("delete-button")
                    attr("aria-label", "${texts.delete} $name")
                    onClick { onDelete(name) }
                }) {
                    Text("✕")
                }
            }
        }
        Div(attrs = { classes("add-item-form") }) {
            Input(type = InputType.Text, attrs = {
                classes("add-item-input")
                attr("aria-label", texts.addToDefaultList)
                attr("autocomplete", "off")
                value(name)
                onInput { name = it.value }
                onKeyDown {
                    if (it.key == "Enter" && name.isNotBlank()) {
                        onAdd(name)
                        name = ""
                    }
                }
            })
            Button(attrs = {
                classes("add-button")
                attr("aria-label", texts.addToDefaultList)
                if (name.isBlank()) attr("disabled", "")
                onClick {
                    onAdd(name)
                    name = ""
                }
            }) {
                Text("★")
            }
        }
    }
}
