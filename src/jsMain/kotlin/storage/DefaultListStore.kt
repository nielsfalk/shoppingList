package storage

import model.DefaultList

private const val DEFAULT_LIST_KEY = "defaultList"

fun saveDefaultList(storage: Storage, defaultList: DefaultList, initialList: List<String>) {
    if (defaultList.differsFrom(initialList)) {
        storage.set(DEFAULT_LIST_KEY, defaultList.items)
    } else {
        storage.remove(DEFAULT_LIST_KEY)
    }
}

fun loadDefaultList(storage: Storage, initialList: List<String>): DefaultList =
    storage.get<List<String>>(DEFAULT_LIST_KEY)?.let(::DefaultList) ?: DefaultList(initialList)