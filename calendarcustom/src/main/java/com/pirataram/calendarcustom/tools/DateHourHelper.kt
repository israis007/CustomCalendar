package com.pirataram.calendarcustom.tools

import android.util.Log
import android.util.TimeUtils
import com.pirataram.calendarcustom.BuildConfig
import java.util.*
import java.util.concurrent.TimeUnit

class DateHourHelper {
    companion object {
        private const val TAG = "DateHourFormatter"

        fun getHeightBetweenTwoHours(valueInPx: Float, dateEnd: Calendar, dateStart: Calendar): Float {
            val minutes = dateStart.timeInMillis - dateEnd.timeInMillis
            return (TimeUnit.MILLISECONDS.toMinutes(minutes) + 60)  * valueInPx
        }

        fun getCalendar(millisecons: Long): Calendar {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = millisecons
            val goyoCalendar = GregorianCalendar(TimeZone.getDefault())
            goyoCalendar.timeInMillis = calendar.timeInMillis
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Calendar converted TimeZone default ---> ${DateHourFormatter.getStringFormatted(goyoCalendar, "dd/MM/yyyy  HH:mm:ss aa")} <---")
            return goyoCalendar
        }

        fun getCalendarWithoutHour(calendar: Calendar): Calendar {
            return calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        fun getCurrentCalendarWithoutHour() : Calendar =
            getCalendarWithoutHour(Calendar.getInstance(Locale.getDefault()))

        fun getCalendarInDays(calendar: Calendar): Long =
            TimeUnit.MILLISECONDS.toDays(calendar.timeInMillis)

        fun getCurrentCalendarInDays(): Long =
            TimeUnit.MILLISECONDS.toDays(getCurrentCalendarWithoutHour().timeInMillis)
    }
}