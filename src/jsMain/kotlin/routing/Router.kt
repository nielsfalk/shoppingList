package routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.browser.window
import org.w3c.dom.events.Event

@Composable
fun rememberRoute(): Route {
    val route = remember { mutableStateOf(parseRoute(window.location.hash)) }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = { route.value = parseRoute(window.location.hash) }
        window.addEventListener("hashchange", listener)
        onDispose { window.removeEventListener("hashchange", listener) }
    }

    return route.value
}