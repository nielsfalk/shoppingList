package storage

import io.kotest.core.spec.style.FreeSpec
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlin.js.Promise
import kotlin.test.assertTrue as assert

class PersistentStorageTest : FreeSpec({
    "persistent storage is not requested when the storage manager is unsupported" {
        withStorageManager(null) {
            assert(requestPersistentStorage() == null)
        }
    }

    "requests persistent storage when available" {
        var persistCalls = 0
        val storageManager = js("({})")
        storageManager.persist = {
            persistCalls++
            Promise.resolve(true)
        }

        withStorageManager(storageManager) {
            val persisted = requestPersistentStorage().unsafeCast<Promise<Boolean>>().await()

            assert(persisted)
        }

        assert(persistCalls == 1)
    }

    "a failing request does not throw" {
        val storageManager = js("({})")
        storageManager.persist = { Promise.reject(Throwable("persist failed")) }

        withStorageManager(storageManager) {
            val persisted = requestPersistentStorage().unsafeCast<Promise<Boolean>>().await()

            assert(!persisted)
        }
    }
})

private suspend fun withStorageManager(storageManager: dynamic, block: suspend () -> Unit) {
    val navigator = window.navigator
    val descriptor = js("{}")
    descriptor.value = storageManager
    descriptor.configurable = true
    js("Object").defineProperty(navigator, "storage", descriptor)
    try {
        block()
    } finally {
        js("Reflect").deleteProperty(navigator, "storage")
    }
}
