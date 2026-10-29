package i18n

import model.Department

val En = LanguageData(
    ui = UiTexts(
        dailyList = "Daily list",
        defaultList = "Default list",
        history = "History",
        addItem = "Add item",
        addToDailyList = "Add to daily list",
        addToDefaultList = "Add to default list",
        delete = "Delete",
        resetToDefault = "Reset to default list",
        copyToToday = "Copy to today",
        moveUp = "Move up",
        moveDown = "Move down",
        previousMonth = "Previous month",
        nextMonth = "Next month",
        monthNames = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December",
        ),
        weekdayNames = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"),
        noList = "There is no list for this day.",
        departmentNames = mapOf(
            Department.FRUIT_AND_VEGETABLES to "Fruit and vegetables",
            Department.BAKERY to "Bakery",
            Department.DRY_GOODS to "Dry goods",
            Department.CANNED_AND_JARS to "Canned goods and jars",
            Department.OILS_SPICES_AND_SAUCES to "Oils, spices and sauces",
            Department.BAKING to "Baking",
            Department.SWEETS_AND_SNACKS to "Sweets and snacks",
            Department.BEVERAGES to "Beverages",
            Department.CHILLED to "Chilled",
            Department.FROZEN to "Frozen",
            Department.DRUGSTORE_AND_HOUSEHOLD to "Drugstore and household",
            Department.NON_FOOD to "Non-food",
        ),
    ),
    initialList = listOf("Toilet paper", "Water", "Mustard", "Oat milk", "Apples", "Rolled oats"),
    suggestions = listOf("Bread", "Margarine", "Soy milk", "Tofu", "Lentils", "Bananas", "Tomatoes", "Pasta"),
)