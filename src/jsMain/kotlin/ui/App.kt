package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import kotlinx.browser.document
import kotlinx.browser.window
import model.suggestionsFor
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import routing.Route
import routing.rememberRoute
import routing.toHash

@Composable
fun App() {
    val state = remember { AppState() }
    val route = rememberRoute()
    val data = state.languageData

    DisposableEffect(state) {
        val onFocusOrVisible: (dynamic) -> Unit = {
            state.updateToday()
        }
        window.addEventListener("focus", onFocusOrVisible)
        document.addEventListener("visibilitychange", onFocusOrVisible)
        onDispose {
            window.removeEventListener("focus", onFocusOrVisible)
            document.removeEventListener("visibilitychange", onFocusOrVisible)
        }
    }

    SideEffect { document.documentElement?.setAttribute("lang", state.languageCode) }

    Div(attrs = { classes("app") }) {
        H1(attrs = { classes("app-title") }) { Text("shoppingList") }
        NavigationBar(route, data, state.languageCode, state::switchLanguage)
        when (route) {
            Route.DailyList -> {
                H1 { Text(data.ui.dailyList) }
                DailyListScreen(
                    state.dailyList,
                    data.suggestionOrder,
                    data.ui.departmentNames,
                    data.ui.otherDepartment,
                    data.ui.delete,
                    data.ui.resetToDefault,
                    data.ui.addItem,
                    state::resetToDefault,
                    { document.getElementById("add-item-input")?.asDynamic()?.focus() },
                    state::toggleItem,
                    state::deleteItem,
                )
                AddItemForm(
                    suggestionsFor(state.dailyList, state.defaultList, data.suggestions),
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
                    data.initialList,
                    data.ui,
                    state::deleteDefaultItem,
                    state::addDefaultItem,
                    state::resetDefaultList,
                )
            }
            is Route.History -> {
                H1 { Text(data.ui.history) }
                val storedDays = remember { state.storedDays() }
                HistoryScreen(
                    today = state.today,
                    selectedDate = route.date,
                    storedDays = storedDays,
                    selectedList = route.date?.let(state::dailyListOn),
                    texts = data.ui,
                    suggestionOrder = data.suggestionOrder,
                    onCopyToToday = { date ->
                        state.copyToToday(date)
                        window.location.hash = Route.DailyList.toHash()
                    },
                )
            }
        }
    }
}