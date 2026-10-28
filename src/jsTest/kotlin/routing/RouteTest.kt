package routing

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlin.test.assertTrue as assert

class RouteTest : FreeSpec({
    "parses supported routes" {
        assert(parseRoute("#/") == Route.DailyList)
        assert(parseRoute("#/default") == Route.DefaultList)
        assert(parseRoute("#/history/2026-10-27") == Route.History(LocalDate.parse("2026-10-27")))
    }

    "invalid routes fall back safely" {
        assert(parseRoute("#/unknown") == Route.DailyList)
        assert(parseRoute("#/history/not-a-date") == Route.History())
    }

    "routes produce their hash representation" {
        assert(Route.DailyList.toHash() == "#/")
        assert(Route.DefaultList.toHash() == "#/default")
        assert(Route.History(LocalDate.parse("2026-10-27")).toHash() == "#/history/2026-10-27")
    }
})
