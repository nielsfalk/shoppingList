// Kotest reports its results to Karma only after all specs have run, so the browser stays silent longer than Karma's 30s default.
config.set({
    browserNoActivityTimeout: 120000
});
