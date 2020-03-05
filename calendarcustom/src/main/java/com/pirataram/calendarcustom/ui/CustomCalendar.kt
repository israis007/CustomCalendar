package com.pirataram.calendarcustom.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import com.pirataram.calendarcustom.tools.DateHourFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round


class CustomCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var proOb: PropertiesObject
    private var calendar = Calendar.getInstance(Locale.getDefault())
    private var gridc: LinearCustom
    private var lastCalendar = Calendar.getInstance()
    private var lastCoorY = 0f
    private val TAG = "CustomCalendar"

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.CustomCalendar,
            defStyleAttr,
            R.style.CustomCalendar
        ) {
            proOb = PropertiesObject(calendar)
            val reso = context.resources
            proOb.clock_text_show = getBoolean(R.styleable.CustomCalendar_clock_text_show, true)
            proOb.clock_text_color = getColor(
                R.styleable.CustomCalendar_clock_text_color,
                ContextCompat.getColor(context, R.color.clock_text_hour)
            )
            proOb.clock_text_size = getDimension(
                R.styleable.CustomCalendar_clock_text_size,
                reso.getDimension(R.dimen.clock_text_height)
            )
            proOb.clock_text_min_size = getDimension(
                R.styleable.CustomCalendar_clock_text_min_size,
                reso.getDimension(R.dimen.clock_text_min_height)
            )
            proOb.clock_text_max_size = getDimension(
                R.styleable.CustomCalendar_clock_text_max_size,
                reso.getDimension(R.dimen.clock_text_max_height)
            )
            proOb.clock_text_margin_start = getDimension(
                R.styleable.CustomCalendar_clock_text_margin_start,
                reso.getDimension(R.dimen.clock_text_padding_start)
            )
            proOb.clock_text_margin_top = getDimension(
                R.styleable.CustomCalendar_clock_text_margin_top,
                reso.getDimension(R.dimen.clock_text_padding_top)
            )
            proOb.clock_text_margin_end = getDimension(
                R.styleable.CustomCalendar_clock_text_margin_end,
                reso.getDimension(R.dimen.clock_text_padding_end)
            )
            val mask = getString(R.styleable.CustomCalendar_clock_text_mask)
            proOb.clock_text_mask =
                if (mask.isNullOrEmpty()) reso.getString(R.string.clock_mask) else mask
            proOb.clock_max_hour = getInteger(
                R.styleable.CustomCalendar_clock_max_hour,
                reso.getInteger(R.integer.clock_max_hour)
            )
            proOb.clock_min_hour = getInteger(
                R.styleable.CustomCalendar_clock_min_hour,
                reso.getInteger(R.integer.clock_min_hour)
            )
            proOb.clock_line_show = getBoolean(R.styleable.CustomCalendar_clock_line_now_show, true)
            proOb.clock_line_now_color = getColor(
                R.styleable.CustomCalendar_clock_line_now_color,
                ContextCompat.getColor(context, R.color.clock_line_now)
            )
            proOb.clock_line_now_height = getDimension(
                R.styleable.CustomCalendar_clock_line_now_height,
                reso.getDimension(R.dimen.clock_linenow_height)
            )
            proOb.clock_line_now_radius = getDimension(
                R.styleable.CustomCalendar_clock_line_now_radius,
                reso.getDimension(R.dimen.clock_linenow_radius_circle)
            )
            proOb.clock_horizontal_dividers_show =
                getBoolean(R.styleable.CustomCalendar_clock_horizontal_dividers_show, true)
            proOb.clock_horizontal_dividers_color = getColor(
                R.styleable.CustomCalendar_clock_horizontal_dividers_color,
                ContextCompat.getColor(context, R.color.clock_dividers)
            )
            proOb.clock_vertical_dividers_show =
                getBoolean(R.styleable.CustomCalendar_clock_vertical_dividers_show, true)
            proOb.clock_vertical_dividers_color = getColor(
                R.styleable.CustomCalendar_clock_vertical_dividers_color,
                ContextCompat.getColor(context, R.color.clock_dividers)
            )
            proOb.grid_event_at_time = getInteger(
                R.styleable.CustomCalendar_grid_event_at_time,
                reso.getInteger(R.integer.max_event_at_time)
            )
            proOb.grid_event_divider_minutes = getInteger(
                R.styleable.CustomCalendar_grid_event_divider_minutes,
                reso.getInteger(R.integer.divider_each_minutes)
            )
            proOb.clock_worktime_show = getBoolean(
                R.styleable.CustomCalendar_clock_worktime_show,
                true
            )
            proOb.clock_worktime_min_hour = getInteger(
                R.styleable.CustomCalendar_clock_worktime_min_hour,
                reso.getInteger(R.integer.clock_worktime_min_hour)
            )
            proOb.clock_worktime_max_hour = getInteger(
                R.styleable.CustomCalendar_clock_worktime_max_hour,
                reso.getInteger(R.integer.clock_worktime_max_hour)
            )
            proOb.clock_worktime_color = getInteger(
                R.styleable.CustomCalendar_clock_worktime_color,
                ContextCompat.getColor(context, R.color.clock_work_time)
            )
            proOb.clock_events_filter_transparency = getBoolean(
                R.styleable.CustomCalendar_clock_events_filter_transparency,
                true
            )
            proOb.clock_events_opacity_percent = getFloat(
                R.styleable.CustomCalendar_clock_events_opacity_percent,
                Constants.transparency
            )
        }

        val scrollParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        val scroll = ScrollView(context) as android.widget.ScrollView
        scroll.layoutParams = scrollParams
        scrollParams.addRule(ALIGN_PARENT_END, TRUE)
        scrollParams.addRule(ALIGN_PARENT_TOP, TRUE)
        scrollParams.addRule(ALIGN_PARENT_START, TRUE)
        scrollParams.addRule(ALIGN_PARENT_BOTTOM, TRUE)

        val clock = ClockLayout(context, proOb) as View

        gridc = LinearCustom(context, proOb)

        val relativeLayout = RelativeLayout(context)
        val position = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        position.addRule(CENTER_IN_PARENT)
        clock.layoutParams = position
        relativeLayout.addView(clock)

        //Calculates margin start and height
        val coory = proOb.getCoorYToDrawHorizontalLines()[proOb.getHoursToDraw()].toInt()
        val position2 = LayoutParams(
            LayoutParams.MATCH_PARENT,
            coory
        )
        position2.setMargins(proOb.getCoorXToDrawHorizontalLines().toInt(), 0, 0, 0)
        position2.addRule(BELOW, clock.id)
        gridc.layoutParams = position2
        relativeLayout.addView(gridc)

        scroll.addView(relativeLayout)

        addView(scroll)
    }

    fun addEvents(arrayList: ArrayList<EventModel>) {
//        gridc.addEvents(arrayList)
        gridc.removeAllViews()
        lastCalendar[Calendar.HOUR_OF_DAY] = 0
        lastCalendar[Calendar.MINUTE] = 0
        lastCalendar[Calendar.SECOND] = 0
        val coory = proOb.getCoorYToDrawHorizontalLines()
        arrayList.forEach { element ->
            run {
                val view = element.view
                val color = element.background
                if (proOb.clock_events_filter_transparency) {
                    val transp = Color.argb(
                        round(color.alpha * .8f).toInt(),
                        color.red,
                        color.green,
                        color.blue
                    )
                    view.setBackgroundColor(transp)
                }
                val h1 = element.startTime[Calendar.HOUR_OF_DAY] - proOb.clock_min_hour
                val h2 = element.endTime[Calendar.HOUR_OF_DAY] - proOb.clock_min_hour
                Log.d(TAG, "h1 -> $h1 | h2 -> $h2")
                val cy1 = coory[h1]
                val cy2 = coory[h1 + 1]
                val cy3 = coory[h2]
                val cy4 = coory[h2 + 1]
                val coy1 = if (element.startTime[Calendar.MINUTE] > 0) {
                    cy1 + ((cy2 - cy1) / 59) * element.startTime[Calendar.MINUTE]
                } else
                    cy1
                val coy2 = if (element.endTime[Calendar.MINUTE] > 0) {
                    cy3 + ((cy4 - cy3) / 59) * element.endTime[Calendar.MINUTE]
                } else
                    cy3
                val mart = coy1 - lastCoorY
                val tvlp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (coy2 - coy1).toInt()
                )
                tvlp.setMargins(
                    ((proOb.getCoorXToDrawVerticalLine() - proOb.getCoorXToDrawHorizontalLines()) * 2).toInt(),
                    mart.toInt(),
                    0,
                    0
                )
                view.layoutParams = tvlp
                view.id = View.generateViewId()
                gridc.addView(view)
                lastCoorY = coy2
            }
        }
    }
}