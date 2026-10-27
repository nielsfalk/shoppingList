package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import routing.Route
import routing.rememberRoute

@Composable
fun App() {
    val state = androidx.compose.runtime.remember { AppState() }
    val route = rememberRoute()
    val data = state.languageData

    SideEffect { document.documentElement?.setAttribute("lang", state.languageCode) }

    Div(attrs = { classes("app") }) {
        H1(attrs = { classes("app-title") }) { Text("shoppingList") }
        NavigationBar(route, data, state.languageCode, state::switchLanguage)
        when (route) {
            Route.DailyList -> {
                H1 { Text(data.ui.dailyList) }
                DailyListScreen(state.dailyList, state::toggleItem)
            }
            Route.DefaultList -> H1 { Text(data.ui.defaultList) }
            is Route.History -> H1 { Text(data.ui.history) }
        }
    }
}