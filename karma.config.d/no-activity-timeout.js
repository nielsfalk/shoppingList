// Kotest reports its results to Karma only after all specs have run, so the browser stays silent longer than Karma's 30s default.
// While Kotest runs, the browser can also miss Karma's 5s socket heartbeat, so that timeout is raised as well.
config.set({
    browserNoActivityTimeout: 120000,
    pingTimeout: 120000
});
