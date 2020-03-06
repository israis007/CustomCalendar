package com.pirataram.calendarcustom.tools

import java.util.*
import java.util.concurrent.TimeUnit

class DateHourHelper {
    companion object {

        fun getHeightBetweenTwoHours(valueInPx: Float, dateEnd: Calendar, dateStart: Calendar): Float {
            val minutes = dateStart.timeInMillis - dateEnd.timeInMillis
            return (TimeUnit.MILLISECONDS.toMinutes(minutes) + 60)  * valueInPx
        }

        fun getCalendar(millisecons: Long): Calendar {
            val date = Date()
            date.time = millisecons
            val cal = Calendar.getInstance(Locale.getDefault())
            cal.time = date
            cal.timeZone = TimeZone.getDefault()
            return cal
        }
    }
}