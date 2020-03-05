package com.pirataram.calendarcustom.tools

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DateHourFormatter {
    companion object {
        fun getStringFormatted(calendar: Calendar, pattern: String) =
            try {
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                sdf.format(calendar.time)
            } catch (e: Exception){
                ""
            }
    }
}