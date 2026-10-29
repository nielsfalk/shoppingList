package ui

import androidx.compose.runtime.Composable

private const val SUGGESTIONS_ID = "item-suggestions"

@Composable
fun AddItemForm(
    suggestions: List<String>,
    addToDailyListLabel: String,
    addToDefaultListLabel: String,
    onAddToDailyList: (String) -> Unit,
    onAddToDefaultList: (String) -> Unit,
) {
    ItemNameInput(
        label = addToDailyListLabel,
        suggestions = suggestions,
        suggestionsId = SUGGESTIONS_ID,
        inputId = "add-item-input",
        buttons = listOf(
            SubmitButton(addToDailyListLabel, "+", onAddToDailyList),
            SubmitButton(addToDefaultListLabel, "★", onAddToDefaultList),
        ),
    )
}
