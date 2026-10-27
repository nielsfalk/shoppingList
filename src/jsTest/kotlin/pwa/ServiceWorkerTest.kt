package pwa

import kotlin.test.Test

class ServiceWorkerTest {
    @Test
    fun registrationDoesNotThrowWhenServiceWorkerIsMissingOrFails() {
        registerServiceWorker()
    }
}
