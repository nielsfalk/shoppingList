package storage

import model.DefaultList

fun saveDefaultList(storage: Storage, defaultList: DefaultList, initialList: List<String>) =
    DefaultListRepository(storage).save(defaultList, initialList)

fun loadDefaultList(storage: Storage, initialList: List<String>): DefaultList =
    DefaultListRepository(storage).load(initialList)