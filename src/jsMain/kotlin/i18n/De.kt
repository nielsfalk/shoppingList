package i18n

import model.Department.*

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
            FRUIT_AND_VEGETABLES to "Obst und Gemüse",
            BAKERY to "Bäckerei",
            DRY_GOODS to "Trockenwaren",
            CANNED_AND_JARS to "Konserven und Gläser",
            OILS_SPICES_AND_SAUCES to "Öle, Gewürze und Soßen",
            BAKING to "Backzutaten",
            SWEETS_AND_SNACKS to "Süßigkeiten und Snacks",
            BEVERAGES to "Getränke",
            CHILLED to "Kühlregal",
            FROZEN to "Tiefkühlprodukte",
            DRUGSTORE_AND_HOUSEHOLD to "Drogerie und Haushalt",
            NON_FOOD to "Non-Food",
        ),
    ),
    initialList = listOf(
        "Äpfel", "Bananen", "Orangen", "Tomaten", "Gurken", "Paprika", "Karotten", "Kartoffeln", "Zwiebeln",
        "Knoblauch", "Salat", "Brot", "Haferflocken", "Reis", "Nudeln", "Linsen", "Getrocknete Bohnen", "Mandeln",
        "Gehackte Tomaten", "Kichererbsen", "Olivenöl", "Salz", "Pfeffer", "Senf", "Mehl", "Vegane Schokolade",
        "Wasser", "Kaffee", "Hafermilch", "Pflanzlicher Joghurt", "Tofu", "Vegane Margarine", "Veganer Aufstrich",
        "Tiefkühlgemüse", "Toilettenpapier", "Shampoo", "Zahnpasta", "Waschmittel", "Spülmittel", "Müllbeutel",
        "Batterien",
    ),
    suggestionsByDepartment = mapOf(
        FRUIT_AND_VEGETABLES to listOf(
            "Äpfel", "Birnen", "Bananen", "Orangen", "Zitronen", "Beeren", "Trauben", "Avocados",
            "Tomaten", "Gurken", "Paprika", "Karotten", "Kartoffeln", "Zwiebeln", "Knoblauch", "Salat",
            "Spinat", "Brokkoli", "Zucchini", "Champignons", "Kohl", "Süßkartoffeln",
        ),
        BAKERY to listOf("Brot", "Brötchen", "Toastbrot", "Fladenbrot", "Pita"),
        DRY_GOODS to listOf(
            "Haferflocken", "Müsli", "Cornflakes", "Reis", "Nudeln", "Couscous", "Quinoa", "Bulgur",
            "Linsen", "Getrocknete Bohnen", "Getrocknete Erbsen", "Mandeln", "Walnüsse", "Cashewkerne",
            "Sonnenblumenkerne", "Chiasamen",
        ),
        CANNED_AND_JARS to listOf(
            "Gehackte Tomaten", "Tomatenmark", "Passierte Tomaten", "Mais", "Kidneybohnen", "Kichererbsen",
            "Apfelmus", "Essiggurken", "Marmelade", "Erdnussbutter",
        ),
        OILS_SPICES_AND_SAUCES to listOf(
            "Olivenöl", "Rapsöl", "Essig", "Salz", "Pfeffer", "Paprikapulver", "Currypulver", "Zimt",
            "Senf", "Ketchup", "Sojasoße", "Vegane Mayonnaise", "Gemüsebrühe",
        ),
        BAKING to listOf("Mehl", "Zucker", "Backpulver", "Hefe", "Vanillezucker", "Kakao", "Speisestärke"),
        SWEETS_AND_SNACKS to listOf(
            "Vegane Schokolade", "Vegane Kekse", "Kartoffelchips", "Popcorn", "Salzstangen", "Trockenfrüchte",
        ),
        BEVERAGES to listOf("Wasser", "Fruchtsaft", "Limonade", "Kaffee", "Tee", "Mineralwasser"),
        CHILLED to listOf(
            "Hafermilch", "Sojadrink", "Mandeldrink", "Pflanzlicher Joghurt", "Tofu", "Tempeh",
            "Vegane Margarine", "Veganer Aufstrich", "Frische Pasta", "Vegane Sahne", "Veganer Käse",
            "Räuchertofu",
        ),
        FROZEN to listOf(
            "Tiefkühlgemüse", "Tiefkühlbeeren", "Pommes", "Vegane Pizza", "Veganes Eis", "Tiefkühlspinat",
            "Edamame", "Vegane Nuggets",
        ),
        DRUGSTORE_AND_HOUSEHOLD to listOf(
            "Toilettenpapier", "Taschentücher", "Küchenrolle", "Seife", "Shampoo", "Zahnpasta", "Waschmittel",
            "Spülmittel", "Müllbeutel", "Schwämme", "Reinigungsmittel", "Zahnseide", "Deodorant",
            "Spülmaschinentabs",
        ),
        NON_FOOD to listOf("Batterien", "Glühbirnen", "Kerzen", "Alufolie", "Backpapier"),
    ),
)