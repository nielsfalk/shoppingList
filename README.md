# shoppingList

[![Deploy to GitHub Pages](https://github.com/nielsfalk/shoppingList/actions/workflows/pages.yml/badge.svg)](https://github.com/nielsfalk/shoppingList/actions/workflows/pages.yml)

This is a single page app for grocery shopping. It runs entirely in the browser, stores all data in local storage and is available in several languages.

**Live app:** https://nielsfalk.github.io/shoppingList/

**Status:** the app is deployed to GitHub Pages; implementation is current through **T62** (see [Tasks](#tasks)).

## Getting started

Requirements: JDK 17 for compilation, JDK 21 for the Gradle daemon (see `gradle/gradle-daemon-jvm.properties`) and a current browser.

```
git clone https://github.com/nielsfalk/shoppingList.git
cd shoppingList
./gradlew jsBrowserDevelopmentRun --continuous
```

## Glossary

| Term             | Meaning                                                                                    |
|------------------|--------------------------------------------------------------------------------------------|
| **Initial list** | the shopping list of a language, built into the app; it is never changed by the user      |
| **Default list** | the user's own version of the initial list; equal to the initial list until it is changed |
| **Daily list**   | the shopping list of one day; there is at most one daily list per day                     |
| **Suggestions**  | the likely items per language, used for autocompletion, grouped by supermarket department |

## Features

### Daily list

- **Creation**: when the app is opened on a day without a daily list, the daily list is created as a copy of the current default list. "Today" is the local date of the device.
- **Reset**: a "reset to default" button replaces today's list with the current default list, with all items unchecked.
- **Add item**: a "+" button next to the daily list header focuses the item input.
- **Check**: one tap checks an item, another tap unchecks it.
- **Delete**: every item has a `✕` button with an accessible label.
- **Departments**: unchecked items are grouped by supermarket department with the translated department name as a heading, in the walking order of the departments; unchecked items that are not in the suggestions are shown in a last group ("Other"/"Sonstiges"). Empty departments are not shown. Completed (checked) items are shown below all departments with a heading ("Completed"/"Erledigt"), sorted by suggestion order.
- **Sort order**: unchecked items first (grouped by supermarket departments), followed by checked items below all departments; items follow the suggestion order within both groups, and items that are not in the suggestions come last in their list order.
- **Suggestions**: autocompletion combines the language suggestions (all generic supermarket items, vegan and non-vegan) with items from the default list that are not already in today's list, without duplicates.
- **Add**: at the end there is a text field and two icon buttons: "+" adds the item to today's list, and "★" adds it to the default list **and** to today's list. Both buttons have descriptive accessible labels.
- **Keyboard**: pressing Enter in the item text field adds a non-blank item to today's list and clears the field.
- **Duplicates**: names are compared trimmed and case-insensitively. Adding an item that is already on the list does not create a second entry; if the existing item is checked, it becomes unchecked again.

### Default list

- The default list starts as the initial list of the current language.
- Items can be deleted with a `✕` button. There is no manual sorting: new items are appended at the end, and the daily list is sorted by the suggestion order.
- It is only stored in local storage if it differs from the initial list; otherwise the initial list of the current language is used.
- A "↻" button (with an accessible label) resets the default list to the initial list of the current language; it is only enabled when the default list differs from the initial list.
- The default-list text field and "★" button add an item only to the default list, following the duplicate rule; the daily-list "add to default list" action adds to both lists.

### Initial list

There is an initial list for each language in the [language data](#language-data), sorted by the suggestion order (supermarket departments).

| German (`de`)        | English (`en`)     |
|----------------------|--------------------|
| Äpfel                | Apples             |
| Bananen              | Bananas            |
| Tomaten              | Tomatoes           |
| Karotten             | Carrots            |
| Salat                | Lettuce            |
| Brot                 | Bread              |
| Haferflocken         | Rolled oats        |
| Nudeln               | Pasta              |
| Senf                 | Mustard            |
| Vegane Schokolade    | Vegan chocolate    |
| Wasser               | Water              |
| Hafermilch           | Oat milk           |
| Pflanzlicher Joghurt | Plant-based yogurt |
| Tofu                 | Tofu               |
| Vegane Margarine     | Vegan margarine    |
| Veganer Käse         | Vegan cheese       |
| Vegane Pizza         | Vegan pizza        |
| Toilettenpapier      | Toilet paper       |

### History

- A calendar view shows all days that have a daily list.
- Selecting a day opens its list **read-only**.
- A button "copy to today" adds all items of the old list to today's list as unchecked items, following the duplicate rule above.
- Old daily lists are kept forever.

### Language switch

- On the first start the language is taken from the browser settings (`navigator.language`); if it is not supported, English (`en`) is used.
- A language switch in the navigation bar lets the user choose another supported language (`de`, `en`). The choice is stored in local storage and takes precedence over the browser setting.
- Switching the language changes the UI texts, the suggestions and the initial list. Stored daily lists and a customized default list are kept unchanged (they are not translated).

### Cyberpunk design

The app has a dark cyberpunk look, inspired by neon signs at night:

- **Colors**: near-black background (`#0a0a12`), neon magenta (`#ff2bd6`) and cyan (`#00f0ff`) as accent colors; acid yellow (`#f5ff00`) is reserved for destructive actions (delete buttons). Warnings and errors use magenta.
- **Neon glow**: headings, borders and focused elements glow via `text-shadow` / `box-shadow` in the accent colors.
- **Typography**: `Orbitron` for headings and the title only, `Share Tech Mono` for all other text; uppercase labels with letter spacing. Both fonts are licensed under the SIL Open Font License 1.1, downloaded from Google Fonts and bundled locally so they work offline.
- **Shapes**: sharp edges, cut corners (`clip-path`) on buttons and cards, thin neon outlines instead of filled surfaces.
- **Effects**: subtle scanline overlay and a short glitch animation on the app title; a checked item gets a neon strike-through and fades to a dim color.
- **Calendar**: days with a stored list are highlighted with a cyan glow, today with a magenta frame.
- **Accessibility**: contrast is at least WCAG AA, tap targets are at least 44 × 44 px, keyboard focus is always visible, and all animations are disabled with `prefers-reduced-motion`.

### Non-goals

- no user accounts and no login
- no sync between devices
- no backend or server-side code

## Architecture

The app is a client-only static single page app written in **Kotlin/JS** with **Compose HTML** and hosted on **GitHub Pages**.
There is no backend: all user data lives in the browser's `localStorage`, all language data is compiled into the app as Kotlin code.

### Tech stack

| Area              | Choice                                                                      |
|-------------------|-----------------------------------------------------------------------------|
| Language / build  | Kotlin Multiplatform Gradle plugin with `js { browser() }`                  |
| UI                | Compose HTML (`org.jetbrains.compose.html:html-core`), renders real DOM     |
| Persistence       | `localStorage` + `kotlinx.serialization` (JSON)                             |
| Dates / calendar  | `kotlinx-datetime`                                                          |
| i18n              | Kotlin object per language (UI texts, initial list, suggestions)            |
| Autocompletion    | HTML `<datalist>`                                                           |
| Offline           | `manifest.webmanifest` + small service worker (PWA)                         |
| Styling           | plain CSS (`styles.css`) with CSS custom properties for the cyberpunk theme |
| Tests             | Kotest `FreeSpec`, Kotlin power asserts and the Kotlin Datatable Plugin in `jsTest` |
| Hosting           | GitHub Pages, deployed by GitHub Actions                                    |

### Constraints of GitHub Pages

- **Static files only**: the app is the output of `./gradlew jsBrowserDistribution`.
- **Hash routing**: there is no server-side rewriting, so routes look like `#/`, `#/history/2026-10-27`, `#/default`.
- **Relative paths only**: the app is served under a subpath (`https://nielsfalk.github.io/shoppingList/`), so scripts, manifest, icons and service worker are referenced relatively.
- **Shared origin**: all pages under `nielsfalk.github.io` share one `localStorage`, so every key is prefixed with `shoppingList:`.

### Project structure

```
src/jsMain/kotlin/
  model/      pure logic: daily list, sort order, add/duplicate rules, diff to the initial list
  storage/    localStorage access + JSON serialization (key prefix "shoppingList:")
  i18n/       language data per language (De.kt, En.kt) + language selection: stored choice, else navigator.language, fallback "en"
  routing/    tiny hash router (window.onhashchange)
  ui/         Compose HTML screens: DailyList, History (calendar), DefaultList
src/jsMain/resources/
  index.html, styles.css, manifest.webmanifest, sw.js, icons/, fonts/
src/jsTest/kotlin/
  tests for model and storage
.github/workflows/pages.yml
```

### Language data

The UI texts, the initial list and the suggestions of each language are plain Kotlin objects in `src/jsMain/kotlin/i18n/`. They are part of the app bundle, so they are available immediately (no loading state) and work offline without extra caching. All texts are constructor parameters without default values, so a missing translation fails the build.

```kotlin
import model.Department

data class UiTexts(
    val dailyList: String,
    val defaultList: String,
    val history: String,
    val addToDailyList: String,
    val addToDefaultList: String,
    val delete: String,
    val copyToToday: String,
    val departmentNames: Map<Department, String>,
)

data class LanguageData(
    val ui: UiTexts,
    val initialList: List<String>,
    val suggestionsByDepartment: Map<Department, List<String>>,
) {
    val suggestions: List<String> = Department.entries.flatMap { suggestionsByDepartment[it].orEmpty() }
}
```

The flat `suggestions` list is derived in department order and is passed to the `<datalist>` used for autocompletion. Every department has its own non-empty list of generic items (no brand names), vegan and non-vegan; the initial list only contains vegan items.

Example `i18n/De.kt`:

```kotlin
import model.Department.*

val De = LanguageData(
    ui = UiTexts(
        dailyList = "Tagesliste",
        defaultList = "Standardliste",
        history = "Verlauf",
        addToDailyList = "Zur Tagesliste",
        addToDefaultList = "Zur Standardliste",
        delete = "Löschen",
        copyToToday = "In heutige Liste übernehmen",
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
    initialList = listOf("Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken"),
    suggestionsByDepartment = mapOf(
        FRUIT_AND_VEGETABLES to listOf("Äpfel", "Bananen", "Tomaten"),
        BAKERY to listOf("Brot"),
        DRY_GOODS to listOf("Haferflocken", "Linsen", "Nudeln"),
        CANNED_AND_JARS to listOf("Kichererbsen"),
        OILS_SPICES_AND_SAUCES to listOf("Senf"),
        BAKING to listOf("Mehl"),
        SWEETS_AND_SNACKS to listOf("Vegane Kekse"),
        BEVERAGES to listOf("Wasser"),
        CHILLED to listOf("Vegane Margarine", "Hafermilch", "Tofu"),
        FROZEN to listOf("Tiefkühlgemüse"),
        DRUGSTORE_AND_HOUSEHOLD to listOf("Klopapier"),
        NON_FOOD to listOf("Batterien"),
    ),
)
```

The supported languages are registered in one place, e.g. `val languages = mapOf("de" to De, "en" to En)`.

### Storage keys

| Key                             | Content                                                                        |
|---------------------------------|--------------------------------------------------------------------------------|
| `shoppingList:schemaVersion`    | version of the stored data format (starts with `1`), used for migrations      |
| `shoppingList:defaultList`      | the user's default list, only present if it differs from the initial list     |
| `shoppingList:day:<yyyy-MM-dd>` | the daily list of that day (items with checked state, in list order)          |
| `shoppingList:language`         | the language chosen with the language switch, only present if chosen manually |

Example value of `shoppingList:day:2026-10-27`:

```json
{
  "date": "2026-10-27",
  "items": [
    { "name": "Wasser", "checked": false },
    { "name": "Senf", "checked": true }
  ]
}
```

### Data safety

There is no backup, so the app protects the stored data as well as the browser allows:

- The app calls `navigator.storage.persist()` so the browser does not evict the data under storage pressure.
- Safari on iOS deletes `localStorage` of websites that were not visited for 7 days. This does not apply to apps installed to the home screen, so iOS users should install the app (PWA).

### Updates

The service worker caches the app files under a versioned cache name (e.g. `shoppingList-<build version>`). A new deployment creates a new cache, removes the old one and reloads the app once the new version is active, so users do not stay on an old version.

### Build and deployment

- Development server: `./gradlew jsBrowserDevelopmentRun --continuous`
- Production build: `./gradlew jsBrowserDistribution` (output in `build/dist/js/productionExecutable`)
- Deployment: on every push to `main` or `master`, a GitHub Actions workflow builds the distribution and publishes it with `actions/upload-pages-artifact` and `actions/deploy-pages` (Settings → Pages → Source: "GitHub Actions").

## Tasks

Each task is small, can be completed on its own and leaves the app in a working state. The tasks are numbered in the suggested order; dependencies across groups are noted.

**Definition of done**: tests are green, the change is deployed to GitHub Pages, and this README is updated if behavior changed.

### Setup

- [x] **T01** Switch `build.gradle.kts` from `kotlin("jvm")` to Kotlin Multiplatform with `js { browser() }`, add Compose HTML, `kotlinx.serialization` and `kotlinx-datetime`.
- [x] **T02** Add `index.html` and a `main()` that renders "Hello shoppingList" with Compose HTML; verify with `./gradlew jsBrowserDevelopmentRun`.
- [x] **T03** Add `.github/workflows/pages.yml` that builds `jsBrowserDistribution` and deploys it to GitHub Pages.
- [x] **T04** Verify the deployed page loads under `https://nielsfalk.github.io/shoppingList/` (relative paths only).

### Model

- [x] **T05** Create `Item` (name, checked) and `DailyList` (date, items) data classes with `@Serializable`.
- [x] **T06** Implement the sort order "unchecked first, then list order" with tests.
- [x] **T07** Implement toggling the checked state and deleting an item with tests.
- [x] **T08** Implement adding an item to the daily list with the duplicate rule (trimmed, case-insensitive; a checked duplicate gets unchecked) with tests.
- [x] **T09** Implement "add to default list" (adds to the default list and to today's list) with tests.
- [x] **T10** Implement moving an item up/down and deleting an item in the default list with tests.
- [x] **T11** Implement the diff check "default list differs from the initial list" with tests.
- [x] **T12** Create today's list as a copy of the default list when no list exists for today (local date) with tests.
- [x] **T13** Implement "copy to today" (add all items of an old list unchecked, duplicate rule applies) with tests.

### i18n and language data

- [x] **T14** Add `UiTexts`, `LanguageData` and the German language data `i18n/De.kt` with UI texts, the initial list and vegan-only suggestions (see [Language data](#language-data)).
- [x] **T15** Add `i18n/En.kt` with the same structure (vegan-only suggestions).
- [x] **T16** Select the language from `navigator.language` with fallback `en` and look up its `LanguageData`, with tests.
- [x] **T17** Store a manually chosen language under `shoppingList:language` and prefer it over `navigator.language`, with tests.

### Storage

- [x] **T18** Implement a `localStorage` wrapper with the key prefix `shoppingList:` and JSON (de)serialization.
- [x] **T19** Write `shoppingList:schemaVersion` on first start and add a hook for future migrations.
- [x] **T20** Save and load the daily list under `shoppingList:day:<yyyy-MM-dd>`.
- [x] **T21** Save the default list under `shoppingList:defaultList` only if it differs from the initial list; remove the key otherwise.
- [x] **T22** Request persistent storage with `navigator.storage.persist()` on start.

### Routing

- [x] **T23** Implement a tiny hash router for `#/`, `#/default` and `#/history/<yyyy-MM-dd>`.
- [x] **T24** Add a navigation bar linking daily list, default list and history.
- [x] **T25** Add a language switch to the navigation bar that changes the language without a page reload (depends on T17, T24).

### UI: Daily list

- [x] **T26** Render today's list sorted; check/uncheck an item with one tap.
- [x] **T27** Add a delete button per item.
- [x] **T28** Add the text field with `<datalist>` autocompletion from the suggestions.
- [x] **T29** Add the buttons "add to daily list" and "add to default list".

### UI: Default list

- [x] **T30** Render the default list with up/down buttons to change the order.
- [x] **T31** Add a delete button per item of the default list.

### UI: History

- [x] **T32** Render a month calendar grid with previous/next month navigation.
- [x] **T33** Mark days that have a stored list; open a day's list read-only via `#/history/<yyyy-MM-dd>`.
- [x] **T34** Add the button "copy to today" to a past day's list.

### PWA

- [x] **T35** Add `manifest.webmanifest` and icons so the app can be installed.
- [x] **T36** Add a service worker that caches the app files (including the bundled language data) for offline use.
- [x] **T37** Use a versioned cache in the service worker, remove old caches and reload the app after an update.

### Design

- [x] **T38** Add `styles.css` with a mobile-first base layout, the cyberpunk color palette as CSS custom properties and the locally bundled fonts.
- [x] **T39** Style buttons, inputs, list items and the navigation bar with neon outlines, glow and cut corners; tap targets at least 44 × 44 px, visible focus.
- [x] **T40** Add the scanline overlay and the glitch animation for the title, disabled with `prefers-reduced-motion`.
- [x] **T41** Style the calendar (stored days, today) and checked items in the cyberpunk theme; verify WCAG AA contrast.

### Documentation

- [x] **T42** Fix typos in this README.

### UI: Daily list improvements

- [x] **T43** Add a "reset to default" button with a refresh icon to the daily list; it replaces today's list with an unchecked copy of the current default list.
- [x] **T44** Also suggest the items of the default list that are not on today's list in the `<datalist>` autocompletion (no duplicates with the language suggestions).
- [x] **T45** Add a button to the daily list that adds a new item to today's list (it opens/focuses the text field).
- [x] **T46** Replace the text of the delete buttons with an icon; keep the text as `aria-label`.
- [x] **T47** Replace the text of the buttons "add to daily list" and "add to default list" with icons; keep the texts as `aria-label`.
- [x] **T48** Pressing Enter (new line) in the text field adds the item to today's list and clears the field.

### UI: Default list improvements

- [x] **T49** Add a text field and a "★" button to the default list that add the item to the default list only (duplicate rule applies); keep the text as `aria-label`.

### Testing

- [x] **T50** Refactor all tests in `src/jsTest/kotlin/` to Kotest `FreeSpec` with Kotlin power asserts and data tables:
  - Add Kotest (`FreeSpec`, JS-compatible engine) and the `org.jetbrains.kotlin.plugin.power-assert` plugin (same version as Kotlin); use plain `assert(condition)` instead of `assertEquals`/`assertTrue`.
  - Add the [Kotlin Datatable Plugin](https://github.com/nielsfalk/kotlin-datatable-plugin) (`io.github.nielsfalk.datatable`, see its KMP sample) and make the test compilation depend on `generateDataTables`.
  - Turn similar tests that only differ in their data into `@Data(...)` tables with `.each { ... }` (separate inputs from expected values with `ǀǀ`), and run `./gradlew formatDataTables` afterwards.
  - Separate given (optional), when and then with empty lines; no comments or labels for these blocks.
  - Keep the test coverage the same (no tests dropped), keep `./gradlew jsTest` green, and update the tech stack table in this README.

### Supermarket departments

- [x] **T51** Add an enum `Department` in `model/` with the supermarket departments in walking order through a typical supermarket, and a translated name per department in `UiTexts` (or `LanguageData`) for `de` and `en`, with tests that every department has a non-blank name in every language:
  1. `FRUIT_AND_VEGETABLES` (first)
  2. `BAKERY` (bread, rolls)
  3. `DRY_GOODS` (pasta, rice, grains, legumes, cereals, nuts)
  4. `CANNED_AND_JARS` (canned tomatoes, beans, pickles, spreads)
  5. `OILS_SPICES_AND_SAUCES` (oil, vinegar, salt, spices, mustard, ketchup)
  6. `BAKING` (flour, sugar, baking powder, yeast)
  7. `SWEETS_AND_SNACKS` (chocolate, crisps, cookies)
  8. `BEVERAGES` (water, juice, coffee, tea)
  9. `CHILLED` (all cooled products in one block: plant-based milk and yogurt, tofu, tempeh, vegan spreads, fresh pasta)
  10. `FROZEN` (all frozen products in one block: frozen vegetables, fruit, fries, pizza, ice cream)
  11. `DRUGSTORE_AND_HOUSEHOLD` (toilet paper, soap, toothpaste, detergent, dish soap, trash bags)
  12. `NON_FOOD` (all other non-food in one block: batteries, light bulbs, candles, aluminium foil)
- [x] **T52** Fill the suggestions of `de` and `en` with all generic items (no brand names) a supermarket usually sells, keeping the vegan-only rule for food. Store them per department (e.g. `suggestions: Map<Department, List<String>>` or a list of `(name, Department)`), ordered by department in the order of T51; derive the flat suggestion list for the `<datalist>` from it. Add tests that there are no duplicates (trimmed, case-insensitive) and no empty department, and update the [Language data](#language-data) section (depends on T51).
- [x] **T53** Add a lookup "department and position of an item name in the suggestion order" (trimmed, case-insensitive); unknown items (not in the suggestions) get no department and sort after all known items, keeping their list order. With data-table tests (depends on T52).
- [x] **T54** Add to the initial list of `de` and `en` everything a vegan person would usually buy (fruit, vegetables, bread, oats, legumes, rice, pasta, tofu, plant-based milk, nuts, spices, household basics, …); every item of the initial list must also be a suggestion. Update the [Initial list](#initial-list) table (depends on T52).
- [x] **T55** Sort the initial list of every language by the suggestion order, with a test that checks the order for all languages (depends on T53, T54).
- [x] **T56** Sort the daily list by "unchecked first, then suggestion order" (unknown items after the known ones in list order) instead of "unchecked first, then list order"; update the existing sort tests and the **Sort order** feature description (depends on T53).
- [x] **T57** Group the items of the daily list by department with the translated department name as a heading, in the department order of T51; items without a department go into a last group (e.g. "Other"/"Sonstiges"). Within each group the sort order of T56 applies; empty departments are not shown (depends on T51, T56).

### UI: Default list improvements (2)

- [x] **T58** Add a refresh button with a refresh icon (text as `aria-label`) to the default list that resets it to the initial list of the current language; the stored `shoppingList:defaultList` key is removed. The button is only enabled when the default list differs from the initial list.
- [x] **T59** Remove the manual sorting of the default list: drop the up/down buttons (T30) from `DefaultListScreen`, `DefaultList.moveUp`/`moveDown`, `AppState.moveDefaultItemUp`/`moveDefaultItemDown`, the `moveUp`/`moveDown` texts in `UiTexts`, `De.kt` and `En.kt`, the now unused `.move-button` styles and the move tests. Newly added items are still appended at the end; the daily list gets its order from the suggestion order (T56). Update the [Default list](#default-list) feature description.

### UI: Daily list improvements (2)

- [x] **T73** Render completed items below all departments in the daily list: show checked items in a separate section with a heading at the bottom below all department groups, sorted by the suggestion order (depends on T56, T57).

### Suggestions

- [x] **T60** Add all generic non-vegan items (no brand names) a supermarket usually sells to the suggestions of `de` and `en`, in their departments of T51 (e.g. meat, sausages, fish, milk, cheese, butter, yogurt, cream and eggs in `CHILLED`; frozen fish and meat in `FROZEN`; canned tuna and honey in `CANNED_AND_JARS`; gelatine in `BAKING`; milk chocolate and gummy bears in `SWEETS_AND_SNACKS`). Drop the vegan-only rule for suggestions (the initial list stays vegan), keep the no-duplicates and every-initial-item-is-a-suggestion tests green, and update the glossary, the **Suggestions** feature description and the [Language data](#language-data) section.
- [x] **T62** Replace the initial list of every language with the new curated 18-item vegan shopping list (Äpfel, Bananen, Tomaten, Karotten, Salat, Brot, Haferflocken, Nudeln, Senf, Vegane Schokolade, Wasser, Hafermilch, Pflanzlicher Joghurt, Tofu, Vegane Margarine, Veganer Käse, Vegane Pizza, Toilettenpapier), translated per language. Every item must remain a suggestion, the list must stay sorted by the suggestion order, and the [Initial list](#initial-list) table must be updated (depends on T54, T55).

### Maintenance

- [ ] **T61** Update all dependencies to their latest stable versions: Gradle wrapper, Kotlin (and the power-assert plugin), Compose, KSP, Kotest, the Kotlin Datatable Plugin, `kotlinx.serialization`, `kotlinx-datetime`, the npm/yarn lock in `kotlin-js-store`, and the GitHub Actions in `.github/workflows/pages.yml`. Keep `./gradlew build` green. Add a table to this README of every dependency that was **not** updated to the latest version, with its current version, the latest version and the reason (e.g. incompatible with the Kotlin version, breaks the Kotlin/JS browser tests).

### Stability & Bugfixes

- [x] **T63** Fix midnight date rollover: recompute/update `AppState.today` when the app regains focus or visibility (`visibilitychange` / `window.onfocus`) so long-running PWA sessions roll over to the new day automatically.
- [x] **T64** Make `Storage.get` deserialization fault-tolerant: wrap JSON deserialization in `runCatching` with fallback handling to prevent corrupt or incompatible `localStorage` entries from crashing app startup.
- [x] **T65** Optimize history storage scan: avoid scanning all `localStorage` keys on every Compose recomposition of the history screen by reading/caching `storedDays` only when needed.

### Refactoring: Domain & Type Safety

- [x] **T66** Introduce `@JvmInline value class ItemKey`: encapsulate trimmed case-insensitive name normalization in a dedicated value class (`ItemKey.of(name)`, `String.toItemKey()`, `Item.key`) to eliminate repetitive manual `normalizedName()` calls across the model.
- [x] **T67** Introduce typed `Language` enum: replace `String` language codes with `enum class Language(val code: String, val data: LanguageData)` including fallback selection (`select(stored, browser)`) to eliminate hardcoded language lists and runtime validation checks.

### Refactoring: UI & Architecture

- [x] **T68** Unify screen parameter signatures and extract `DailyListActions`: group `DailyListScreen`'s loose translation and callback parameters into `UiTexts` and a `DailyListActions` handler class, aligning its API with other screens.
- [x] **T69** Extract reusable `ItemNameInput` composable: deduplicate the text input field, Enter-key submission, blank check, and button rendering shared between `AddItemForm` and `DefaultListScreen`.
- [ ] **T70** (obsolete) Extract UI constants for CSS classes and glyphs: define `Styles` and `Glyphs` constants for CSS class names and symbol literals (`"☑"`, `"☐"`, `"↻"`, `"★"`, `"✕"`).
- [x] **T71** Centralize persistence and introduce repository abstractions: group storage free functions into repositories (`DailyListRepository`, `LanguageRepository`) and add an `updateDailyList` helper in `AppState` to eliminate repetitive state mutation and save pairs.
- [x] **T72** Replace untyped `dynamic` browser calls with typed external declarations: add typed `external` declarations for `ServiceWorker`, `StorageManager`, and DOM event listeners.

### Supermarket departments & Suggestions (2)

- [x] **T74** Realign supermarket departments with German supermarket layouts: ensure items like eggs and margarine are not classified under `CHILLED` (e.g. eggs in baking/ambient, margarine in oils/fats or baking) and adjust department definitions and item mappings accordingly (depends on T51, T60).
- [x] **T75** Duplicate all vegan-prefixed suggestions without the vegan prefix across all languages (e.g. "Vegane Schokolade" -> "Schokolade", "Veganer Käse" -> "Käse", "Vegane Margarine" -> "Margarine", "Vegane Pizza" -> "Pizza", "Veganer Joghurt" / "Pflanzlicher Joghurt" -> "Joghurt") so that both vegan and generic variants exist in suggestions (depends on T60, T62).
- [x] **T76** Implement aliases and common typo mappings for suggestions: support lookup mappings (e.g. "Kola" -> "Cola", "Mayo" -> "Mayonnaise", common misspellings like "Magerine" -> "Margarine") to resolve to canonical suggestions and their associated departments (depends on T53, T66).
- [ ] **T77** Add ~5,000 likely supermarket suggestions across all supported languages (e.g. Nüsse, Cola, Kekse, Saft, Apfelsaft, Balsamico, Skyr, Pudding, ...) and introduce any missing departments (depends on T52, T60, T74).
- [ ] **T78** Research online supermarket product catalogs to integrate missing product suggestions and missing departments across all supported languages (depends on T77).

### UI: Daily list improvements (3)

- [ ] **T79** Refocus and scroll to input field when pressing the top "+" button in the daily list header, ensuring the input field is re-focused and brought into view even if it was already focused.
- [ ] **T80** Add a confirmation dialog before resetting: prompt the user for confirmation when clicking the reset button on the daily list or default list to prevent accidental data loss (depends on T43, T58).
- [ ] **T81** Share daily list via URL parameter: add a share button encoding today's list into a shareable URL parameter; when opening a shared link, if the local daily list has modifications compared to the default list and is not already loaded, display a confirmation dialog before overwriting (depends on T26, T43).

## License

[MIT](LICENSE)
