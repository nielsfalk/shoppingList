package model

import io.kotest.core.spec.style.FreeSpec
import kotlinx.datetime.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CalendarTest : FreeSpec({
    "builds Monday-first weeks with blanks outside the month" {
        val weeks = CalendarMonth(2026, 10).weeks()

        assertEquals(5, weeks.size)
        weeks.forEach { assertEquals(7, it.size) }
        assertEquals(listOf(null, null, null), weeks.first().take(3))
        assertEquals(LocalDate.parse("2026-10-01"), weeks.first()[3])
        assertEquals(LocalDate.parse("2026-10-31"), weeks.last()[5])
        assertNull(weeks.last()[6])
    }

    "navigates across year boundaries" {
        assertEquals(CalendarMonth(2025, 12), CalendarMonth(2026, 1).previous())
        assertEquals(CalendarMonth(2027, 1), CalendarMonth(2026, 12).next())
    }

    "handles leap years and converts dates" {
        assertEquals(29, CalendarMonth(2028, 2).days().size)
        assertEquals(28, CalendarMonth(2026, 2).days().size)
        assertEquals(CalendarMonth(2026, 10), LocalDate.parse("2026-10-27").toCalendarMonth())
    }
})
