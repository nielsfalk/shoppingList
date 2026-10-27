package ui

import androidx.compose.runtime.Composable
import i18n.currentLanguage
import i18n.languageData
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import routing.Route
import routing.rememberRoute

@Composable
fun App() {
    val route = rememberRoute()
    val data = languageData(currentLanguage())

    Div(attrs = { classes("app") }) {
        H1(attrs = { classes("app-title") }) { Text("shoppingList") }
        NavigationBar(route, data)
        when (route) {
            Route.DailyList -> H1 { Text(data.ui.dailyList) }
            Route.DefaultList -> H1 { Text(data.ui.defaultList) }
            is Route.History -> H1 { Text(data.ui.history) }
        }
    }
}