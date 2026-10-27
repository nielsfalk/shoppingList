package routing

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteTest {
    @Test
    fun parsesSupportedRoutes() {
        assertEquals(Route.DailyList, parseRoute("#/"))
        assertEquals(Route.DefaultList, parseRoute("#/default"))
        assertEquals(Route.History(LocalDate.parse("2026-10-27")), parseRoute("#/history/2026-10-27"))
    }

    @Test
    fun invalidRoutesFallBackSafely() {
        assertEquals(Route.DailyList, parseRoute("#/unknown"))
        assertEquals(Route.History(), parseRoute("#/history/not-a-date"))
    }

    @Test
    fun routesProduceTheirHashRepresentation() {
        assertEquals("#/", Route.DailyList.toHash())
        assertEquals("#/default", Route.DefaultList.toHash())
        assertEquals("#/history/2026-10-27", Route.History(LocalDate.parse("2026-10-27")).toHash())
    }
}