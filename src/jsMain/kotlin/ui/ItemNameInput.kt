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

data class SubmitButton(
    val label: String,
    val symbol: String,
    val onSubmit: (String) -> Unit,
)

@Composable
fun ItemNameInput(
    label: String,
    suggestions: List<String> = emptyList(),
    suggestionsId: String? = null,
    inputId: String? = null,
    buttons: List<SubmitButton>,
) {
    var name by remember { mutableStateOf("") }

    Div(attrs = { classes("add-item-form") }) {
        Input(type = InputType.Text, attrs = {
            classes("add-item-input")
            if (inputId != null) id(inputId)
            if (suggestionsId != null) attr("list", suggestionsId)
            attr("aria-label", label)
            attr("autocomplete", "off")
            value(name)
            onInput { name = it.value }
            onKeyDown {
                if (it.key == "Enter" && name.isNotBlank()) {
                    buttons.firstOrNull()?.let { button ->
                        button.onSubmit(name)
                        name = ""
                    }
                }
            }
        })
        if (suggestionsId != null && suggestions.isNotEmpty()) {
            Datalist(attrs = { id(suggestionsId) }) {
                suggestions.forEach { suggestion -> Option(value = suggestion) }
            }
        }
        buttons.forEach { button ->
            Button(attrs = {
                classes("add-button")
                attr("aria-label", button.label)
                if (name.isBlank()) attr("disabled", "")
                onClick {
                    button.onSubmit(name)
                    name = ""
                }
            }) {
                Text(button.symbol)
            }
        }
    }
}
