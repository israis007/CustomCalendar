package com.irisoft.calendarmodule.ui

import androidx.annotation.ColorInt

interface Event : Comparable<Event> {
    val title: String
    val description: String?
    @get:ColorInt
    val color: Int?
    val start: Long
    val end: Long
    val allDay: Boolean
}

open class BaseEvent(
    override val title: String,
    override val description: String?,
    override val color: Int?,
    override val start: Long,
    override val end: Long,
    override val allDay: Boolean
) : Event {
    override fun compareTo(other: Event): Int {
        val result = start.compareTo(other.start)
        if (result != 0)
            return result
        return end.compareTo(other.end)
    }

    override fun toString(): String =
        "$title ($description), $start-$end, allDay: $allDay"

}

class AddEvent(
    title: String,
    description: String,
    color: Int?,
    start: Long,
    end: Long,
    allDay: Boolean
) : BaseEvent(title, description, color, start, end, allDay)