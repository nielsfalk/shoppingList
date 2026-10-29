package model

import de.nielsfalk.dataTables.Data
import io.kotest.core.spec.style.FreeSpec
import kotlin.test.assertTrue as assert

class SuggestionOrderTest : FreeSpec({
    val suggestionOrder = SuggestionOrder(
        mapOf(
            Department.FRUIT_AND_VEGETABLES to listOf("Apples", "Bananas"),
            Department.BAKERY to listOf("Bread", "Rolls"),
            Department.DRY_GOODS to listOf("Oats"),
            Department.CHILLED to listOf("Tofu"),
        ),
    )

    @Data("name"     ,  "expectedDepartment"            , "expectedPosition") SuggestionLookupCases {
          " apples " ǀǀ Department.FRUIT_AND_VEGETABLES ǀ 0
          "bReAd"    ǀǀ Department.BAKERY               ǀ 2
          " TOFU "   ǀǀ Department.CHILLED              ǀ 5
          "unknown"  ǀǀ null                            ǀ null
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