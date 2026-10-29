package storage

import kotlinx.browser.window
import kotlin.js.Promise
import org.w3c.dom.Navigator

external interface StorageManager {
    fun persist(): Promise<Boolean>
}

val Navigator.storage: StorageManager?
    get() = asDynamic().storage as? StorageManager

fun requestPersistentStorage(): Promise<Boolean>? {
    val storageManager = window.navigator.storage ?: return null
    return storageManager.persist().catch { false }
}