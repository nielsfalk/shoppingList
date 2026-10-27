package storage

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SchemaTest {
    @Test
    fun initializesSchemaVersionForNewStorage() {
        val storage = newTestStorage()

        try {
            initSchema(storage)

            assertEquals(CURRENT_SCHEMA_VERSION.toString(), storage.getString("schemaVersion"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun runsMigrationsInOrderAndRecordsEachCompletedVersion() {
        val storage = newTestStorage()
        var migrationRuns = 0

        try {
            storage.setString("schemaVersion", "0")
            initSchema(
                storage,
                migrations = mapOf(
                    0 to { migratedStorage ->
                        migrationRuns++
                        migratedStorage.setString("migrationResult", "complete")
                    },
                ),
            )
            initSchema(storage, migrations = mapOf(0 to { migrationRuns++ }))

            assertEquals(1, migrationRuns)
            assertEquals("complete", storage.getString("migrationResult"))
            assertEquals(CURRENT_SCHEMA_VERSION.toString(), storage.getString("schemaVersion"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    @Test
    fun missingMigrationDoesNotAdvanceSchemaVersion() {
        val storage = newTestStorage()

        try {
            storage.setString("schemaVersion", "0")

            assertFailsWith<IllegalStateException> { initSchema(storage) }
            assertEquals("0", storage.getString("schemaVersion"))
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    private fun newTestStorage() = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
}