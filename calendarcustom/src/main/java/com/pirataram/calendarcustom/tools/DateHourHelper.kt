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
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millisecons
            val goyoCalendar = GregorianCalendar(TimeZone.getDefault())
            goyoCalendar.timeInMillis = calendar.timeInMillis
            return goyoCalendar
        }
    }
}