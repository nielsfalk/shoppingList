package pwa

import kotlinx.browser.window

fun registerServiceWorker(): dynamic {
    val serviceWorker = window.navigator.asDynamic().serviceWorker
    if (serviceWorker == null || serviceWorker == undefined) return null

    val hadController = serviceWorker.controller != null
    var reloading = false
    serviceWorker.addEventListener("controllerchange", {
        if (hadController && !reloading) {
            reloading = true
            window.location.reload()
        }
    })

    return serviceWorker.register("sw.js").catch { error: dynamic ->
        console.warn("Service worker registration failed", error)
    }
}
