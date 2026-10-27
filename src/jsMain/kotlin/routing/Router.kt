package routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.browser.window

@Composable
fun rememberRoute(): Route {
    val route = remember { mutableStateOf(parseRoute(window.location.hash)) }

    DisposableEffect(Unit) {
        val listener: (dynamic) -> Unit = { route.value = parseRoute(window.location.hash) }
        window.asDynamic().addEventListener("hashchange", listener)
        onDispose { window.asDynamic().removeEventListener("hashchange", listener) }
    }

    return route.value
}