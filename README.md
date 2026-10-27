# shoppingList

This is a Singe page app for shopping grocery. It uses local storage only and is selecting a language based on the user's browser settings.

## Features

### Initial shopping list

There are initial shopping list for each language in a datafile.
In german the initial list contains
- Klopapier
- Wasser
- Senf
- Hafermilch
- Äpfel
- Haferflockoen

### History

There is a calendar view to browse to older lists. You can have at max one list per day.

### Daily list

Items on the daily list can be checked with one tab or deleted on a button. The sortorder is Unchecked first than original order. In the end there is a textfied for new Items with autocompletion for likely items (also in a data file). There are two add button for it. Add to daily list and add to default list.

### Default list

a default list is only stored in locale storage if there is a diff to the original list. The order in defalt lists can be changed.
