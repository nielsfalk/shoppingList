package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import routing.Route
import routing.rememberRoute
import routing.toHash

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
                DailyListScreen(
                    state.dailyList,
                    data.ui.delete,
                    data.ui.resetToDefault,
                    state::resetToDefault,
                    state::toggleItem,
                    state::deleteItem,
                )
                AddItemForm(
                    data.suggestions,
                    data.ui.addToDailyList,
                    data.ui.addToDefaultList,
                    state::addToDailyList,
                    state::addToDefaultList,
                )
            }
            Route.DefaultList -> {
                H1 { Text(data.ui.defaultList) }
                DefaultListScreen(
                    state.defaultList,
                    data.ui,
                    state::moveDefaultItemUp,
                    state::moveDefaultItemDown,
                    state::deleteDefaultItem,
                )
            }
            is Route.History -> {
                H1 { Text(data.ui.history) }
                HistoryScreen(
                    today = state.today,
                    selectedDate = route.date,
                    storedDays = state.storedDays(),
                    selectedList = route.date?.let(state::dailyListOn),
                    texts = data.ui,
                    onCopyToToday = { date ->
                        state.copyToToday(date)
                        window.location.hash = Route.DailyList.toHash()
                    },
                )
            }
        }
    }
}