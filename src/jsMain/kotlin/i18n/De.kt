package i18n

val De = LanguageData(
    ui = UiTexts(
        dailyList = "Tagesliste",
        defaultList = "Standardliste",
        history = "Verlauf",
        addToDailyList = "Zur Tagesliste",
        addToDefaultList = "Zur Standardliste",
        delete = "Löschen",
        resetToDefault = "Auf Standardliste zurücksetzen",
        copyToToday = "In heutige Liste übernehmen",
        moveUp = "Nach oben",
        moveDown = "Nach unten",
        previousMonth = "Vorheriger Monat",
        nextMonth = "Nächster Monat",
        monthNames = listOf(
            "Januar", "Februar", "März", "April", "Mai", "Juni",
            "Juli", "August", "September", "Oktober", "November", "Dezember",
        ),
        weekdayNames = listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"),
        noList = "Für diesen Tag gibt es keine Liste.",
    ),
    initialList = listOf("Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken"),
    suggestions = listOf("Brot", "Margarine", "Sojamilch", "Tofu", "Linsen", "Bananen", "Tomaten", "Nudeln"),
)