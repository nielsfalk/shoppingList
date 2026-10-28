// Kotest blocks the Karma iframe while it runs its specs, so the tests run in the Karma page itself.
config.set({
    client: {
        runInParent: true,
        useIframe: false
    }
});
