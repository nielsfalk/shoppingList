import org.jetbrains.compose.web.renderComposable
import pwa.registerServiceWorker
import storage.Storage
import storage.initSchema
import storage.requestPersistentStorage
import ui.App

fun main() {
    requestPersistentStorage()
    initSchema(Storage())
    registerServiceWorker()

    renderComposable(rootElementId = "root") {
        App()
    }
}