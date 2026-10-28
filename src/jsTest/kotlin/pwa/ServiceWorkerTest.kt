package pwa

import io.kotest.core.spec.style.FreeSpec
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlin.js.Promise
import kotlin.test.assertTrue as assert

class ServiceWorkerTest : FreeSpec({
    "registration is skipped when service workers are not supported" {
        withServiceWorker(null) {
            assert(registerServiceWorker() == null)
        }
    }

    "registration does not throw when registering the service worker fails" {
        val registeredScripts = mutableListOf<String>()
        val failingServiceWorker = js("({})")
        failingServiceWorker.controller = null
        failingServiceWorker.addEventListener = { _: String, _: dynamic -> }
        failingServiceWorker.register = { script: String ->
            registeredScripts += script
            Promise.reject(Throwable("registration failed"))
        }

        withServiceWorker(failingServiceWorker) {
            val registration: Promise<Any?> = registerServiceWorker().unsafeCast<Promise<Any?>>()

            registration.await()
        }

        assert(registeredScripts == listOf("sw.js"))
    }
})

private suspend fun withServiceWorker(serviceWorker: dynamic, block: suspend () -> Unit) {
    val navigator = window.navigator
    val descriptor = js("{}")
    descriptor.value = serviceWorker
    descriptor.configurable = true
    js("Object").defineProperty(navigator, "serviceWorker", descriptor)
    try {
        block()
    } finally {
        js("Reflect").deleteProperty(navigator, "serviceWorker")
    }
}
