const CACHE_PREFIX = "shoppingList-";
const CACHE_NAME = CACHE_PREFIX + "__BUILD_VERSION__";
const APP_FILES = [
    "./",
    "index.html",
    "shoppingList.js",
    "manifest.webmanifest",
    "styles.css",
    "fonts/orbitron-700.woff2",
    "fonts/share-tech-mono-400.woff2",
    "icons/icon-192.png",
    "icons/icon-512.png",
];

self.addEventListener("install", (event) => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then((cache) => cache.addAll(APP_FILES.map((file) => new Request(file, { cache: "reload" }))))
            .then(() => self.skipWaiting())
    );
});

self.addEventListener("activate", (event) => {
    event.waitUntil(
        caches.keys()
            .then((names) => Promise.all(names
                .filter((name) => name.startsWith(CACHE_PREFIX) && name !== CACHE_NAME)
                .map((name) => caches.delete(name))))
            .then(() => self.clients.claim())
    );
});

self.addEventListener("fetch", (event) => {
    const request = event.request;
    if (request.method !== "GET" || new URL(request.url).origin !== self.location.origin) return;

    event.respondWith(
        caches.match(request).then((cached) => cached || fetch(request).then((response) => {
            if (response.ok) {
                const copy = response.clone();
                caches.open(CACHE_NAME).then((cache) => cache.put(request, copy));
            }
            return response;
        }))
    );
});
