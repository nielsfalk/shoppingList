package ui

import i18n.LanguageData
import i18n.currentLanguage
import i18n.languageData
import i18n.languages
import i18n.storeLanguage
import model.DefaultList
import storage.Storage
import storage.loadDefaultList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppState(
    private val storage: Storage = Storage(),
    initialLanguageCode: String = currentLanguage(),
    private val languagePersistence: (String) -> Unit = ::storeLanguage,
) {
    var languageCode by mutableStateOf(initialLanguageCode)
        private set

    var defaultList by mutableStateOf(loadDefaultList(storage, languageData(initialLanguageCode).initialList))
        private set

    val languageData: LanguageData
        get() = languageData(languageCode)

    fun switchLanguage(languageCode: String) {
        require(languageCode in languages) { "Unsupported language: $languageCode" }
        languagePersistence(languageCode)
        this.languageCode = languageCode
        defaultList = loadDefaultList(storage, languageData(languageCode).initialList)
    }
}