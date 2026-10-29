package ui

import i18n.Language
import i18n.LanguageData
import kotlinx.datetime.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.DailyList
import model.DefaultList
import model.addToDefaultList
import model.dailyListFor
import model.today
import storage.DailyListRepository
import storage.DefaultListRepository
import storage.LanguageRepository
import storage.Storage

class AppState(
    storage: Storage = Storage(),
    private val dailyLists: DailyListRepository = DailyListRepository(storage),
    private val defaultLists: DefaultListRepository = DefaultListRepository(storage),
    private val languages: LanguageRepository = LanguageRepository(storage),
    initialLanguage: Language = languages.currentLanguage(),
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

    var defaultList by mutableStateOf(defaultLists.load(initialLanguage.data.initialList))
        private set

    var dailyList by mutableStateOf(dailyLists.loadOrCreate(todayDate, defaultList))
        private set

    private fun updateDailyList(transform: (DailyList) -> DailyList) {
        dailyList = transform(dailyList)
        dailyLists.save(dailyList)
    }

    private fun updateDefaultList(transform: (DefaultList) -> DefaultList) {
        defaultList = transform(defaultList)
        defaultLists.save(defaultList, languageData.initialList)
    }

    fun switchLanguage(language: Language) {
        languages.storeLanguage(language)
        this.language = language
        defaultList = defaultLists.load(language.data.initialList)
    }

    fun updateToday(newToday: LocalDate = today()) {
        if (newToday == today) return
        today = newToday
        dailyList = dailyLists.loadOrCreate(newToday, defaultList)
    }

    fun storedDays(): Set<LocalDate> = dailyLists.storedDays().toSet()

    fun dailyListOn(date: LocalDate): DailyList? = if (date == today) dailyList else dailyLists.load(date)

    fun copyToToday(date: LocalDate) {
        if (date >= today) return
        val oldList = dailyLists.load(date) ?: return
        updateDailyList { it.copyFrom(oldList) }
    }

    fun resetToDefault() = updateDailyList { dailyListFor(today, null, defaultList) }

    fun setDailyListItems(items: List<model.Item>) = updateDailyList { it.copy(items = items) }

    fun toggleItem(name: String) = updateDailyList { it.toggle(name) }

    fun deleteItem(name: String) = updateDailyList { it.delete(name) }

    fun addToDailyList(name: String) = updateDailyList { it.add(name) }

    fun deleteDefaultItem(name: String) = updateDefaultList { it.delete(name) }

    fun addDefaultItem(name: String) = updateDefaultList { it.add(name) }

    fun resetDefaultList() = updateDefaultList { DefaultList(languageData.initialList) }

    fun addToDefaultList(name: String) {
        val (updatedDefaultList, updatedDailyList) = addToDefaultList(defaultList, dailyList, name)
        defaultList = updatedDefaultList
        dailyList = updatedDailyList
        defaultLists.save(defaultList, languageData.initialList)
        dailyLists.save(dailyList)
    }
}