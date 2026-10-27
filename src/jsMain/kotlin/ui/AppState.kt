package ui

import i18n.LanguageData
import i18n.currentLanguage
import i18n.languageData
import i18n.languages
import i18n.storeLanguage
import kotlinx.datetime.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.DailyList
import model.DefaultList
import model.dailyListFor
import model.today
import storage.Storage
import storage.loadDailyList
import storage.loadDefaultList
import storage.saveDailyList

class AppState(
    private val storage: Storage = Storage(),
    initialLanguageCode: String = currentLanguage(),
    private val languagePersistence: (String) -> Unit = ::storeLanguage,
    todayDate: LocalDate = today(),
) {
    var languageCode by mutableStateOf(initialLanguageCode)
        private set

    var defaultList by mutableStateOf(loadDefaultList(storage, languageData(initialLanguageCode).initialList))
        private set

    var dailyList by mutableStateOf(loadOrCreateDailyList(storage, todayDate, defaultList))
        private set

    val languageData: LanguageData
        get() = languageData(languageCode)

    fun switchLanguage(languageCode: String) {
        require(languageCode in languages) { "Unsupported language: $languageCode" }
        languagePersistence(languageCode)
        this.languageCode = languageCode
        defaultList = loadDefaultList(storage, languageData(languageCode).initialList)
    }

    fun toggleItem(name: String) {
        dailyList = dailyList.toggle(name)
        saveDailyList(storage, dailyList)
    }

    fun deleteItem(name: String) {
        dailyList = dailyList.delete(name)
        saveDailyList(storage, dailyList)
    }
}

private fun loadOrCreateDailyList(storage: Storage, date: LocalDate, defaultList: DefaultList): DailyList {
    val existing = loadDailyList(storage, date)
    val dailyList = dailyListFor(date, existing, defaultList)
    if (existing?.date != date) {
        saveDailyList(storage, dailyList)
    }
    return dailyList
}