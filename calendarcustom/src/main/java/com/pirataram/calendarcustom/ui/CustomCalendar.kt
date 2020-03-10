package com.pirataram.calendarcustom.ui

import android.R.attr.factor
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.DrawEventModel
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.properties.Delegates


class CustomCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private lateinit var proOb: PropertiesObject
    private var calendar = Calendar.getInstance(Locale.getDefault())
    private var gridc: LinearCustom
    private var lastCoorY = 0f
    private val TAG = "CustomCalendar"
    private val displayMetrics = DisplayMetrics()
    private var scaleDetector: ScaleGestureDetector
    val scroll = ScrollView(context) as android.widget.ScrollView

    var scrollPosition: Int by Delegates.observable(0) { _, _, new ->
        scroll.scrollY = new
//        Constants.heightChange.value = new
    }

    var hourHeightMin: Float by Delegates.observable(0f) { _, _, new ->
        if (new > 0 && hourHeight < new)
            hourHeight = new
    }
    var hourHeightMax: Float by Delegates.observable(0f) { _, _, new ->
        if (new > 0 && hourHeight > new)
            hourHeight = new
    }

    var hourHeight: Float by Delegates.vetoable(0f) { _, old, new ->
        @Suppress("ComplexCondition")
        if ((hourHeightMin > 0 && new < hourHeightMin)
            || (hourHeightMax > 0 && new > hourHeightMax))
            return@vetoable false
        if (old == new)
            return@vetoable true

        return@vetoable true
    }

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
            proOb.clock_events_opacity_percent = getDimension(
                R.styleable.CustomCalendar_clock_events_opacity_percent,
                Constants.transparency
            )
            proOb.clock_events_padding_between = getDimension(
                R.styleable.CustomCalendar_clock_events_padding_between,
                reso.getDimension(R.dimen.clock_event_padding)
            )
        }

        val scrollParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

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

        scaleDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                Constants.factor = 1.0f
                return super.onScaleBegin(detector)
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                if (detector == null)
                    return false


/*
                val foc = (detector.focusY + scrollPosition) / proOb.clock_text_size
                hourHeight *= detector.currentSpanY / detector.previousSpanY
                scrollPosition = (foc * hourHeight - detector.focusY).toInt()
*/
                val scaleFactor = detector.currentSpanY - detector.previousSpanY
                Constants.factor += scaleFactor.toInt()

                Constants.heightChange.value = Constants.heightChange.value!! * Constants.factor.toInt()

                return true
            }
        })

        addView(scroll)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    fun addEvents(activity: Activity, listaEventos: ArrayList<EventModel>) {
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        //Sort events according start time
        Collections.sort(listaEventos, EventModel.EventModelComparator())
        //Validating if twice or more events sharing time
        val drawList = ArrayList<DrawEventModel>()
        repeat(listaEventos.size){
            val newEventDraw = DrawEventModel(Constants.eventStart, Constants.colsStart, listaEventos[it])
            if (it == 0)
                drawList.add(DrawEventModel(Constants.eventStart, Constants.colsStart, listaEventos[it]))
            else {
                var isAdded = false
                repeat(drawList.size){ it2 ->
                    run {
                        val eventAdded = drawList[it2]
                        newEventDraw.cols = eventAdded.cols
                        newEventDraw.events = eventAdded.events
                        if (newEventDraw.eventModel.startTime.timeInMillis < eventAdded.eventModel.endTime.timeInMillis) {
                            drawList[it2].events++
                            newEventDraw.cols++
                            newEventDraw.events++
                            //Buscar si el evento ya fue agregado
                            drawList.forEach { event -> run {
                                if (event.eventModel.id == newEventDraw.eventModel.id){
                                    isAdded = true
                                }
                            } }
                            if (!isAdded) {
                                drawList.add(newEventDraw)
                                isAdded = true
                            }
                        }
                    }
                }
                if (!isAdded){
                    newEventDraw.cols = Constants.colsStart
                    newEventDraw.events = Constants.eventStart
                    drawList.add(newEventDraw)
                }
            }
        }
        //Validate events not start or end in hours out of time
        drawList.forEach {
            if (it.eventModel.startTime[Calendar.HOUR_OF_DAY] < proOb.clock_min_hour ||
                it.eventModel.startTime[Calendar.HOUR_OF_DAY] > proOb.clock_max_hour ||
                it.eventModel.endTime[Calendar.HOUR_OF_DAY] > proOb.clock_max_hour)
            drawList.remove(it)
        }

        //Remove all views
        gridc.removeAllViews()
        listaEventos.clear()
        val coory = proOb.getCoorYToDrawHorizontalLines()

        //Draws views events
        drawList.forEach { element ->
            run {
                val view = element.eventModel.view
                val color = element.eventModel.background
                if (proOb.clock_events_filter_transparency) {
                    val transp = Color.argb(
                        round(color.alpha * .8f).toInt(),
                        color.red,
                        color.green,
                        color.blue
                    )
                    view.setBackgroundColor(transp)
                }
                val h1 = element.eventModel.startTime[Calendar.HOUR_OF_DAY] - proOb.clock_min_hour
                val h2 = element.eventModel.endTime[Calendar.HOUR_OF_DAY] - proOb.clock_min_hour
                val cy1 = coory[h1]
                val cy2 = coory[h1 + 1]
                val cy3 = coory[h2]
                val cy4 = coory[h2 + 1]
                val coy1 = if (element.eventModel.startTime[Calendar.MINUTE] > 0) {
                    cy1 + ((cy2 - cy1) / 59) * element.eventModel.startTime[Calendar.MINUTE]
                } else
                    cy1
                val coy2 = if (element.eventModel.endTime[Calendar.MINUTE] > 0) {
                    cy3 + ((cy4 - cy3) / 59) * element.eventModel.endTime[Calendar.MINUTE]
                } else
                    cy3
                val mart = coy1 - lastCoorY
                val margin_start_to_draw = (proOb.getCoorXToDrawVerticalLine() - proOb.getCoorXToDrawHorizontalLines()) * 2
                val width_to_draw = displayMetrics.widthPixels - proOb.getCoorXToDrawHorizontalLines() - margin_start_to_draw
                val width_by_event = width_to_draw / element.events - proOb.clock_events_padding_between / Constants.divisorPadding
                val tvlp = LinearLayout.LayoutParams (
                    width_by_event.toInt(),
                    (coy2 - coy1).toInt()
                )
                tvlp.setMargins(
                    if (element.events == 1)
                        margin_start_to_draw.toInt()
                    else
                        (margin_start_to_draw
                                + (width_by_event * element.cols)
                                + (element.cols * (proOb.clock_events_padding_between / Constants.divisorPadding))).toInt(),
                    mart.toInt(),
                    0,
                    0
                )
                view.layoutParams = tvlp
                view.id = View.generateViewId()
                try {
                    gridc.addView(view)
                } catch (il: IllegalStateException){
                    throw Exception("Fail to add a view by: ${il.stackTrace}")
                }
                lastCoorY = coy2
            }
        }
    }
}