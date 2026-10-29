package i18n

import model.Department.*

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
            FRUIT_AND_VEGETABLES to "Fruit and vegetables",
            BAKERY to "Bakery",
            DRY_GOODS to "Dry goods",
            CANNED_AND_JARS to "Canned goods and jars",
            OILS_SPICES_AND_SAUCES to "Oils, spices and sauces",
            BAKING to "Baking",
            SWEETS_AND_SNACKS to "Sweets and snacks",
            BEVERAGES to "Beverages",
            CHILLED to "Chilled",
            FROZEN to "Frozen",
            DRUGSTORE_AND_HOUSEHOLD to "Drugstore and household",
            NON_FOOD to "Non-food",
        ),
    ),
    initialList = listOf(
        "Toilet paper", "Water", "Mustard", "Oat milk", "Apples", "Rolled oats", "Bananas", "Oranges",
        "Tomatoes", "Cucumbers", "Bell peppers", "Carrots", "Potatoes", "Onions", "Garlic", "Lettuce", "Bread",
        "Rice", "Pasta", "Lentils", "Dried beans", "Chickpeas", "Almonds", "Chopped tomatoes", "Olive oil",
        "Salt", "Pepper", "Flour", "Vegan chocolate", "Coffee", "Plant-based yogurt", "Tofu", "Vegan margarine",
        "Vegan spread", "Frozen vegetables", "Shampoo", "Toothpaste", "Laundry detergent", "Dish soap",
        "Trash bags", "Batteries",
    ),
    suggestionsByDepartment = mapOf(
        FRUIT_AND_VEGETABLES to listOf(
            "Apples", "Pears", "Bananas", "Oranges", "Lemons", "Berries", "Grapes", "Avocados",
            "Tomatoes", "Cucumbers", "Bell peppers", "Carrots", "Potatoes", "Onions", "Garlic", "Lettuce",
            "Spinach", "Broccoli", "Zucchini", "Mushrooms", "Cabbage", "Sweet potatoes",
        ),
        BAKERY to listOf("Bread", "Bread rolls", "Toast bread", "Flatbread", "Pita bread"),
        DRY_GOODS to listOf(
            "Rolled oats", "Muesli", "Cornflakes", "Rice", "Pasta", "Couscous", "Quinoa", "Bulgur",
            "Lentils", "Dried beans", "Dried peas", "Almonds", "Walnuts", "Cashews", "Sunflower seeds",
            "Chia seeds",
        ),
        CANNED_AND_JARS to listOf(
            "Chopped tomatoes", "Tomato paste", "Passata", "Sweetcorn", "Kidney beans", "Chickpeas",
            "Applesauce", "Pickles", "Jam", "Peanut butter",
        ),
        OILS_SPICES_AND_SAUCES to listOf(
            "Olive oil", "Rapeseed oil", "Vinegar", "Salt", "Pepper", "Paprika", "Curry powder", "Cinnamon",
            "Mustard", "Ketchup", "Soy sauce", "Vegan mayonnaise", "Vegetable stock",
        ),
        BAKING to listOf("Flour", "Sugar", "Baking powder", "Yeast", "Vanilla sugar", "Cocoa", "Cornstarch"),
        SWEETS_AND_SNACKS to listOf(
            "Vegan chocolate", "Vegan cookies", "Potato crisps", "Popcorn", "Pretzels", "Dried fruit",
        ),
        BEVERAGES to listOf("Water", "Fruit juice", "Lemonade", "Coffee", "Tea", "Sparkling water"),
        CHILLED to listOf(
            "Oat milk", "Soy milk", "Almond milk", "Plant-based yogurt", "Tofu", "Tempeh", "Vegan margarine",
            "Vegan spread", "Fresh pasta", "Vegan cream", "Vegan cheese", "Smoked tofu",
        ),
        FROZEN to listOf(
            "Frozen vegetables", "Frozen berries", "Fries", "Vegan pizza", "Vegan ice cream", "Frozen spinach",
            "Edamame", "Vegan nuggets",
        ),
        DRUGSTORE_AND_HOUSEHOLD to listOf(
            "Toilet paper", "Tissues", "Kitchen roll", "Soap", "Shampoo", "Toothpaste", "Laundry detergent",
            "Dish soap", "Trash bags", "Sponges", "Cleaning products", "Dental floss", "Deodorant",
            "Dishwasher tablets",
        ),
        NON_FOOD to listOf("Batteries", "Light bulbs", "Candles", "Aluminium foil", "Baking paper"),
    ),
)