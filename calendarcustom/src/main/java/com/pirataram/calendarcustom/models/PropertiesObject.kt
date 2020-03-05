package com.pirataram.calendarcustom.models

import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.Log
import com.google.gson.Gson
import com.pirataram.calendarcustom.tools.DateHourFormatter
import java.util.*

class PropertiesObject(private val calendar: Calendar) {
    var clock_text_show: Boolean = true
    var clock_text_color: Int = 0
    var clock_text_size: Float = 0f
    var clock_text_min_size: Float = 0f
    var clock_text_max_size: Float = 0f
    var clock_text_margin_start: Float = 0f
    var clock_text_margin_top: Float = 0f
    var clock_text_margin_end: Float = 0f
    var clock_text_mask: String = ""
    var clock_max_hour: Int = 0
    var clock_min_hour: Int = 0
    var clock_line_show: Boolean = true
    var clock_line_now_color: Int = 0
    var clock_line_now_radius: Float = 0f
    var clock_line_now_height: Float = 0f
    var clock_vertical_dividers_show: Boolean = true
    var clock_vertical_dividers_color: Int = 0
    var clock_horizontal_dividers_show: Boolean = true
    var clock_horizontal_dividers_color: Int = 0
    var grid_event_at_time: Int = 0
    var grid_event_divider_minutes: Int = 0
    var clock_worktime_show: Boolean = true
    var clock_worktime_min_hour: Int = 0
    var clock_worktime_max_hour: Int = 0
    var clock_worktime_color: Int = 0
    var clock_events_filter_transparency: Boolean = true
    var clock_events_opacity_percent: Float = 0f
    private var clockPaint: TextPaint = TextPaint()
    private var lineNowPaint: Paint = Paint()
    private var gridHorizontalPaint: Paint = Paint()
    private var gridVerticalPaint: Paint = Paint()
    private var gridWorkTimePaint: Paint = Paint()
    private val cal = Calendar.getInstance()
    private val h = .45f

    override fun toString(): String {
        return Gson().toJson(this)
    }

    fun getHoursToDraw() = clock_max_hour - clock_min_hour

    fun getheighByPx() = clock_text_margin_top + clock_text_size

    fun isValidHours() = clock_min_hour < clock_max_hour

    fun getCalendarToDraw(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = this.calendar.time
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.HOUR_OF_DAY] += clock_min_hour
        return calendar
    }

    fun isOutOfHour(): Boolean {
        val cal = Calendar.getInstance(Locale.getDefault())
        return cal[Calendar.HOUR_OF_DAY] > clock_max_hour ||
                cal[Calendar.HOUR_OF_DAY] == clock_max_hour && cal[Calendar.MINUTE] > 0 ||
                cal[Calendar.HOUR_OF_DAY] < clock_min_hour
    }

    fun isToday(): Boolean {
        val calnow = Calendar.getInstance(Locale.getDefault())
        Log.d("ProObjetc", "${calnow[Calendar.YEAR]} -> ${calendar[Calendar.YEAR]} | ${calnow[Calendar.DAY_OF_MONTH]} -> ${calendar[Calendar.MONTH]} | ${calnow[Calendar.DAY_OF_MONTH]} -> ${calendar[Calendar.DAY_OF_MONTH]}")
        return calnow[Calendar.YEAR] == calendar[Calendar.YEAR] && calnow[Calendar.MONTH] == calendar[Calendar.MONTH] && calnow[Calendar.DAY_OF_MONTH] == calendar[Calendar.DAY_OF_MONTH]
    }

    fun getCoorXToDrawVerticalLine(): Float {
        cal.time = calendar.time
        cal[Calendar.HOUR_OF_DAY] = 1
        val txt = DateHourFormatter.getStringFormatted(cal, clock_text_mask)
        val ancho = txt.length * (clock_text_size * .66f)
        return (clock_text_margin_start + ancho + clock_text_margin_end * 1.5f) - (clock_text_margin_end / 2)
    }

    fun getCoorYToDrawHorizontalLines(): FloatArray {
        val floatArray = FloatArray(getHoursToDraw() + 1)
            repeat(getHoursToDraw()){
                floatArray[it] = (getheighByPx() * (it + 1)) - (clock_text_size * h)
            }
        floatArray[getHoursToDraw()] = floatArray[getHoursToDraw() - 1] + getheighByPx() - (clock_text_size * h)
        return floatArray
    }

    fun getValueInPXByMinute(): Float {
        val cy1 = getheighByPx() - (clock_text_size * h)
        val cy2 = getheighByPx() * 2 - (clock_text_size * h)
        return (cy2 - cy1) / 59
    }

    fun getCoorXToDrawHorizontalLines(): Float = getCoorXToDrawVerticalLine() - (clock_text_margin_end / 2)

    fun getDifferenceOfHours(): Int = Calendar.getInstance(Locale.getDefault())[Calendar.HOUR_OF_DAY] - clock_min_hour

    fun getClockPaint(): TextPaint {
        clockPaint.apply{
            color = clock_text_color
            isAntiAlias = true
            textSize = clock_text_size
        }
        return clockPaint
    }

    fun getGridHorizontalPaint(): Paint {
        gridHorizontalPaint.apply {
            color = clock_horizontal_dividers_color
            isAntiAlias = true
        }
        return gridHorizontalPaint
    }

    fun getGridVerticalPaint(): Paint {
        gridVerticalPaint.apply {
            color = clock_vertical_dividers_color
            isAntiAlias = true
        }
        return gridVerticalPaint
    }

    fun getLineNowPaint(): Paint {
        lineNowPaint.apply {
            color = clock_line_now_color
            isAntiAlias = true
        }
        return lineNowPaint
    }

    fun getGridWorkTimePaint(): Paint {
        gridWorkTimePaint.apply {
            color = clock_worktime_color
            isAntiAlias = true
        }
        return gridWorkTimePaint
    }

}