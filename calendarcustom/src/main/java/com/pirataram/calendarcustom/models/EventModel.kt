package com.pirataram.calendarcustom.models

import android.view.View
import com.google.gson.Gson
import com.pirataram.calendarcustom.tools.DateHourHelper.Companion.getCalendar
import java.util.*
import java.util.concurrent.TimeUnit

data class EventModel(
    val id: Long,
    val startTime: Calendar,
    val endTime: Calendar,
    val background: Int?,
    val view: View,
    val classPojo: Any
) {

    constructor(id: Long, startTime: Long, endTime: Long, view: View, classPojo: Any): this(
        id, getCalendar(startTime), getCalendar(endTime), null, view, classPojo
    )

    fun getDurationInMinutes(): Long =
        TimeUnit.MILLISECONDS.toMinutes(endTime.timeInMillis - startTime.timeInMillis)

    override fun toString(): String = Gson().toJson("$id,$startTime,$endTime,$background,$view,$classPojo")

    class EventModelComparator: Comparator<EventModel> {
        override fun compare(event1: EventModel?, event2: EventModel?): Int {
            return event1!!.startTime.timeInMillis.compareTo(event2!!.startTime.timeInMillis)
        }
    }
}