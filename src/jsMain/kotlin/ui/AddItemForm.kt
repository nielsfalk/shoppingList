package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Datalist
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Option
import org.jetbrains.compose.web.dom.Text

private const val SUGGESTIONS_ID = "item-suggestions"

@Composable
fun AddItemForm(
    suggestions: List<String>,
    addToDailyListLabel: String,
    addToDefaultListLabel: String,
    onAddToDailyList: (String) -> Unit,
    onAddToDefaultList: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }

    Div(attrs = { classes("add-item-form") }) {
        Input(type = InputType.Text, attrs = {
            classes("add-item-input")
            id("add-item-input")
            attr("list", SUGGESTIONS_ID)
            attr("aria-label", addToDailyListLabel)
            attr("autocomplete", "off")
            value(name)
            onInput { name = it.value }
        })
        Datalist(attrs = { id(SUGGESTIONS_ID) }) {
            suggestions.forEach { suggestion -> Option(value = suggestion) }
        }
        Button(attrs = {
            classes("add-button")
            if (name.isBlank()) attr("disabled", "")
            onClick {
                onAddToDailyList(name)
                name = ""
            }
        }) {
            Text(addToDailyListLabel)
        }
        Button(attrs = {
            classes("add-button")
            if (name.isBlank()) attr("disabled", "")
            onClick {
                onAddToDefaultList(name)
                name = ""
            }
        }) {
            Text(addToDefaultListLabel)
        }
    }
}
