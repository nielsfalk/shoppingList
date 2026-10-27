package pwa

import kotlinx.browser.window

fun registerServiceWorker(): dynamic {
    val serviceWorker = window.navigator.asDynamic().serviceWorker
    if (serviceWorker == null || serviceWorker == undefined) return null
    return serviceWorker.register("sw.js").catch { error: dynamic ->
        console.warn("Service worker registration failed", error)
    }
}
