package ui

import androidx.compose.runtime.Composable
import i18n.Language
import i18n.LanguageData
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import routing.Route
import routing.toHash

@Composable
fun NavigationBar(route: Route, languageData: LanguageData, currentLanguage: Language, onLanguageChange: (Language) -> Unit) {
    Div(attrs = { classes("navigation") }) {
        navigationLink(Route.DailyList(), route, languageData.ui.dailyList)
        navigationLink(Route.DefaultList, route, languageData.ui.defaultList)
        navigationLink(Route.History(), route, languageData.ui.history)
        Div(attrs = { classes("language-switch") }) {
            Language.entries.forEach { lang ->
                Button(attrs = {
                    classes("language-button")
                    attr("aria-pressed", (currentLanguage == lang).toString())
                    onClick { onLanguageChange(lang) }
                }) {
                    Text(lang.code.uppercase())
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
            if (current == target || (target is Route.DailyList && current is Route.DailyList) || (target == Route.History() && current is Route.History)) {
                attr("aria-current", "page")
            }
        },
    ) {
        Text(label)
    }
}