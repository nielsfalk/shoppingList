import org.jetbrains.compose.web.renderComposable
import storage.Storage
import storage.initSchema
import storage.requestPersistentStorage
import ui.App

fun main() {
    requestPersistentStorage()
    initSchema(Storage())

    renderComposable(rootElementId = "root") {
        App()
    }
}