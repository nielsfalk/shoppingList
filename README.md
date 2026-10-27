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

## Architecture

The app is a client-only static single page app written in **Kotlin/JS** with **Compose HTML** and hosted on **GitHub Pages**.
There is no backend: all user data lives in the browser's `localStorage`, all language data is shipped as static files.

### Tech stack

| Area               | Choice                                                                    |
|--------------------|---------------------------------------------------------------------------|
| Language / build   | Kotlin Multiplatform Gradle plugin with `js(IR) { browser() }`            |
| UI                 | Compose HTML (`org.jetbrains.compose.html:html-core`), renders real DOM   |
| Persistence        | `localStorage` + `kotlinx.serialization` (JSON)                           |
| Dates / calendar   | `kotlinx-datetime`                                                        |
| i18n / data files  | static JSON per language (UI texts, initial list, autocomplete items)     |
| Autocompletion     | HTML `<datalist>`                                                         |
| Offline            | `manifest.webmanifest` + small service worker (PWA)                       |
| Tests              | `kotlin("test")` in `jsTest`                                              |
| Hosting            | GitHub Pages, deployed by GitHub Actions                                  |

### Constraints of GitHub Pages

- **Static files only**: the app is the output of `./gradlew jsBrowserDistribution`.
- **Hash routing**: there is no server-side rewriting, so routes look like `#/`, `#/history/2026-10-27`, `#/default`.
- **Relative paths only**: the app is served under a subpath (`https://nielsfalk.github.io/shoppingList/`), so scripts, data files, manifest and service worker are referenced relatively.
- **Shared origin**: all pages under `nielsfalk.github.io` share one `localStorage`, so every key is prefixed with `shoppingList:`.

### Project structure

```
src/jsMain/kotlin/
  model/      pure logic: daily list, sort order, diff to the original default list
  storage/    localStorage access + JSON serialization (key prefix "shoppingList:")
  i18n/       language selection from navigator.language, fallback "en"
  routing/    tiny hash router (window.onhashchange)
  ui/         Compose HTML screens: DailyList, History (calendar), DefaultList
src/jsMain/resources/
  index.html, manifest.webmanifest, sw.js, icons/
  i18n/de.json, i18n/en.json
src/jsTest/kotlin/
  tests for model and storage
.github/workflows/pages.yml
```

### Storage keys

| Key                              | Content                                                         |
|----------------------------------|-----------------------------------------------------------------|
| `shoppingList:defaultList`       | the user's default list, only present if it differs from the original |
| `shoppingList:day:<yyyy-MM-dd>`  | the daily list of that day (items with checked state and order) |

### Build and deployment

- Development server: `./gradlew jsBrowserDevelopmentRun --continuous`
- Production build: `./gradlew jsBrowserDistribution` (output in `build/dist/js/productionExecutable`)
- Deployment: on every push to `main` a GitHub Actions workflow builds the distribution and publishes it with `actions/upload-pages-artifact` and `actions/deploy-pages` (Settings → Pages → Source: "GitHub Actions").
