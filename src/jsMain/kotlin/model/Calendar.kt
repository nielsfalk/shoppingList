package model

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

data class CalendarMonth(val year: Int, val month: Int) {
    init {
        require(month in 1..12) { "Invalid month: $month" }
    }

    val firstDay: LocalDate get() = LocalDate(year, month, 1)

    fun previous(): CalendarMonth = firstDay.minus(1, DateTimeUnit.MONTH).toCalendarMonth()

    fun next(): CalendarMonth = firstDay.plus(1, DateTimeUnit.MONTH).toCalendarMonth()

    fun days(): List<LocalDate> {
        val firstOfNextMonth = next().firstDay
        return generateSequence(firstDay) { it.plus(1, DateTimeUnit.DAY) }
            .takeWhile { it < firstOfNextMonth }
            .toList()
    }

    /** Weeks starting on Monday; days outside this month are `null`. */
    fun weeks(): List<List<LocalDate?>> {
        val leadingBlanks = firstDay.dayOfWeek.isoDayNumber - 1
        val cells: List<LocalDate?> = List(leadingBlanks) { null } + days()
        val trailingBlanks = (7 - cells.size % 7) % 7
        return (cells + List(trailingBlanks) { null }).chunked(7)
    }
}

fun LocalDate.toCalendarMonth(): CalendarMonth = CalendarMonth(year, month.ordinal + 1)
