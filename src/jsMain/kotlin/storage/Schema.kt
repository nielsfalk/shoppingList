package storage

const val CURRENT_SCHEMA_VERSION = 1

fun initSchema(storage: Storage, migrations: Map<Int, (Storage) -> Unit> = emptyMap()) {
    val storedVersion = storage.getString("schemaVersion")
    if (storedVersion == null) {
        storage.setString("schemaVersion", CURRENT_SCHEMA_VERSION.toString())
        return
    }

    var version = storedVersion.toIntOrNull()
        ?: throw IllegalStateException("Invalid storage schema version: $storedVersion")
    require(version <= CURRENT_SCHEMA_VERSION) { "Unsupported storage schema version: $version" }

    while (version < CURRENT_SCHEMA_VERSION) {
        val migration = migrations[version]
            ?: throw IllegalStateException("Missing migration from schema version $version")
        migration(storage)
        version++
        storage.setString("schemaVersion", version.toString())
    }
}