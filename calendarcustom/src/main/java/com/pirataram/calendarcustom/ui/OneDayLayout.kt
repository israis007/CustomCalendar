package com.pirataram.calendarcustom.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.google.gson.Gson
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.DrawEventModel
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import com.pirataram.calendarcustom.tools.DateHourFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round


class OneDayLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var proOb: PropertiesObject
    var calendar = Calendar.getInstance(Locale.getDefault())
    private lateinit var linearEvents: LinearLayout
    private lateinit var linearNewEvents: OverLineDrawLayout
    private var lastCoorY = 0f
    private val TAG = "CustomCalendar"
    private val displayMetrics = DisplayMetrics()
    private var scaleDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f
    private var lastValue = 0f
    private var drawList = ArrayList<DrawEventModel>()
    private lateinit var clock: View
    private lateinit var coory: FloatArray
    private var scroll = ScrollView(context) as android.widget.ScrollView

    constructor(context: Context, calendar: Calendar): this(context){
        this.calendar = calendar
        proOb.calendar = calendar
        reCalcViews()
    }

    constructor(context: Context, propertiesObject: PropertiesObject, calendar: Calendar): this(context){
        this.calendar = calendar
        this.proOb = propertiesObject
        this.proOb.calendar = calendar
        Log.d(TAG, "Propiedades: ${Gson().toJson(proOb)}")
        reCalcViews()
    }

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.OneDayLayout,
            defStyleAttr,
            R.style.OneDayLayout
        ) {
            proOb = PropertiesObject(calendar)
            val reso = context.resources
            proOb.clock_background = getColor(
                R.styleable.OneDayLayout_clock_background,
                ContextCompat.getColor(context, R.color.clock_background))
            proOb.clock_text_show = getBoolean(R.styleable.OneDayLayout_clock_text_show, true)
            proOb.clock_text_color = getColor(
                R.styleable.OneDayLayout_clock_text_color,
                ContextCompat.getColor(context, R.color.clock_text_hour)
            )
            proOb.clock_text_size = getDimension(
                R.styleable.OneDayLayout_clock_text_size,
                reso.getDimension(R.dimen.clock_text_height)
            )
            proOb.clock_text_min_size = getDimension(
                R.styleable.OneDayLayout_clock_text_min_size,
                reso.getDimension(R.dimen.clock_text_min_height)
            )
            proOb.clock_text_max_size = getDimension(
                R.styleable.OneDayLayout_clock_text_max_size,
                reso.getDimension(R.dimen.clock_text_max_height)
            )
            proOb.clock_text_margin_start = getDimension(
                R.styleable.OneDayLayout_clock_text_margin_start,
                reso.getDimension(R.dimen.clock_text_padding_start)
            )
            proOb.clock_text_margin_top = getDimension(
                R.styleable.OneDayLayout_clock_text_margin_top,
                reso.getDimension(R.dimen.clock_text_padding_top)
            )
            proOb.clock_text_margin_end = getDimension(
                R.styleable.OneDayLayout_clock_text_margin_end,
                reso.getDimension(R.dimen.clock_text_padding_end)
            )
            val mask = getString(R.styleable.OneDayLayout_clock_text_mask)
            proOb.clock_text_mask =
                if (mask.isNullOrEmpty()) reso.getString(R.string.clock_mask) else mask
            proOb.clock_max_hour = getInteger(
                R.styleable.OneDayLayout_clock_max_hour,
                reso.getInteger(R.integer.clock_max_hour)
            )
            proOb.clock_min_hour = getInteger(
                R.styleable.OneDayLayout_clock_min_hour,
                reso.getInteger(R.integer.clock_min_hour)
            )
            proOb.clock_line_now_show_draw_on = PropertiesObject.getDirection(getInt(R.styleable.OneDayLayout_clock_line_now_draw_on, 1))
            proOb.clock_line_now_show_hour = getBoolean(R.styleable.OneDayLayout_clock_line_now_show_hour, true)
            proOb.clock_line_now_show = getBoolean(R.styleable.OneDayLayout_clock_line_now_show, true)
            proOb.clock_line_now_color = getColor(
                R.styleable.OneDayLayout_clock_line_now_color,
                ContextCompat.getColor(context, R.color.clock_line_now)
            )
            proOb.clock_line_now_height = getDimension(
                R.styleable.OneDayLayout_clock_line_now_height,
                reso.getDimension(R.dimen.clock_linenow_height)
            )
            proOb.clock_line_now_radius = getDimension(
                R.styleable.OneDayLayout_clock_line_now_radius,
                reso.getDimension(R.dimen.clock_linenow_radius_circle)
            )
            proOb.clock_horizontal_dividers_show =
                getBoolean(R.styleable.OneDayLayout_clock_horizontal_dividers_show, true)
            proOb.clock_horizontal_dividers_color = getColor(
                R.styleable.OneDayLayout_clock_horizontal_dividers_color,
                ContextCompat.getColor(context, R.color.clock_dividers)
            )
            proOb.clock_vertical_dividers_show =
                getBoolean(R.styleable.OneDayLayout_clock_vertical_dividers_show, true)
            proOb.clock_vertical_dividers_color = getColor(
                R.styleable.OneDayLayout_clock_vertical_dividers_color,
                ContextCompat.getColor(context, R.color.clock_dividers)
            )
            proOb.grid_event_at_time = getInteger(
                R.styleable.OneDayLayout_grid_event_at_time,
                reso.getInteger(R.integer.max_event_at_time)
            )
            proOb.grid_event_divider_minutes = getInteger(
                R.styleable.OneDayLayout_grid_event_divider_minutes,
                reso.getInteger(R.integer.divider_each_minutes)
            )
            proOb.clock_worktime_show = getBoolean(
                R.styleable.OneDayLayout_clock_worktime_show,
                true
            )
            proOb.clock_worktime_min_hour = getInteger(
                R.styleable.OneDayLayout_clock_worktime_min_hour,
                reso.getInteger(R.integer.clock_worktime_min_hour)
            )
            proOb.clock_worktime_max_hour = getInteger(
                R.styleable.OneDayLayout_clock_worktime_max_hour,
                reso.getInteger(R.integer.clock_worktime_max_hour)
            )
            proOb.clock_worktime_color = getInteger(
                R.styleable.OneDayLayout_clock_worktime_color,
                ContextCompat.getColor(context, R.color.clock_work_time)
            )
            proOb.clock_events_filter_transparency = getBoolean(
                R.styleable.OneDayLayout_clock_events_filter_transparency,
                true
            )
            proOb.clock_events_opacity_percent = getDimension(
                R.styleable.OneDayLayout_clock_events_opacity_percent,
                Constants.transparency
            )
            proOb.clock_events_padding_between = getDimension(
                R.styleable.OneDayLayout_clock_events_padding_between,
                reso.getDimension(R.dimen.clock_event_padding)
            )
        }

        reCalcViews()

        scaleDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                if (detector == null)
                    return false

                mScaleFactor *= detector.scaleFactor
                val minValue = 3.0f
                val maxValue = 9.0f
                mScaleFactor = max(minValue, min(mScaleFactor, maxValue))
                Log.d(TAG, "Scale factor calculated: $mScaleFactor -> lastValue: $lastValue")

                var newValue = proOb.clock_text_margin_top
                if (newValue >= proOb.clock_text_min_size &&
                    newValue <= proOb.clock_text_max_size) {
                    if (mScaleFactor == minValue || lastValue > mScaleFactor)
                        newValue -= mScaleFactor
                    else if (mScaleFactor == maxValue || lastValue < mScaleFactor)
                        newValue += mScaleFactor

                    if (newValue <= proOb.clock_text_min_size)
                        newValue = proOb.clock_text_min_size
                    if (newValue >= proOb.clock_text_max_size)
                        newValue = proOb.clock_text_max_size

                    Constants.heightChange.value = newValue
                    proOb.clock_text_margin_top = newValue
                    Log.d("CustomCalendar", "newValue = $newValue")
                }
                lastValue = abs(mScaleFactor)
                return true
            }
        })

        Constants.heightChange.observeForever {
            val newValue = Constants.heightChange.value!!
            if (newValue > 0f) {
                proOb.clock_text_margin_top = newValue
                Log.d("CustomCalendar", "newValue = $newValue")
                invalidate()
                requestLayout()
                printEvents()
            }
        }
    }

    private fun reCalcViews(){
        removeAllViews()
        scroll = ScrollView(context)
        val scrollParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        scrollParams.addRule(ALIGN_PARENT_END, TRUE)
        scrollParams.addRule(ALIGN_PARENT_TOP, TRUE)
        scrollParams.addRule(ALIGN_PARENT_START, TRUE)
        scrollParams.addRule(ALIGN_PARENT_BOTTOM, TRUE)
        scroll.layoutParams = scrollParams

        clock = ClockLayout(context, proOb)
        linearEvents = LinearLayout(context)
        linearNewEvents = OverLineDrawLayout(context, proOb)
        linearEvents.id = View.generateViewId()
        linearEvents.orientation = LinearLayout.VERTICAL
        linearNewEvents.orientation = LinearLayout.VERTICAL
        val relativeLayout = RelativeLayout(context)
        val lpClock = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        lpClock.addRule(CENTER_IN_PARENT)
        clock.layoutParams = lpClock
        relativeLayout.addView(clock)

        calculateNewParams()

        linearEvents.setOnClickListener {
            
        }

        linearEvents.setOnLongClickListener {
            Toast.makeText(context, DateHourFormatter.getStringFormatted(proOb.calendar, context.getString(R.string.date_mask)), Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.TOP, 0,0)
                val tv = this.view.findViewById<TextView>(android.R.id.message)
                tv.textSize = proOb.clock_text_size
            }.show()
            true
        }

        relativeLayout.addView(linearEvents)
        relativeLayout.addView(linearNewEvents)

        scroll.addView(relativeLayout)

        addView(scroll)

        setBackgroundColor(proOb.clock_background)

    }

    private fun calculateNewParams(){
        //Calculates margin start and height
        coory = proOb.getCoorYToDrawHorizontalLines()
        val lpLinearEvents = LayoutParams(
            LayoutParams.MATCH_PARENT,
            coory[proOb.getHoursToDraw()].toInt()
        )
        lpLinearEvents.setMargins(proOb.getCoorXToDrawHorizontalLines().toInt(), 0, 0, 0)
        lpLinearEvents.addRule(BELOW, clock.id)
        lpLinearEvents.addRule(ALIGN_PARENT_START, TRUE)
        lpLinearEvents.addRule(ALIGN_PARENT_END, TRUE)
        linearEvents.layoutParams = lpLinearEvents
        val lpLinearNewEvents = LayoutParams(
            LayoutParams.MATCH_PARENT,
            coory[proOb.getHoursToDraw()].toInt()
        )
        lpLinearNewEvents.setMargins(0, 0, 0, 0)
        lpLinearNewEvents.addRule(ALIGN_START, clock.id)
        lpLinearNewEvents.addRule(ALIGN_END, clock.id)
        lpLinearNewEvents.addRule(ALIGN_TOP, clock.id)
        linearNewEvents.layoutParams = lpLinearNewEvents
    }

    fun addEvents(activity: Activity, listaEventos: ArrayList<EventModel>) {
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        //Sort events according start time
        Collections.sort(listaEventos, EventModel.EventModelComparator())
        //Validating if twice or more events sharing time
        drawList = ArrayList()
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
        //Remove Parents
        drawList.forEach {
            val v = it.eventModel.view
            if (v.parent != null)
                (v.parent as ViewGroup).removeView(v)
        }
        //Print Events
        printEvents()
    }

    private fun printEvents(){
        lastCoorY = 0f
        //Remove all views
        linearEvents.removeAllViews()
        //Recalc params
        calculateNewParams()
        //Draws views events
        drawList.forEach { element ->
            run {
                val view = element.eventModel.view
                val color = element.eventModel.background
                if (proOb.clock_events_filter_transparency && color != null && color > 0) {
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
                    linearEvents.addView(view)
                } catch (il: IllegalStateException){
                    Log.d(TAG, "Fail to add a view by: ${il.stackTrace}")
                }
                lastCoorY = coy2
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    fun moveScrollUp(){
        scroll.scrollTo(0, 0)
    }

    fun moveScrollCenter(){
        scroll.scrollTo(0, height / 2)
    }

    fun moveScrollDown(){
        scroll.scrollTo(0, height)
    }
}