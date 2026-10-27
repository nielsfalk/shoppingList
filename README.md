# shoppingList

This is a single page app for grocery shopping. It uses local storage only and selects a language based on the user's browser settings.

## Features

### Initial shopping list

There is an initial shopping list for each language in a data file.
In German the initial list contains
- Klopapier
- Wasser
- Senf
- Hafermilch
- Äpfel
- Haferflocken

### History

There is a calendar view to browse older lists. You can have at most one list per day.

### Daily list

Items on the daily list can be checked with one tap or deleted with a button. The sort order is unchecked first, then original order. At the end there is a text field for new items with autocompletion for likely items (also in a data file). There are two add buttons for it: "add to daily list" and "add to default list".

### Default list

A default list is only stored in local storage if it differs from the original list. The order of the default list can be changed.

### Language switch

The language is preselected from the browser settings. A language switch in the navigation bar lets the user choose another supported language (e.g. `de`, `en`). The choice is stored in local storage and takes precedence over the browser setting. Switching the language changes the UI texts, the autocomplete items and the original default list; already stored daily lists and a customized default list are kept unchanged.

### Cyberpunk design

The app has a dark cyberpunk look, inspired by neon signs at night:

- **Colors**: near-black background (`#0a0a12`), neon magenta (`#ff2bd6`) and cyan (`#00f0ff`) as accent colors, acid yellow (`#f5ff00`) for warnings and the delete button.
- **Neon glow**: headings, borders and focused elements glow via `text-shadow` / `box-shadow` in the accent colors.
- **Typography**: monospace / futuristic font (e.g. `Orbitron` for headings, `Share Tech Mono` for text, bundled locally), uppercase labels with letter spacing.
- **Shapes**: sharp edges, cut corners (`clip-path`) on buttons and cards, thin neon outlines instead of filled surfaces.
- **Effects**: subtle scanline overlay and a short glitch animation on the app title; a checked item gets a neon strike-through and fades to a dim color.
- **Calendar**: days with a stored list are highlighted with a cyan glow, today with a magenta frame.
- **Accessibility**: contrast stays at least WCAG AA, and all animations are disabled with `prefers-reduced-motion`.

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
| Styling            | plain CSS (`styles.css`) with CSS custom properties for the cyberpunk theme |

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
  i18n/       language selection: stored choice, else navigator.language, fallback "en"
  routing/    tiny hash router (window.onhashchange)
  ui/         Compose HTML screens: DailyList, History (calendar), DefaultList
src/jsMain/resources/
  index.html, styles.css, manifest.webmanifest, sw.js, icons/, fonts/
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
| `shoppingList:language`          | the language chosen with the language switch, only present if chosen manually |

### Build and deployment

- Development server: `./gradlew jsBrowserDevelopmentRun --continuous`
- Production build: `./gradlew jsBrowserDistribution` (output in `build/dist/js/productionExecutable`)
- Deployment: on every push to `main` a GitHub Actions workflow builds the distribution and publishes it with `actions/upload-pages-artifact` and `actions/deploy-pages` (Settings → Pages → Source: "GitHub Actions").

## Tasks

Each task is small, can be completed on its own and leaves the app in a working state.

### Setup

- [ ] **T01** Switch `build.gradle.kts` from `kotlin("jvm")` to Kotlin Multiplatform with `js(IR) { browser() }`, add Compose HTML, `kotlinx.serialization` and `kotlinx-datetime`.
- [ ] **T02** Add `index.html` and a `main()` that renders "Hello shoppingList" with Compose HTML; verify with `./gradlew jsBrowserDevelopmentRun`.
- [ ] **T03** Add `.github/workflows/pages.yml` that builds `jsBrowserDistribution` and deploys it to GitHub Pages.
- [ ] **T04** Verify the deployed page loads under `https://nielsfalk.github.io/shoppingList/` (relative paths only).

### Model

- [ ] **T05** Create `Item` (name, checked) and `DailyList` (date, items) data classes with `@Serializable`.
- [ ] **T06** Implement the sort order "unchecked first, then original order" with tests.
- [ ] **T07** Implement toggling the checked state and deleting an item with tests.
- [ ] **T08** Implement adding an item to the daily list and to the default list (no duplicates) with tests.
- [ ] **T09** Implement moving an item up/down in the default list with tests.
- [ ] **T10** Implement the diff check "default list differs from the original list" with tests.

### i18n and data files

- [ ] **T11** Add `i18n/de.json` with UI texts, the initial list and autocomplete items.
- [ ] **T12** Add `i18n/en.json` with the same structure.
- [ ] **T13** Select the language from `navigator.language` with fallback `en`; load the JSON file relatively.
- [ ] **T32** Store a manually chosen language under `shoppingList:language` and prefer it over `navigator.language`, with tests.
- [ ] **T33** Add a language switch to the navigation bar that changes the language without a page reload.

### Storage

- [ ] **T14** Implement a `localStorage` wrapper with the key prefix `shoppingList:` and JSON (de)serialization.
- [ ] **T15** Save and load the daily list under `shoppingList:day:<yyyy-MM-dd>`.
- [ ] **T16** Save the default list under `shoppingList:defaultList` only if it differs from the original; remove the key otherwise.
- [ ] **T17** Create today's list from the default list when no list exists for today.

### Routing

- [ ] **T18** Implement a tiny hash router for `#/`, `#/default` and `#/history/<yyyy-MM-dd>`.
- [ ] **T19** Add a navigation bar linking daily list, default list and history.

### UI: Daily list

- [ ] **T20** Render today's list sorted; check/uncheck an item with one tap.
- [ ] **T21** Add a delete button per item.
- [ ] **T22** Add the text field with `<datalist>` autocompletion.
- [ ] **T23** Add the buttons "add to daily list" and "add to default list".

### UI: Default list

- [ ] **T24** Render the default list with up/down buttons to change the order.
- [ ] **T25** Allow deleting items from the default list.

### UI: History

- [ ] **T26** Render a month calendar grid with previous/next month navigation.
- [ ] **T27** Mark days that have a stored list; open a day's list via `#/history/<yyyy-MM-dd>`.

### PWA

- [ ] **T28** Add `manifest.webmanifest` and icons so the app can be installed.
- [ ] **T29** Add a service worker that caches the app files for offline use.

### Polish

- [ ] **T30** Add mobile-first styling.
- [ ] **T34** Add `styles.css` with the cyberpunk color palette as CSS custom properties and bundle the fonts locally.
- [ ] **T35** Style buttons, inputs, list items and the navigation bar with neon outlines, glow and cut corners.
- [ ] **T36** Add the scanline overlay and the glitch animation for the title, disabled with `prefers-reduced-motion`.
- [ ] **T37** Style the calendar (stored days, today) and checked items in the cyberpunk theme; verify WCAG AA contrast.
- [x] **T31** Fix typos in this README.
