// Browser tests that exercise Compose state and localStorage can exceed Mocha's 2s default on slower machines.
// Kotest runs all specs inside a single Mocha anchor test, so that test needs to cover the whole Kotest run.
config.set({
    client: {
        mocha: {
            timeout: 120000
        }
    }
});
