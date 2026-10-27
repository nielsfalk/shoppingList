// Browser tests that exercise Compose state and localStorage can exceed Mocha's 2s default on slower machines.
config.set({
    client: {
        mocha: {
            timeout: 10000
        }
    }
});
