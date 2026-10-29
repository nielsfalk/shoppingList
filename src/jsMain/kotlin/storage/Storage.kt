package storage

import kotlinx.browser.localStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.Storage as BrowserStorage

class Storage(
    private val backend: BrowserStorage = localStorage,
    private val prefix: String = "shoppingList:",
) {
    @PublishedApi
    internal val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    fun getString(key: String): String? = backend.getItem(storageKey(key))

    fun setString(key: String, value: String) {
        backend.setItem(storageKey(key), value)
    }

    fun remove(key: String) {
        backend.removeItem(storageKey(key))
    }

    fun keys(): List<String> = (0 until backend.length)
        .mapNotNull(backend::key)
        .filter { it.startsWith(prefix) }
        .map { it.removePrefix(prefix) }

    inline fun <reified T> get(key: String): T? = getString(key)?.let { serialized ->
        runCatching { json.decodeFromString<T>(serialized) }.getOrNull()
    }

    inline fun <reified T> set(key: String, value: T) {
        setString(key, json.encodeToString(value))
    }

    private fun storageKey(key: String): String = "$prefix$key"
}