package ui

import i18n.En
import i18n.languageData
import storage.Storage
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class AppStateTest {
    @Test
    fun switchingLanguagePersistsChoiceAndLoadsItsInitialList() {
        val storage = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
        val persistedLanguages = mutableListOf<String>()

        try {
            val state = AppState(storage, initialLanguageCode = "de", languagePersistence = persistedLanguages::add)

            state.switchLanguage("en")

            assertEquals("en", state.languageCode)
            assertEquals(En, state.languageData)
            assertEquals(languageData("en").initialList, state.defaultList.items)
            assertEquals(listOf("en"), persistedLanguages)
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
}