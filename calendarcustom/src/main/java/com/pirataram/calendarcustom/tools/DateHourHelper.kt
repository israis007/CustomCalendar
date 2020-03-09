package com.pirataram.calendarcustom.tools

import android.util.Log
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
                Log.d(TAG, "Calendar converted TimeZone defualt ---> ${DateHourFormatter.getStringFormatted(goyoCalendar, "dd/MM/yyyy  HH:mm:ss aa")} <---")
            return goyoCalendar
        }
    }
}