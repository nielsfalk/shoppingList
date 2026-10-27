import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import storage.Storage
import storage.initSchema
import storage.requestPersistentStorage

fun main() {
    requestPersistentStorage()
    initSchema(Storage())

    renderComposable(rootElementId = "root") {
        Text("Hello shoppingList")
    }
}