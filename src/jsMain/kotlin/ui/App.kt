package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import kotlinx.browser.document
import kotlinx.browser.window
import model.suggestionsFor
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import routing.Route
import routing.rememberRoute
import routing.toHash

@Composable
fun App() {
    val state = remember { AppState() }
    val route = rememberRoute()
    val data = state.languageData

    LaunchedEffect(route) {
        if (route is Route.DailyList && route.sharedItems != null) {
            val sharedItems = route.sharedItems
            if (state.dailyList.items != sharedItems) {
                val shouldOverwrite = if (state.dailyList.differsFrom(state.defaultList)) {
                    window.confirm(data.ui.confirmOverwriteDailyList)
                } else {
                    true
                }
                if (shouldOverwrite) {
                    state.setDailyListItems(sharedItems)
                }
            }
        }
    }

    DisposableEffect(state) {
        val onFocusOrVisible: (Event) -> Unit = {
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
        NavigationBar(route, data, state.language, state::switchLanguage)
        when (route) {
            is Route.DailyList -> {
                H1 { Text(data.ui.dailyList) }
                DailyListScreen(
                    dailyList = state.dailyList,
                    suggestionOrder = data.suggestionOrder,
                    texts = data.ui,
                    actions = DailyListActions(
                        onResetToDefault = {
                            if (window.confirm(data.ui.confirmResetDailyList)) {
                                state.resetToDefault()
                            }
                        },
                        onAddItem = {
                            (document.getElementById("add-item-input") as? HTMLElement)?.let { input ->
                                input.scrollIntoView()
                                input.focus()
                            }
                        },
                        onShare = {
                            val hash = Route.DailyList(state.dailyList.items).toHash()
                            window.location.hash = hash
                            runCatching {
                                val fullUrl = "${window.location.origin}${window.location.pathname}$hash"
                                window.navigator.asDynamic().clipboard?.writeText(fullUrl)
                            }
                        },
                        onToggle = state::toggleItem,
                        onDelete = state::deleteItem,
                    ),
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
                    onReset = {
                        if (window.confirm(data.ui.confirmResetDefaultList)) {
                            state.resetDefaultList()
                        }
                    },
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
                        window.location.hash = Route.DailyList().toHash()
                    },
                )
            }
        }
    }
}
