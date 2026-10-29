package model

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import model.Department.*
import kotlin.test.assertTrue as assert

class SuggestionOrderTest : FreeSpec({
    val suggestionOrder = SuggestionOrder(
        mapOf(
            FRUIT_AND_VEGETABLES to listOf("Apples", "Bananas"),
            BAKERY to listOf("Bread", "Rolls"),
            DRY_GOODS to listOf("Oats"),
            CHILLED to listOf("Tofu"),
        ),
        aliases = mapOf(
            "Bannanas" to "Bananas",
            "White Bread" to "Bread",
        ),
    )

    @Data("name"        ,  "expectedDepartment" , "expectedPosition") SuggestionLookupCases {
          " apples "    ǀǀ FRUIT_AND_VEGETABLES ǀ 0
          "bReAd"       ǀǀ BAKERY               ǀ 2
          " TOFU "      ǀǀ CHILLED              ǀ 5
          "Bannanas"    ǀǀ FRUIT_AND_VEGETABLES ǀ 1
          "white bread" ǀǀ BAKERY               ǀ 2
          "unknown"     ǀǀ null                 ǀ null
    }.each {
        "looks up the department and position of '$name'" {
            assert(suggestionOrder.departmentOf(name) == expectedDepartment)
            assert(suggestionOrder.positionOf(name) == expectedPosition)
        }
    }

    "sorts known items by suggestion position and keeps unknown items in their list order" {
        val items = listOf("Unknown two", "Bananas", "Unknown one", "bread", "Apples")

        assert(suggestionOrder.sorted(items) { it } == listOf("Apples", "Bananas", "bread", "Unknown two", "Unknown one"))
    }
})