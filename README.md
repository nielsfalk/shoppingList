# shoppingList

[![Deploy to GitHub Pages](https://github.com/nielsfalk/shoppingList/actions/workflows/pages.yml/badge.svg)](https://github.com/nielsfalk/shoppingList/actions/workflows/pages.yml)

This is a single page app for grocery shopping. It runs entirely in the browser, stores all data in local storage and is available in several languages.

**Live app:** https://nielsfalk.github.io/shoppingList/

**Status:** the Kotlin/JS app entry point is set up; the next step is **T03** (see [Tasks](#tasks)).

## Getting started

Requirements: JDK 17 and a current browser.

```
git clone https://github.com/nielsfalk/shoppingList.git
cd shoppingList
./gradlew jsBrowserDevelopmentRun --continuous
```

## Glossary

| Term             | Meaning                                                                                    |
|------------------|--------------------------------------------------------------------------------------------|
| **Initial list** | the shopping list shipped in the data file of a language; it is never changed by the user |
| **Default list** | the user's own version of the initial list; equal to the initial list until it is changed |
| **Daily list**   | the shopping list of one day; there is at most one daily list per day                     |
| **Suggestions**  | the likely items per language in a data file, used for autocompletion; vegan items only   |

## Features

### Daily list

- **Creation**: when the app is opened on a day without a daily list, the daily list is created as a copy of the current default list. "Today" is the local date of the device.
- **Check**: one tap checks an item, another tap unchecks it.
- **Delete**: every item has a delete button.
- **Sort order**: unchecked items first, then checked items; within both groups the items keep their position in the list (the order of the default list, newly added items at the end).
- **Add**: at the end there is a text field with autocompletion from the suggestions (vegan items only) and two buttons:
  - "add to daily list" adds the item to today's list.
  - "add to default list" adds the item to the default list **and** to today's list.
- **Duplicates**: names are compared trimmed and case-insensitively. Adding an item that is already on the list does not create a second entry; if the existing item is checked, it becomes unchecked again.

### Default list

- The default list starts as the initial list of the current language.
- Items can be deleted and their order can be changed with up/down buttons.
- It is only stored in local storage if it differs from the initial list; otherwise the initial list from the data file is used.
- Changes to the default list do not change today's list that has already been created (except "add to default list", which adds to both).

### Initial list

There is an initial list for each language in a data file.

| German (`de`) | English (`en`) |
|---------------|----------------|
| Klopapier     | Toilet paper   |
| Wasser        | Water          |
| Senf          | Mustard        |
| Hafermilch    | Oat milk       |
| Äpfel         | Apples         |
| Haferflocken  | Rolled oats    |

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
There is no backend: all user data lives in the browser's `localStorage`, all language data is shipped as static files.

### Tech stack

| Area              | Choice                                                                      |
|-------------------|-----------------------------------------------------------------------------|
| Language / build  | Kotlin Multiplatform Gradle plugin with `js { browser() }`                  |
| UI                | Compose HTML (`org.jetbrains.compose.html:html-core`), renders real DOM     |
| Persistence       | `localStorage` + `kotlinx.serialization` (JSON)                             |
| Dates / calendar  | `kotlinx-datetime`                                                          |
| i18n / data files | static JSON per language (UI texts, initial list, suggestions)              |
| Autocompletion    | HTML `<datalist>`                                                           |
| Offline           | `manifest.webmanifest` + small service worker (PWA)                         |
| Styling           | plain CSS (`styles.css`) with CSS custom properties for the cyberpunk theme |
| Tests             | `kotlin("test")` in `jsTest`                                                |
| Hosting           | GitHub Pages, deployed by GitHub Actions                                    |

### Constraints of GitHub Pages

- **Static files only**: the app is the output of `./gradlew jsBrowserDistribution`.
- **Hash routing**: there is no server-side rewriting, so routes look like `#/`, `#/history/2026-10-27`, `#/default`.
- **Relative paths only**: the app is served under a subpath (`https://nielsfalk.github.io/shoppingList/`), so scripts, data files, manifest and service worker are referenced relatively.
- **Shared origin**: all pages under `nielsfalk.github.io` share one `localStorage`, so every key is prefixed with `shoppingList:`.

### Project structure

```
src/jsMain/kotlin/
  model/      pure logic: daily list, sort order, add/duplicate rules, diff to the initial list
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

### Data files

The language files are loaded relatively with `fetch("i18n/<language>.json")` and cached by the service worker, so they also work offline. Example `i18n/de.json`:

```json
{
  "ui": {
    "dailyList": "Tagesliste",
    "defaultList": "Standardliste",
    "history": "Verlauf",
    "addToDailyList": "Zur Tagesliste",
    "addToDefaultList": "Zur Standardliste",
    "delete": "Löschen",
    "copyToToday": "In heutige Liste übernehmen"
  },
  "initialList": ["Klopapier", "Wasser", "Senf", "Hafermilch", "Äpfel", "Haferflocken"],
  "suggestions": ["Brot", "Margarine", "Sojamilch", "Tofu", "Linsen", "Bananen", "Tomaten", "Nudeln"]
}
```

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
- Deployment: on every push to `main` a GitHub Actions workflow builds the distribution and publishes it with `actions/upload-pages-artifact` and `actions/deploy-pages` (Settings → Pages → Source: "GitHub Actions").

## Tasks

Each task is small, can be completed on its own and leaves the app in a working state. The tasks are numbered in the suggested order; dependencies across groups are noted.

**Definition of done**: tests are green, the change is deployed to GitHub Pages, and this README is updated if behavior changed.

### Setup

- [x] **T01** Switch `build.gradle.kts` from `kotlin("jvm")` to Kotlin Multiplatform with `js { browser() }`, add Compose HTML, `kotlinx.serialization` and `kotlinx-datetime`.
- [x] **T02** Add `index.html` and a `main()` that renders "Hello shoppingList" with Compose HTML; verify with `./gradlew jsBrowserDevelopmentRun`.
- [ ] **T03** Add `.github/workflows/pages.yml` that builds `jsBrowserDistribution` and deploys it to GitHub Pages.
- [ ] **T04** Verify the deployed page loads under `https://nielsfalk.github.io/shoppingList/` (relative paths only).

### Model

- [ ] **T05** Create `Item` (name, checked) and `DailyList` (date, items) data classes with `@Serializable`.
- [ ] **T06** Implement the sort order "unchecked first, then list order" with tests.
- [ ] **T07** Implement toggling the checked state and deleting an item with tests.
- [ ] **T08** Implement adding an item to the daily list with the duplicate rule (trimmed, case-insensitive; a checked duplicate gets unchecked) with tests.
- [ ] **T09** Implement "add to default list" (adds to the default list and to today's list) with tests.
- [ ] **T10** Implement moving an item up/down and deleting an item in the default list with tests.
- [ ] **T11** Implement the diff check "default list differs from the initial list" with tests.
- [ ] **T12** Create today's list as a copy of the default list when no list exists for today (local date) with tests.
- [ ] **T13** Implement "copy to today" (add all items of an old list unchecked, duplicate rule applies) with tests.

### i18n and data files

- [ ] **T14** Add `i18n/de.json` with UI texts, the initial list and vegan-only suggestions (see [Data files](#data-files)).
- [ ] **T15** Add `i18n/en.json` with the same structure (vegan-only suggestions).
- [ ] **T16** Select the language from `navigator.language` with fallback `en`; load the JSON file relatively.
- [ ] **T17** Store a manually chosen language under `shoppingList:language` and prefer it over `navigator.language`, with tests.

### Storage

- [ ] **T18** Implement a `localStorage` wrapper with the key prefix `shoppingList:` and JSON (de)serialization.
- [ ] **T19** Write `shoppingList:schemaVersion` on first start and add a hook for future migrations.
- [ ] **T20** Save and load the daily list under `shoppingList:day:<yyyy-MM-dd>`.
- [ ] **T21** Save the default list under `shoppingList:defaultList` only if it differs from the initial list; remove the key otherwise.
- [ ] **T22** Request persistent storage with `navigator.storage.persist()` on start.

### Routing

- [ ] **T23** Implement a tiny hash router for `#/`, `#/default` and `#/history/<yyyy-MM-dd>`.
- [ ] **T24** Add a navigation bar linking daily list, default list and history.
- [ ] **T25** Add a language switch to the navigation bar that changes the language without a page reload (depends on T17, T24).

### UI: Daily list

- [ ] **T26** Render today's list sorted; check/uncheck an item with one tap.
- [ ] **T27** Add a delete button per item.
- [ ] **T28** Add the text field with `<datalist>` autocompletion from the suggestions.
- [ ] **T29** Add the buttons "add to daily list" and "add to default list".

### UI: Default list

- [ ] **T30** Render the default list with up/down buttons to change the order.
- [ ] **T31** Add a delete button per item of the default list.

### UI: History

- [ ] **T32** Render a month calendar grid with previous/next month navigation.
- [ ] **T33** Mark days that have a stored list; open a day's list read-only via `#/history/<yyyy-MM-dd>`.
- [ ] **T34** Add the button "copy to today" to a past day's list.

### PWA

- [ ] **T35** Add `manifest.webmanifest` and icons so the app can be installed.
- [ ] **T36** Add a service worker that caches the app files and the language files for offline use.
- [ ] **T37** Use a versioned cache in the service worker, remove old caches and reload the app after an update.

### Design

- [ ] **T38** Add `styles.css` with a mobile-first base layout, the cyberpunk color palette as CSS custom properties and the locally bundled fonts.
- [ ] **T39** Style buttons, inputs, list items and the navigation bar with neon outlines, glow and cut corners; tap targets at least 44 × 44 px, visible focus.
- [ ] **T40** Add the scanline overlay and the glitch animation for the title, disabled with `prefers-reduced-motion`.
- [ ] **T41** Style the calendar (stored days, today) and checked items in the cyberpunk theme; verify WCAG AA contrast.

### Documentation

- [x] **T42** Fix typos in this README.

## License

[MIT](LICENSE)
IT](LICENSE)
