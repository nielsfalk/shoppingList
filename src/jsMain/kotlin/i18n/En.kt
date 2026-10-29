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
    suggestionsByDepartment = mapOf(
        Department.FRUIT_AND_VEGETABLES to listOf(
            "Apples", "Pears", "Bananas", "Oranges", "Lemons", "Berries", "Grapes", "Avocados",
            "Tomatoes", "Cucumbers", "Bell peppers", "Carrots", "Potatoes", "Onions", "Garlic", "Lettuce",
            "Spinach", "Broccoli", "Zucchini", "Mushrooms", "Cabbage", "Sweet potatoes",
        ),
        Department.BAKERY to listOf("Bread", "Bread rolls", "Toast bread", "Flatbread", "Pita bread"),
        Department.DRY_GOODS to listOf(
            "Rolled oats", "Muesli", "Cornflakes", "Rice", "Pasta", "Couscous", "Quinoa", "Bulgur",
            "Lentils", "Dried beans", "Dried peas", "Almonds", "Walnuts", "Cashews", "Sunflower seeds",
            "Chia seeds",
        ),
        Department.CANNED_AND_JARS to listOf(
            "Chopped tomatoes", "Tomato paste", "Passata", "Sweetcorn", "Kidney beans", "Chickpeas",
            "Applesauce", "Pickles", "Jam", "Peanut butter",
        ),
        Department.OILS_SPICES_AND_SAUCES to listOf(
            "Olive oil", "Rapeseed oil", "Vinegar", "Salt", "Pepper", "Paprika", "Curry powder", "Cinnamon",
            "Mustard", "Ketchup", "Soy sauce", "Vegan mayonnaise", "Vegetable stock",
        ),
        Department.BAKING to listOf("Flour", "Sugar", "Baking powder", "Yeast", "Vanilla sugar", "Cocoa", "Cornstarch"),
        Department.SWEETS_AND_SNACKS to listOf(
            "Vegan chocolate", "Vegan cookies", "Potato crisps", "Popcorn", "Pretzels", "Dried fruit",
        ),
        Department.BEVERAGES to listOf("Water", "Fruit juice", "Lemonade", "Coffee", "Tea", "Sparkling water"),
        Department.CHILLED to listOf(
            "Oat milk", "Soy milk", "Almond milk", "Plant-based yogurt", "Tofu", "Tempeh", "Vegan margarine",
            "Vegan spread", "Fresh pasta", "Vegan cream", "Vegan cheese", "Smoked tofu",
        ),
        Department.FROZEN to listOf(
            "Frozen vegetables", "Frozen berries", "Fries", "Vegan pizza", "Vegan ice cream", "Frozen spinach",
            "Edamame", "Vegan nuggets",
        ),
        Department.DRUGSTORE_AND_HOUSEHOLD to listOf(
            "Toilet paper", "Tissues", "Kitchen roll", "Soap", "Shampoo", "Toothpaste", "Laundry detergent",
            "Dish soap", "Trash bags", "Sponges", "Cleaning products", "Dental floss", "Deodorant",
            "Dishwasher tablets",
        ),
        Department.NON_FOOD to listOf("Batteries", "Light bulbs", "Candles", "Aluminium foil", "Baking paper"),
    ),
)