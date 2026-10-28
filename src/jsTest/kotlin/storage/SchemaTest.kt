package storage

import io.kotest.core.spec.style.FreeSpec
import kotlin.random.Random
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue as assert

class SchemaTest : FreeSpec({
    "initializes the schema version for new storage" {
        val storage = newTestStorage()

        try {
            initSchema(storage)

            assert(storage.getString("schemaVersion") == CURRENT_SCHEMA_VERSION.toString())
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "runs migrations in order and records each completed version" {
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

            assert(migrationRuns == 1)
            assert(storage.getString("migrationResult") == "complete")
            assert(storage.getString("schemaVersion") == CURRENT_SCHEMA_VERSION.toString())
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }

    "a missing migration does not advance the schema version" {
        val storage = newTestStorage()

        try {
            storage.setString("schemaVersion", "0")

            assertFailsWith<IllegalStateException> { initSchema(storage) }

            assert(storage.getString("schemaVersion") == "0")
        } finally {
            storage.keys().forEach(storage::remove)
        }
    }
})

private fun newTestStorage() = Storage(prefix = "shoppingList:test:${Random.nextLong()}:")
