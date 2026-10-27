package storage

import kotlinx.browser.window

fun requestPersistentStorage(): dynamic {
    val storageManager = window.navigator.asDynamic().storage
    if (storageManager == null || storageManager.persist == null) return null
    return storageManager.persist().catch { false }
}