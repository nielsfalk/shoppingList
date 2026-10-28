package model

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlin.test.assertTrue as assert

class CalendarTest : FreeSpec({
    "builds Monday-first weeks with blanks outside the month" {
        val weeks = CalendarMonth(2026, 10).weeks()

        assert(weeks.size == 5)
        assert(weeks.all { it.size == 7 })
        assert(weeks.first().take(3) == listOf(null, null, null))
        assert(weeks.first()[3] == LocalDate.parse("2026-10-01"))
        assert(weeks.last()[5] == LocalDate.parse("2026-10-31"))
        assert(weeks.last()[6] == null)
    }

    "navigates across year boundaries" {
        assert(CalendarMonth(2026, 1).previous() == CalendarMonth(2025, 12))
        assert(CalendarMonth(2026, 12).next() == CalendarMonth(2027, 1))
    }

    "handles leap years and converts dates" {
        assert(CalendarMonth(2028, 2).days().size == 29)
        assert(CalendarMonth(2026, 2).days().size == 28)
        assert(LocalDate.parse("2026-10-27").toCalendarMonth() == CalendarMonth(2026, 10))
    }
})
