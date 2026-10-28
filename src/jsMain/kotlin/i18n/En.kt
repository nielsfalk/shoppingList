package i18n

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
    ),
    initialList = listOf("Toilet paper", "Water", "Mustard", "Oat milk", "Apples", "Rolled oats"),
    suggestions = listOf("Bread", "Margarine", "Soy milk", "Tofu", "Lentils", "Bananas", "Tomatoes", "Pasta"),
)