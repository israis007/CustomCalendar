package com.pirataram.calendarcustom.models

import android.graphics.Paint
import android.text.TextPaint
import com.google.gson.Gson
import com.pirataram.calendarcustom.tools.DateHourFormatter
import com.pirataram.calendarcustom.tools.DateHourHelper
import java.text.DecimalFormat
import java.util.*

class PropertiesObject(var calendar: Calendar) {
    var clock_background: Int = 0
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
    var clock_line_now_show_draw_on: Direction = Direction.UP
    var clock_line_now_show_hour: Boolean = true
    var clock_line_now_show: Boolean = true
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
    var clock_events_padding_between: Float = 0f
    var clock_max_date: Limits = Limits.FUTURE
    var clock_min_date: Limits = Limits.PAST
    var clock_max_date_calendar: Calendar = Calendar.getInstance(Locale.getDefault())
    var clock_min_date_calendar: Calendar = Calendar.getInstance(Locale.getDefault())
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
        repeat(getHoursToDraw()) {
            floatArray[it] = (getheighByPx() * (it + 1)) - (clock_text_size * h)
        }
        floatArray[getHoursToDraw()] =
            floatArray[getHoursToDraw() - 1] + getheighByPx() - (clock_text_size * h)
        return floatArray
    }

    fun getValueInPXByMinute(): Float {
        val cy1 = getheighByPx() - (clock_text_size * h)
        val cy2 = getheighByPx() * 2 - (clock_text_size * h)
        return (cy2 - cy1) / 59
    }

    fun getCoorXToDrawHorizontalLines(): Float =
        getCoorXToDrawVerticalLine() - (clock_text_margin_end / 2)

    fun getDifferenceOfHours(): Int =
        Calendar.getInstance(Locale.getDefault())[Calendar.HOUR_OF_DAY] - clock_min_hour

    fun getClockPaint(): TextPaint {
        clockPaint.apply {
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

    fun getTotalDaysPast(calendar: Calendar?): Long {
        return if (calendar == null)
            when(clock_min_date){
                Limits.TODAY -> 0L
                Limits.INFINITELY -> Int.MAX_VALUE.toLong()
                else -> DateHourHelper.getCurrentCalendarInDays() - DateHourHelper.getCalendarInDays(clock_min_date_calendar)
            }
        else
            DateHourHelper.getCalendarInDays(calendar) - DateHourHelper.getCalendarInDays(clock_min_date_calendar)
    }

    fun getTotalDaysFuture(calendar: Calendar?): Long {
        return if (calendar == null)
            when(clock_max_date){
                Limits.TODAY -> 0L
                Limits.INFINITELY -> Int.MAX_VALUE.toLong()
                else -> DateHourHelper.getCalendarInDays(clock_max_date_calendar) - DateHourHelper.getCurrentCalendarInDays()
            }
        else
            DateHourHelper.getCalendarInDays(clock_max_date_calendar) - DateHourHelper.getCalendarInDays(calendar)
    }


    companion object {
        fun getDirection(value: Int): Direction {
            return if (value == 0) Direction.UP else Direction.DOWN
        }

        fun getLimits(value: Int): Limits {
            return when (value) {
                0 -> Limits.TODAY
                1 -> Limits.FUTURE
                2 -> Limits.PAST
                else -> Limits.INFINITELY
            }
        }
    }

    enum class Direction {
        UP,
        DOWN;
    }

    enum class Limits {
        TODAY,
        FUTURE,
        PAST,
        INFINITELY;
    }

    fun getCoordinatesYEachQuarterOfHour(): FloatArray {
        val array = FloatArray(getHoursToDraw() * 4)
        val coorys = getCoorYToDrawHorizontalLines()
        var contador = 0
        for (i in 0 until coorys.size - 1){
            var cy1 = coorys[i]
            if (i == 0) {
                array[contador] = cy1 * 0.25f
                array[contador + 1] = cy1 * 0.50f
                array[contador + 2] = cy1 * 0.75f
                array[contador + 3] = cy1
            } else {
                cy1 = coorys[i - 1]
                val cy2 = coorys[i]
                val rest = cy2 - cy1
                array[contador] = rest * 0.25f + cy1
                array[contador + 1] = rest * 0.50f + cy1
                array[contador + 2] = rest * 0.75f + cy1
                array[contador + 3] = cy2
            }
            contador += 4
        }
        return array
    }

    fun getCoorYNewEvent(coorY: Float, cal: Calendar): CoorYNewEvent? {
        val coorys = getCoordinatesYEachQuarterOfHour()
        var tempY = 0f
        for (i in coorys.indices){
            if (coorys[i] > coorY) {
                val hours = i.toFloat() / 4f
                val hour = DecimalFormat("#.0").format(hours).toDouble().toInt()
                val min = (i - hour * 4) * 15
                val minutes = if (min == 60) 0 else min
                val calendar = Calendar.getInstance(Locale.getDefault()).apply {
                    time.time = cal.time.time
                    set(Calendar.HOUR_OF_DAY, clock_min_hour)
                }
                repeat(hour - 1){
                    calendar[Calendar.HOUR_OF_DAY] += 1
                }
                calendar[Calendar.MINUTE] = minutes
                return CoorYNewEvent(
                    coorys[i - if (i == 0) 0 else 3],
                    coorys[i + if (i == 3) 0 else 1],
                    calendar,
                    calendar[Calendar.HOUR_OF_DAY] in (clock_worktime_min_hour + 1) until clock_worktime_max_hour
                )
            } else
                tempY = coorys[i]
        }
        return null
    }

    data class CoorYNewEvent (
        val coorY: Float,
        val coorYOneHourBefore: Float,
        val dateSelected: Calendar,
        val isInWorkTime: Boolean
    )
}