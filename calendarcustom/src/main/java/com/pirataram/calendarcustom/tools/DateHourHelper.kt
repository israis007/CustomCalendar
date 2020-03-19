package com.pirataram.calendarcustom.tools

import android.util.Log
import android.util.TimeUtils
import com.pirataram.calendarcustom.BuildConfig
import com.pirataram.calendarcustom.models.PropertiesObject
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

        fun isToday(calendar: Calendar): Boolean =
            getCalendarInDays(calendar) == getCurrentCalendarInDays()

        fun isDayOf(calendar: Calendar): DAY_OF =
            when {
                getCalendarInDays(calendar) - getCurrentCalendarInDays() < 0 -> DAY_OF.PAST
                getCalendarInDays(calendar) - getCurrentCalendarInDays() > 0 -> DAY_OF.FUTURE
                else -> DAY_OF.TODAY
            }

        fun cloneCalendar(calendar: Calendar): Calendar =
            Calendar.getInstance(Locale.getDefault()).apply {
                set(Calendar.YEAR, calendar[Calendar.YEAR])
                set(Calendar.MONTH, calendar[Calendar.MONTH])
                set(Calendar.DAY_OF_YEAR, calendar[Calendar.DAY_OF_YEAR])
                set(Calendar.DAY_OF_MONTH, calendar[Calendar.DAY_OF_MONTH])
                set(Calendar.DAY_OF_WEEK, calendar[Calendar.DAY_OF_WEEK])
                set(Calendar.HOUR_OF_DAY, calendar[Calendar.HOUR_OF_DAY])
                set(Calendar.MINUTE, calendar[Calendar.MINUTE])
                set(Calendar.SECOND, calendar[Calendar.SECOND])
                set(Calendar.MILLISECOND, calendar[Calendar.MILLISECOND])
            }

        enum class DAY_OF {
            PAST,
            TODAY,
            FUTURE
        }
    }
}