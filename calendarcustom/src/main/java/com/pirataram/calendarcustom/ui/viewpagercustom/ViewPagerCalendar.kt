package com.pirataram.calendarcustom.ui.viewpagercustom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.pirataram.calendarcustom.BuildConfig
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import com.pirataram.calendarcustom.tools.DateHourHelper
import com.pirataram.calendarcustom.ui.OneDayLayout
import com.pirataram.calendarcustom.ui.OneLayoutEvent
import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

class ViewPagerCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val TAG = "ViewPagerCalendar"
    private var selected: Int = 0
    private var listDaysView = ArrayList<OneDayLayout>()
    private var viewPager: ViewPager = ViewPager(context)
    private lateinit var proOb: PropertiesObject
    private var viewPagerEvent: ViewPagerEvent? = null
    private var today = 0

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.ViewPagerCalendar,
            defStyleAttr,
            R.style.ViewPagerCalendar
        ) {
            proOb = PropertiesObject(Calendar.getInstance())
            val reso = context.resources
            proOb.clock_background = getColor(
                R.styleable.ViewPagerCalendar_clock_background,
                ContextCompat.getColor(context, R.color.clock_background)
            )
            proOb.clock_text_show = getBoolean(R.styleable.ViewPagerCalendar_clock_text_show, true)
            proOb.clock_text_color = getColor(
                R.styleable.ViewPagerCalendar_clock_text_color,
                ContextCompat.getColor(context, R.color.clock_text_hour)
            )
            proOb.clock_text_size = getDimension(
                R.styleable.ViewPagerCalendar_clock_text_size,
                reso.getDimension(R.dimen.clock_text_height)
            )
            proOb.clock_text_min_size = getDimension(
                R.styleable.ViewPagerCalendar_clock_text_min_size,
                reso.getDimension(R.dimen.clock_text_min_height)
            )
            proOb.clock_text_max_size = getDimension(
                R.styleable.ViewPagerCalendar_clock_text_max_size,
                reso.getDimension(R.dimen.clock_text_max_height)
            )
            proOb.clock_text_margin_start = getDimension(
                R.styleable.ViewPagerCalendar_clock_text_margin_start,
                reso.getDimension(R.dimen.clock_text_padding_start)
            )
            proOb.clock_text_margin_top = getDimension(
                R.styleable.ViewPagerCalendar_clock_text_margin_top,
                reso.getDimension(R.dimen.clock_text_padding_top)
            )
            proOb.clock_text_margin_end = getDimension(
                R.styleable.ViewPagerCalendar_clock_text_margin_end,
                reso.getDimension(R.dimen.clock_text_padding_end)
            )
            val mask = getString(R.styleable.ViewPagerCalendar_clock_text_mask)
            proOb.clock_text_mask =
                if (mask.isNullOrEmpty()) reso.getString(R.string.clock_mask) else mask
            proOb.clock_max_hour = getInteger(
                R.styleable.ViewPagerCalendar_clock_max_hour,
                reso.getInteger(R.integer.clock_max_hour)
            )
            proOb.clock_min_hour = getInteger(
                R.styleable.ViewPagerCalendar_clock_min_hour,
                reso.getInteger(R.integer.clock_min_hour)
            )
            proOb.clock_line_now_show_draw_on = PropertiesObject.getDirection(
                getInt(
                    R.styleable.ViewPagerCalendar_clock_line_now_draw_on,
                    1
                )
            )
            proOb.clock_line_now_show_hour =
                getBoolean(R.styleable.ViewPagerCalendar_clock_line_now_show_hour, true)
            proOb.clock_line_now_show =
                getBoolean(R.styleable.ViewPagerCalendar_clock_line_now_show, true)
            proOb.clock_line_now_color = getColor(
                R.styleable.ViewPagerCalendar_clock_line_now_color,
                ContextCompat.getColor(context, R.color.clock_line_now)
            )
            proOb.clock_line_now_height = getDimension(
                R.styleable.ViewPagerCalendar_clock_line_now_height,
                reso.getDimension(R.dimen.clock_linenow_height)
            )
            proOb.clock_line_now_radius = getDimension(
                R.styleable.ViewPagerCalendar_clock_line_now_radius,
                reso.getDimension(R.dimen.clock_linenow_radius_circle)
            )
            proOb.clock_horizontal_dividers_show =
                getBoolean(R.styleable.ViewPagerCalendar_clock_horizontal_dividers_show, true)
            proOb.clock_horizontal_dividers_color = getColor(
                R.styleable.ViewPagerCalendar_clock_horizontal_dividers_color,
                ContextCompat.getColor(context, R.color.clock_dividers)
            )
            proOb.clock_vertical_dividers_show =
                getBoolean(R.styleable.ViewPagerCalendar_clock_vertical_dividers_show, true)
            proOb.clock_vertical_dividers_color = getColor(
                R.styleable.ViewPagerCalendar_clock_vertical_dividers_color,
                ContextCompat.getColor(context, R.color.clock_dividers)
            )
            proOb.grid_event_at_time = getInteger(
                R.styleable.ViewPagerCalendar_grid_event_at_time,
                reso.getInteger(R.integer.max_event_at_time)
            )
            proOb.grid_event_divider_minutes = getInteger(
                R.styleable.ViewPagerCalendar_grid_event_divider_minutes,
                reso.getInteger(R.integer.divider_each_minutes)
            )
            proOb.clock_worktime_show = getBoolean(
                R.styleable.ViewPagerCalendar_clock_worktime_show,
                true
            )
            proOb.clock_worktime_min_hour = getInteger(
                R.styleable.ViewPagerCalendar_clock_worktime_min_hour,
                reso.getInteger(R.integer.clock_worktime_min_hour)
            )
            proOb.clock_worktime_max_hour = getInteger(
                R.styleable.ViewPagerCalendar_clock_worktime_max_hour,
                reso.getInteger(R.integer.clock_worktime_max_hour)
            )
            proOb.clock_worktime_color = getInteger(
                R.styleable.ViewPagerCalendar_clock_worktime_color,
                ContextCompat.getColor(context, R.color.clock_work_time)
            )
            proOb.clock_events_filter_transparency = getBoolean(
                R.styleable.ViewPagerCalendar_clock_events_filter_transparency,
                true
            )
            proOb.clock_events_opacity_percent = getDimension(
                R.styleable.ViewPagerCalendar_clock_events_opacity_percent,
                Constants.transparency
            )
            proOb.clock_events_padding_between = getDimension(
                R.styleable.ViewPagerCalendar_clock_events_padding_between,
                reso.getDimension(R.dimen.clock_event_padding)
            )
            proOb.clock_max_date = PropertiesObject.getLimits(
                getInteger(
                    R.styleable.ViewPagerCalendar_clock_max_date,
                    3
                )
            )
            proOb.clock_min_date = PropertiesObject.getLimits(
                getInteger(
                    R.styleable.ViewPagerCalendar_clock_min_date,
                    3
                )
            )
            proOb.clock_create_event_enable = getBoolean(R.styleable.ViewPagerCalendar_clock_create_event_enable, true)
            proOb.clock_create_event_enable_toast = getBoolean(R.styleable.ViewPagerCalendar_clock_create_event_enable_toast, true)
            proOb.clock_create_event_space = PropertiesObject.getSpaceEvent(getInteger(R.styleable.ViewPagerCalendar_clock_create_event_space, 0))
            var timeMax = getString(R.styleable.ViewPagerCalendar_clock_max_date_millis)
            var timeMin = getString(R.styleable.ViewPagerCalendar_clock_min_date_millis)
            val cal = DateHourHelper.getCurrentCalendarWithoutHour()
            timeMax = if (!timeMax.isNullOrEmpty())
                try {
                    timeMax.trim().toLong().toString()
                } catch (nb: NumberFormatException) {
                    cal.timeInMillis.toString()
                }
            else
                cal.timeInMillis.toString()
            timeMin = if (!timeMin.isNullOrEmpty())
                try {
                    timeMin.trim().toLong().toString()
                } catch (nb: NumberFormatException) {
                    cal.timeInMillis.toString()
                }
            else
                cal.timeInMillis.toString()
            proOb.clock_max_date_calendar =
                DateHourHelper.getCalendarWithoutHour(Calendar.getInstance(
                    Locale.getDefault()
                ).apply {
                    timeInMillis = timeMax.toLong()
                })
            proOb.clock_min_date_calendar =
                DateHourHelper.getCalendarWithoutHour(Calendar.getInstance(
                    Locale.getDefault()
                ).apply {
                    timeInMillis = timeMin.toLong()
                })
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Properties from XML ===>>>: ${Gson().toJson(proOb)}")

        recreateCache(null)

        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        lp.addRule(ALIGN_PARENT_END, TRUE)
        lp.addRule(ALIGN_PARENT_TOP, TRUE)
        lp.addRule(ALIGN_PARENT_START, TRUE)
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE)

        viewPager.layoutParams = lp

        this.addView(viewPager)

        if (viewPagerEvent != null)
            viewPagerEvent!!.getAllCustomCalendarViews(listDaysView)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                var direction = Direction.RIGHT
                if (state == 0) {
                    if (selected == listDaysView.size - 1 ) {
                        addCalendar(selected, direction)
                    } else if (selected == 0) {
                        direction = Direction.LEFT
                        addCalendar(selected, direction)
                    }
                    if (viewPagerEvent != null) {
                        val selectedDay = listDaysView[selected]
                        viewPagerEvent!!.currentPage(selected)
                        viewPagerEvent!!.currentCalendar(selectedDay.calendar)
                        viewPagerEvent!!.getCustomCalendarView(selectedDay)
                        viewPagerEvent!!.getAllCustomCalendarViews(listDaysView)
                        viewPagerEvent!!.getDirection(direction)
                        if (viewPagerEvent!!.refreshEventsPreviousDay())
                            listDaysView[selected - 1].addEvents(
                                viewPagerEvent!!.getActivity(),
                                viewPagerEvent!!.addEventsPreviousDay()
                            )
                        if (viewPagerEvent!!.refreshEventsToday())
                            selectedDay.addEvents(
                                viewPagerEvent!!.getActivity(),
                                viewPagerEvent!!.addEventsCurrentDay()
                            )
                        if (viewPagerEvent!!.refreshEventsNextDay())
                            listDaysView[selected + 1].addEvents(
                                viewPagerEvent!!.getActivity(),
                                viewPagerEvent!!.addEventsNextDay()
                            )
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                selected = viewPager.currentItem
            }
        })

    }

    fun addViewPagerListeners(viewPagerEvent: ViewPagerEvent) {
        this.viewPagerEvent = viewPagerEvent
        var dp: OneDayLayout? = null
        val dt: OneDayLayout
        var df: OneDayLayout? = null
        if (proOb.clock_max_date == PropertiesObject.Limits.TODAY && proOb.clock_min_date == PropertiesObject.Limits.TODAY) {
            dt = listDaysView[0]
        } else if (proOb.clock_max_date == PropertiesObject.Limits.TODAY && (proOb.clock_min_date == PropertiesObject.Limits.PAST || proOb.clock_min_date == PropertiesObject.Limits.INFINITELY)) {
            dp = listDaysView[listDaysView.size - 2]
            dt = listDaysView[listDaysView.size - 1]
        }else if (proOb.clock_min_date == PropertiesObject.Limits.TODAY && (proOb.clock_max_date == PropertiesObject.Limits.FUTURE || proOb.clock_max_date == PropertiesObject.Limits.INFINITELY)) {
            dt = listDaysView[0]
            df = listDaysView[1]
        } else { //******* INFINITELY TO THE TWO SIDES *******//
            dp = listDaysView[1]
            dt = listDaysView[2]
            df = listDaysView[3]
        }
        if (dp != null && viewPagerEvent.refreshEventsPreviousDay())
            dp.addEvents(viewPagerEvent.getActivity(), viewPagerEvent.addEventsPreviousDay())

        if (viewPagerEvent.refreshEventsToday())
            dt.addEvents(viewPagerEvent.getActivity(), viewPagerEvent.addEventsCurrentDay())

        if (df != null && viewPagerEvent.refreshEventsNextDay())
            df.addEvents(viewPagerEvent.getActivity(), viewPagerEvent.addEventsNextDay())

        proOb.oneLayoutEvent = object: OneLayoutEvent {
            override fun endDrag(startDate: Calendar, endTime: Calendar) {
                viewPagerEvent.onEventCreated(startDate, endTime)
            }

            override fun onDragging(newEvent: PropertiesObject.CoorYNewEvent) {
                viewPagerEvent.onEventDragging(newEvent)
            }
        }

        recreateCache(null)
    }

    private fun addCalendar(position: Int, direction: Direction) {
        if (!(proOb.clock_max_date == PropertiesObject.Limits.TODAY &&
                    proOb.clock_min_date == PropertiesObject.Limits.TODAY)) {
            val cv = getNewCalendar(
                listDaysView[position].calendar,
                direction,
                1
            )

            if (direction == Direction.LEFT){
                when (proOb.clock_min_date){
                    PropertiesObject.Limits.INFINITELY -> addOneDayLayout(cv, direction)
                    PropertiesObject.Limits.TODAY -> {
                        if (DateHourHelper.getCalendarInDays(cv.calendar) >= DateHourHelper.getCurrentCalendarInDays())
                            addOneDayLayout(cv, direction)
                    }
                    else -> { //Have not FUTURE so, is the same case of PAST
                        if (DateHourHelper.getCalendarInDays(cv.calendar) >= DateHourHelper.getCalendarInDays(proOb.clock_min_date_calendar))
                            addOneDayLayout(cv, direction)
                    }
                }
            }
            if (direction == Direction.RIGHT){
                when (proOb.clock_max_date){
                    PropertiesObject.Limits.INFINITELY -> addOneDayLayout(cv, direction)
                    PropertiesObject.Limits.TODAY -> {
                        if (DateHourHelper.getCalendarInDays(cv.calendar) <= DateHourHelper.getCurrentCalendarInDays())
                            addOneDayLayout(cv, direction)
                    }
                    else -> { //Have not PAST so, is the same case of FUTURE
                        if (DateHourHelper.getCalendarInDays(cv.calendar) <= DateHourHelper.getCalendarInDays(proOb.clock_max_date_calendar))
                            addOneDayLayout(cv, direction)
                    }
                }
            }
        }
    }

    private fun addOneDayLayout(oneDayLayout: OneDayLayout, direction: Direction){
        if (direction == Direction.LEFT)
            listDaysView.add(0, oneDayLayout)
        else
            listDaysView.add(oneDayLayout)
        if (listDaysView.size >= context.resources.getInteger(R.integer.cache_all_days))
            listDaysView.removeAt(if (direction == Direction.RIGHT) 0 else listDaysView.size - 1)
        viewPager.adapter!!.notifyDataSetChanged()
    }

    private fun getNewCalendar(calendar: Calendar, direction: Direction, times: Int): OneDayLayout {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.timeInMillis = calendar.timeInMillis

        repeat(times) {
            if (direction == Direction.RIGHT)
                cal[Calendar.DAY_OF_MONTH] += 1
            else
                cal[Calendar.DAY_OF_MONTH] -= 1
        }
        val prop = PropertiesObject(cal)
        prop.apply {
            clock_background = proOb.clock_background
            clock_text_show = proOb.clock_text_show
            clock_text_color = proOb.clock_text_color
            clock_text_size = proOb.clock_text_size
            clock_text_min_size = proOb.clock_text_min_size
            clock_text_max_size = proOb.clock_text_max_size
            clock_text_margin_start = proOb.clock_text_margin_start
            clock_text_margin_top = proOb.clock_text_margin_top
            clock_text_margin_end = proOb.clock_text_margin_end
            clock_text_mask = proOb.clock_text_mask
            clock_max_hour = proOb.clock_max_hour
            clock_min_hour = proOb.clock_min_hour
            clock_line_now_show_hour = proOb.clock_line_now_show_hour
            clock_line_now_show = proOb.clock_line_now_show
            clock_line_now_color = proOb.clock_line_now_color
            clock_line_now_radius = proOb.clock_line_now_radius
            clock_line_now_height = proOb.clock_line_now_height
            clock_vertical_dividers_show = proOb.clock_vertical_dividers_show
            clock_vertical_dividers_color = proOb.clock_vertical_dividers_color
            clock_horizontal_dividers_show = proOb.clock_horizontal_dividers_show
            clock_horizontal_dividers_color = proOb.clock_horizontal_dividers_color
            grid_event_at_time = proOb.grid_event_at_time
            grid_event_divider_minutes = proOb.grid_event_divider_minutes
            clock_worktime_show = proOb.clock_worktime_show
            clock_worktime_min_hour = proOb.clock_worktime_min_hour
            clock_worktime_max_hour = proOb.clock_worktime_max_hour
            clock_worktime_color = proOb.clock_worktime_color
            clock_events_filter_transparency = proOb.clock_events_filter_transparency
            clock_events_opacity_percent = proOb.clock_events_opacity_percent
            clock_events_padding_between = proOb.clock_events_padding_between
            clock_max_date = proOb.clock_max_date
            clock_min_date = proOb.clock_min_date
            clock_max_date_calendar = proOb.clock_max_date_calendar
            clock_min_date_calendar = proOb.clock_min_date_calendar
            clock_create_event_enable = proOb.clock_create_event_enable
            clock_create_event_space = proOb.clock_create_event_space
            clock_create_event_enable_toast = proOb.clock_create_event_enable_toast
        }
        prop.calendar = cal
        return OneDayLayout(context, prop, cal)
    }

    enum class Direction {
        RIGHT,
        LEFT
    }

    private fun addPast(times: Int, cal: Calendar) {
        repeat(times) {
            listDaysView.add(0, getNewCalendar(cal, Direction.LEFT, it + 1))
        }
    }

    private fun addFuture(times: Int, cal: Calendar) {
        repeat(times) {
            listDaysView.add(getNewCalendar(cal, Direction.RIGHT, it + 1))
        }
    }

    fun moveToday(){
        recreateCache(null)
    }

    private fun recreateCache(calendar: Calendar?) {
        listDaysView = ArrayList()
        //Create a list of days to show in cache
        val cal = calendar ?: DateHourHelper.getCurrentCalendarWithoutHour()
        val cache = context.resources.getInteger(R.integer.cache_days)
        val daysPast = proOb.getTotalDaysPast(calendar).toInt()
        val daysFuture = proOb.getTotalDaysFuture(calendar).toInt()

        var times = if (daysPast > cache) cache else daysPast
        if (times > 0)
            addPast(times, cal)
        today = listDaysView.size
        listDaysView.add(OneDayLayout(context, proOb, cal)) //Add Today
        times = if (daysFuture > cache) cache else daysFuture
        if (times > 0)
            addFuture(times, cal)

        selected = today

        viewPager.adapter = ViewPagerAdapter(context, listDaysView)

        viewPager.currentItem = today

        viewPager.adapter!!.notifyDataSetChanged()

        if (this.viewPagerEvent != null) {
            if (proOb.clock_max_date != PropertiesObject.Limits.TODAY && proOb.clock_min_date != PropertiesObject.Limits.TODAY) {
                val restPast = proOb.getTotalDaysPast(null)
                val restFuture = proOb.getTotalDaysFuture(null)
                var vp: OneDayLayout? = null
                val vt = listDaysView[today]
                var vf: OneDayLayout? = null

                if (restPast >= 1L) {
                    vp = listDaysView[today - 1]
                }
                if (restFuture >= 1L)
                    vf = listDaysView[today + 1]

                if (vp != null && this.viewPagerEvent!!.refreshEventsPreviousDay())
                    vp.addEvents(
                        this.viewPagerEvent!!.getActivity(),
                        this.viewPagerEvent!!.addEventsPreviousDay()
                    )

                if (this.viewPagerEvent!!.refreshEventsToday())
                    vt.addEvents(
                        this.viewPagerEvent!!.getActivity(),
                        this.viewPagerEvent!!.addEventsCurrentDay()
                    )

                if (vf != null && this.viewPagerEvent!!.refreshEventsNextDay())
                    vf.addEvents(
                        this.viewPagerEvent!!.getActivity(),
                        this.viewPagerEvent!!.addEventsNextDay()
                    )

            } else if (this.viewPagerEvent!!.refreshEventsToday()) {
                listDaysView[0].addEvents(
                    this.viewPagerEvent!!.getActivity(),
                    this.viewPagerEvent!!.addEventsCurrentDay()
                )
            }
        }
    }

    /**
     * Consider that position will be minor of cache pages
     */
    fun moveToPage(position: Int) {
        viewPager.currentItem = position
    }

    fun moveToDate(calendar: Calendar){
        if (isDateInRange(calendar) == PropertiesObject.Limits.TODAY)
            if (DateHourHelper.getCalendarInDays(calendar) == DateHourHelper.getCurrentCalendarInDays())
                recreateCache(null)
            else
                throw Exception("Date out of Range")
        else
            recreateCache(calendar)
    }

    private fun isDateInRange(calendar: Calendar): PropertiesObject.Limits {
        return if (validateDate(proOb.clock_min_date, calendar) && validateDate(proOb.clock_max_date, calendar))
            PropertiesObject.Limits.INFINITELY
        else
            if (validateDate(proOb.clock_min_date, calendar))
                PropertiesObject.Limits.PAST
            else if (validateDate(proOb.clock_max_date, calendar))
                PropertiesObject.Limits.FUTURE
            else
                PropertiesObject.Limits.TODAY
    }

    private fun validateDate(limits: PropertiesObject.Limits, calendar: Calendar): Boolean {
        return when(limits){
            PropertiesObject.Limits.PAST -> {
                DateHourHelper.getCalendarInDays(calendar) >= DateHourHelper.getCalendarInDays(proOb.clock_min_date_calendar)
            }
            PropertiesObject.Limits.TODAY -> {
                when {
                    DateHourHelper.getCalendarInDays(calendar) < DateHourHelper.getCurrentCalendarInDays() -> false
                    DateHourHelper.getCalendarInDays(calendar) > DateHourHelper.getCurrentCalendarInDays() -> false
                    else -> true
                }
            }
            PropertiesObject.Limits.FUTURE -> {
                DateHourHelper.getCalendarInDays(calendar) <= DateHourHelper.getCalendarInDays(proOb.clock_max_date_calendar)
            }
            PropertiesObject.Limits.INFINITELY -> true
        }
    }

    fun setMaxDate(cal: Calendar) {
        this.proOb.clock_max_date_calendar = cal
        recreateCache(null)
    }

    fun setMinDate(cal: Calendar) {
        this.proOb.clock_min_date_calendar = cal
        recreateCache(null)
    }

    fun setViewNewEvent(view: View){
        this.proOb.viewNewEvent = view
        recreateCache(null)
    }

}