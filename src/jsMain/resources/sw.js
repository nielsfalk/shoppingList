const CACHE_NAME = "shoppingList-v1";
const APP_FILES = [
    "./",
    "index.html",
    "shoppingList.js",
    "manifest.webmanifest",
    "icons/icon-192.png",
    "icons/icon-512.png",
];

self.addEventListener("install", (event) => {
    event.waitUntil(caches.open(CACHE_NAME).then((cache) => cache.addAll(APP_FILES)));
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
