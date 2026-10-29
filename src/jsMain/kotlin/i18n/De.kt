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
        resetDefaultList = "Standardliste auf Ausgangsliste zurücksetzen",
        copyToToday = "In heutige Liste übernehmen",
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
        otherDepartment = "Sonstiges",
        completed = "Erledigt",
    ),
    initialList = listOf(
        "Äpfel", "Bananen", "Tomaten", "Karotten", "Salat", "Brot", "Haferflocken", "Nudeln", "Senf",
        "Vegane Margarine", "Vegane Schokolade", "Wasser", "Hafermilch", "Pflanzlicher Joghurt", "Tofu",
        "Veganer Käse", "Vegane Pizza", "Toilettenpapier",
    ),
    suggestionsByDepartment = mapOf(
        FRUIT_AND_VEGETABLES to listOf(
            "Äpfel", "Birnen", "Bananen", "Orangen", "Zitronen", "Beeren", "Trauben", "Avocados",
            "Tomaten", "Gurken", "Paprika", "Karotten", "Kartoffeln", "Zwiebeln", "Knoblauch", "Salat",
            "Spinat", "Brokkoli", "Zucchini", "Champignons", "Kohl", "Süßkartoffeln",
        ),
        BAKERY to listOf(
            "Brot", "Brötchen", "Toastbrot", "Fladenbrot", "Pita", "Croissants", "Brioche",
        ),
        DRY_GOODS to listOf(
            "Haferflocken", "Müsli", "Cornflakes", "Reis", "Nudeln", "Couscous", "Quinoa", "Bulgur", "Linsen",
            "Getrocknete Bohnen", "Getrocknete Erbsen", "Mandeln", "Walnüsse", "Cashewkerne",
            "Sonnenblumenkerne", "Chiasamen", "Eiernudeln",
        ),
        CANNED_AND_JARS to listOf(
            "Gehackte Tomaten", "Tomatenmark", "Passierte Tomaten", "Mais", "Kidneybohnen", "Kichererbsen",
            "Apfelmus", "Essiggurken", "Marmelade", "Erdnussbutter", "Thunfisch", "Sardinen", "Honig",
            "Nuss-Nougat-Creme",
        ),
        OILS_SPICES_AND_SAUCES to listOf(
            "Olivenöl", "Rapsöl", "Essig", "Salz", "Pfeffer", "Paprikapulver", "Currypulver", "Zimt", "Senf",
            "Ketchup", "Sojasoße", "Vegane Mayonnaise", "Gemüsebrühe", "Mayonnaise", "Rinderbrühe",
            "Hühnerbrühe", "Fischsoße", "Remoulade", "Vegane Margarine", "Margarine",
        ),
        BAKING to listOf(
            "Mehl", "Zucker", "Backpulver", "Hefe", "Vanillezucker", "Kakao", "Speisestärke", "Gelatine",
            "Kondensmilch", "Eier",
        ),
        SWEETS_AND_SNACKS to listOf(
            "Vegane Schokolade", "Schokolade", "Vegane Kekse", "Kekse", "Kartoffelchips", "Popcorn", "Salzstangen", "Trockenfrüchte",
            "Milchschokolade", "Gummibärchen", "Butterkekse", "Schokoriegel",
        ),
        BEVERAGES to listOf(
            "Wasser", "Fruchtsaft", "Limonade", "Kaffee", "Tee", "Mineralwasser", "Bier", "Wein", "Eiskaffee",
        ),
        CHILLED to listOf(
            "Hafermilch", "Sojadrink", "Mandeldrink", "Pflanzlicher Joghurt", "Tofu", "Tempeh",
            "Veganer Aufstrich", "Aufstrich", "Frische Pasta", "Vegane Sahne", "Veganer Käse",
            "Räuchertofu", "Milch", "Buttermilch", "Butter", "Käse", "Gouda", "Mozzarella", "Feta",
            "Parmesan", "Frischkäse", "Quark", "Joghurt", "Sahne", "Schmand", "Schinken", "Salami",
            "Speck", "Würstchen", "Hackfleisch", "Hähnchenbrust", "Schweinekotelett", "Rindersteak", "Lachs",
            "Räucherlachs", "Garnelen", "Hering",
        ),
        FROZEN to listOf(
            "Tiefkühlgemüse", "Tiefkühlbeeren", "Pommes", "Vegane Pizza", "Veganes Eis", "Eis", "Tiefkühlspinat",
            "Edamame", "Vegane Nuggets", "Nuggets", "Fischstäbchen", "Tiefkühlfisch", "Tiefkühlgarnelen",
            "Tiefkühlhähnchen", "Speiseeis", "Pizza",
        ),
        DRUGSTORE_AND_HOUSEHOLD to listOf(
            "Toilettenpapier", "Taschentücher", "Küchenrolle", "Seife", "Shampoo", "Zahnpasta", "Waschmittel",
            "Spülmittel", "Müllbeutel", "Schwämme", "Reinigungsmittel", "Zahnseide", "Deodorant",
            "Spülmaschinentabs",
        ),
        NON_FOOD to listOf(
            "Batterien", "Glühbirnen", "Kerzen", "Alufolie", "Backpapier", "Katzenfutter", "Hundefutter",
        ),
    ),
)