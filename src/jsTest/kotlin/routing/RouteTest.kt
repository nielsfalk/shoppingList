package routing

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlin.test.assertTrue as assert

class RouteTest : FreeSpec({
    @Data("hash"                 ,  "expectedRoute"                             ) ParsedRoute {
          "#/"                   ǀǀ Route.DailyList
          "#/default"            ǀǀ Route.DefaultList
          "#/history/2026-10-27" ǀǀ Route.History(LocalDate.parse("2026-10-27"))
          "#/unknown"            ǀǀ Route.DailyList
          "#/history/not-a-date" ǀǀ Route.History()
    }.each {
        "parses '$hash' as $expectedRoute" {
            assert(parseRoute(hash) == expectedRoute)
        }
    }

    @Data("route"                                      ,  "expectedHash"        ) RouteHash {
          Route.DailyList                              ǀǀ "#/"
          Route.DefaultList                            ǀǀ "#/default"
          Route.History(LocalDate.parse("2026-10-27")) ǀǀ "#/history/2026-10-27"
    }.each {
        "$route has the hash '$expectedHash'" {
            assert(route.toHash() == expectedHash)
        }
    }
})
