package ui

import androidx.compose.runtime.Composable
import i18n.LanguageData
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import routing.Route
import routing.toHash

@Composable
fun NavigationBar(route: Route, languageData: LanguageData, languageCode: String, onLanguageChange: (String) -> Unit) {
    Div(attrs = { classes("navigation") }) {
        navigationLink(Route.DailyList, route, languageData.ui.dailyList)
        navigationLink(Route.DefaultList, route, languageData.ui.defaultList)
        navigationLink(Route.History(), route, languageData.ui.history)
        Div(attrs = { classes("language-switch") }) {
            listOf("de", "en").forEach { code ->
                Button(attrs = {
                    classes("language-button")
                    attr("aria-pressed", (languageCode == code).toString())
                    onClick { onLanguageChange(code) }
                }) {
                    Text(code.uppercase())
                }
            }
        }
    }
}

@Composable
private fun navigationLink(target: Route, current: Route, label: String) {
    A(
        href = target.toHash(),
        attrs = {
            classes("navigation-link")
            if (current == target || (target == Route.History() && current is Route.History)) {
                attr("aria-current", "page")
            }
        },
    ) {
        Text(label)
    }
}