import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import routing.Route
import routing.rememberRoute
import storage.Storage
import storage.initSchema
import storage.requestPersistentStorage

fun main() {
    requestPersistentStorage()
    initSchema(Storage())

    renderComposable(rootElementId = "root") {
        when (val route = rememberRoute()) {
            Route.DailyList -> Text("Daily list")
            Route.DefaultList -> Text("Default list")
            is Route.History -> Text(route.date?.toString() ?: "History")
        }
    }
}