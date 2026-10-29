package i18n

import model.Department

val De = LanguageData(
    ui = UiTexts(
        dailyList = "Tagesliste",
        defaultList = "Standardliste",
        history = "Verlauf",
        addItem = "Artikel hinzufügen",
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
        departmentNames = mapOf(
            Department.FRUIT_AND_VEGETABLES to "Obst und Gemüse",
            Department.BAKERY to "Bäckerei",
            Department.DRY_GOODS to "Trockenwaren",
            Department.CANNED_AND_JARS to "Konserven und Gläser",
            Department.OILS_SPICES_AND_SAUCES to "Öle, Gewürze und Soßen",
            Department.BAKING to "Backzutaten",
            Department.SWEETS_AND_SNACKS to "Süßigkeiten und Snacks",
            Department.BEVERAGES to "Getränke",
            Department.CHILLED to "Kühlregal",
            Department.FROZEN to "Tiefkühlprodukte",
            Department.DRUGSTORE_AND_HOUSEHOLD to "Drogerie und Haushalt",
            Department.NON_FOOD to "Non-Food",
        ),
    ),
    initialList = listOf("Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken"),
    suggestions = listOf("Brot", "Margarine", "Sojamilch", "Tofu", "Linsen", "Bananen", "Tomaten", "Nudeln"),
)