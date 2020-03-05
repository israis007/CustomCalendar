package com.pirataram.calendarcustom.models

import android.view.View
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Comparator

data class EventModel(
    val id: Long,
    val startTime: Calendar,
    val endTime: Calendar,
    val background: Int,
    val view: View,
    val classPojo: Any
) {

    fun getDurationInMinutes(): Long =
        TimeUnit.MILLISECONDS.toMinutes(endTime.timeInMillis - startTime.timeInMillis)


    override fun toString(): String = Gson().toJson(this)

    class EventModelComparator: Comparator<EventModel> {
        override fun compare(event1: EventModel?, event2: EventModel?): Int {
            return event1!!.startTime.timeInMillis.compareTo(event2!!.startTime.timeInMillis)
        }
    }
}