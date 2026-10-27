package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Datalist
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Option

private const val SUGGESTIONS_ID = "item-suggestions"

@Composable
fun AddItemForm(suggestions: List<String>, label: String) {
    var name by remember { mutableStateOf("") }

    Div(attrs = { classes("add-item-form") }) {
        Input(type = InputType.Text, attrs = {
            classes("add-item-input")
            attr("list", SUGGESTIONS_ID)
            attr("aria-label", label)
            attr("autocomplete", "off")
            value(name)
            onInput { name = it.value }
        })
        Datalist(attrs = { id(SUGGESTIONS_ID) }) {
            suggestions.forEach { suggestion -> Option(value = suggestion) }
        }
    }
}
