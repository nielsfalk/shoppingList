package pwa

import kotlinx.browser.window
import kotlin.js.Promise
import org.w3c.dom.Navigator
import org.w3c.dom.events.Event

external interface ServiceWorkerRegistration

external interface ServiceWorkerContainer {
    val controller: ServiceWorkerRegistration?
    fun register(scriptURL: String): Promise<ServiceWorkerRegistration>
    fun addEventListener(type: String, callback: (Event) -> Unit)
}

val Navigator.serviceWorker: ServiceWorkerContainer?
    get() = asDynamic().serviceWorker as? ServiceWorkerContainer

fun registerServiceWorker(): Promise<ServiceWorkerRegistration?>? {
    val serviceWorker = window.navigator.serviceWorker ?: return null

    val hadController = serviceWorker.controller != null
    var reloading = false
    serviceWorker.addEventListener("controllerchange", {
        if (hadController && !reloading) {
            reloading = true
            window.location.reload()
        }
    })

    return serviceWorker.register("sw.js").catch { error ->
        console.warn("Service worker registration failed", error)
        null
    }
}
