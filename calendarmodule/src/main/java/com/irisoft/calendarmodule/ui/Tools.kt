package com.irisoft.calendarmodule.ui

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Tools {
    companion object {
        fun getFormatedTextFromDate(calendar: Calendar, mask: String): String {
            val sdf = SimpleDateFormat(mask, Locale.getDefault())
            return try {
                sdf.format(calendar.time)
            } catch (e: Exception) {
                ""
            }
        }
    }
}