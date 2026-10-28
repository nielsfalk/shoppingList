package model

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlin.test.assertTrue as assert

class SuggestionsTest : FreeSpec({
    "suggests default items not in the daily list without duplicate language suggestions" {
        val dailyList = DailyList(
            date = LocalDate.parse("2026-10-27"),
            items = listOf(Item("Bread"), Item(" water "), Item("Bread", checked = true)),
        )
        val defaultList = DefaultList(listOf("Bread", "Water", "Tofu", "Oat milk"))

        val suggestions = suggestionsFor(
            dailyList,
            defaultList,
            listOf("Tofu", "oat milk", "Apples", "Apples"),
        )

        assert(suggestions == listOf("Tofu", "Oat milk", "Apples"))
    }
})
