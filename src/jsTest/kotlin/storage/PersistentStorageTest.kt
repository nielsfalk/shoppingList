package storage

import kotlin.test.Test

class PersistentStorageTest {
    @Test
    fun requestsPersistentStorageWhenAvailableWithoutFailingWhenUnsupported() {
        requestPersistentStorage()
    }
}