package ui

import i18n.Language
import i18n.LanguageData
import i18n.currentLanguage
import i18n.storeLanguage
import kotlinx.datetime.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.DailyList
import model.DefaultList
import model.addToDefaultList
import model.dailyListFor
import model.today
import storage.Storage
import storage.loadDailyList
import storage.loadDefaultList
import storage.saveDailyList
import storage.saveDefaultList
import storage.storedDays

class AppState(
    private val storage: Storage = Storage(),
    initialLanguage: Language = currentLanguage(),
    private val languagePersistence: (Language) -> Unit = ::storeLanguage,
    todayDate: LocalDate = today(),
) {
    var today by mutableStateOf(todayDate)
        private set

    var language by mutableStateOf(initialLanguage)
        private set

    val languageCode: String
        get() = language.code

    val languageData: LanguageData
        get() = language.data

    var defaultList by mutableStateOf(loadDefaultList(storage, initialLanguage.data.initialList))
        private set

    var dailyList by mutableStateOf(loadOrCreateDailyList(storage, todayDate, defaultList))
        private set

    fun switchLanguage(language: Language) {
        languagePersistence(language)
        this.language = language
        defaultList = loadDefaultList(storage, language.data.initialList)
    }

    fun updateToday(newToday: LocalDate = today()) {
        if (newToday == today) return
        today = newToday
        dailyList = loadOrCreateDailyList(storage, newToday, defaultList)
    }

    fun storedDays(): Set<LocalDate> = storedDays(storage).toSet()

    fun dailyListOn(date: LocalDate): DailyList? = if (date == today) dailyList else loadDailyList(storage, date)

    fun copyToToday(date: LocalDate) {
        if (date >= today) return
        val oldList = loadDailyList(storage, date) ?: return
        dailyList = dailyList.copyFrom(oldList)
        saveDailyList(storage, dailyList)
    }

    fun resetToDefault() {
        dailyList = dailyListFor(today, null, defaultList)
        saveDailyList(storage, dailyList)
    }

    fun toggleItem(name: String) {
        dailyList = dailyList.toggle(name)
        saveDailyList(storage, dailyList)
    }

    fun deleteItem(name: String) {
        dailyList = dailyList.delete(name)
        saveDailyList(storage, dailyList)
    }

    fun addToDailyList(name: String) {
        dailyList = dailyList.add(name)
        saveDailyList(storage, dailyList)
    }

    fun deleteDefaultItem(name: String) = updateDefaultList(defaultList.delete(name))

    fun addDefaultItem(name: String) = updateDefaultList(defaultList.add(name))

    fun resetDefaultList() = updateDefaultList(DefaultList(languageData.initialList))

    private fun updateDefaultList(updatedDefaultList: DefaultList) {
        defaultList = updatedDefaultList
        saveDefaultList(storage, defaultList, languageData.initialList)
    }

    fun addToDefaultList(name: String) {
        val (updatedDefaultList, updatedDailyList) = addToDefaultList(defaultList, dailyList, name)
        defaultList = updatedDefaultList
        dailyList = updatedDailyList
        saveDefaultList(storage, defaultList, languageData.initialList)
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