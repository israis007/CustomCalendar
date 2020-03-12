package com.pirataram.calendarcustom.ui.viewpagercustom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import com.pirataram.calendarcustom.ui.OneDayLayout
import java.util.*
import kotlin.collections.ArrayList

class ViewPagerCalendar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val TAG = "ViewPagerCalendar"
    private var selected: Int = 0
    private val listDaysView = ArrayList<OneDayLayout>()
    private var viewPager: ViewPager = ViewPager(context)
    private lateinit var proOb: PropertiesObject
    private var viewPagerEvent: ViewPagerEvent? = null

    init {
        context.withStyledAttributes(attrs,
            R.styleable.ViewPagerCalendar,
            defStyleAttr,
            R.style.ViewPagerCalendar){
            proOb = PropertiesObject(Calendar.getInstance())
            val reso = context.resources
            proOb.clock_background = getColor(
                R.styleable.ViewPagerCalendar_clock_background,
                ContextCompat.getColor(context, R.color.clock_background))
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
            proOb.clock_line_now_show_draw_on = PropertiesObject.getDirection(getInt(R.styleable.ViewPagerCalendar_clock_line_now_draw_on, 1))
            proOb.clock_line_now_show_hour = getBoolean(R.styleable.ViewPagerCalendar_clock_line_now_show_hour, true)
            proOb.clock_line_now_show = getBoolean(R.styleable.ViewPagerCalendar_clock_line_now_show, true)
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
        }

        Log.d(TAG, "Propiedades se van alv ===>>>: ${Gson().toJson(proOb)}")

        //Create a list of days to show in cache
        val cal = Calendar.getInstance(Locale.getDefault())
        listDaysView.add(getNewCalendar(cal, Direction.DOWN,  2))
        listDaysView.add(getNewCalendar(cal, Direction.DOWN,  1))
        listDaysView.add(OneDayLayout(context, proOb, cal))
        listDaysView.add(getNewCalendar(cal, Direction.UP,  1))
        listDaysView.add(getNewCalendar(cal, Direction.UP,  2))

        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        viewPager.adapter = ViewPagerAdapter(context, listDaysView)

        lp.addRule(ALIGN_PARENT_END, TRUE)
        lp.addRule(ALIGN_PARENT_TOP, TRUE)
        lp.addRule(ALIGN_PARENT_START, TRUE)
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE)

        viewPager.layoutParams = lp

        this.addView(viewPager)

        viewPager.currentItem =
            if (viewPagerEvent != null)
                viewPagerEvent!!.selectPage()
            else
                2

        if (viewPagerEvent != null)
            viewPagerEvent!!.getAllCustomCalendarViews(listDaysView)

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                if(state == 0){
                    if (selected == listDaysView.size - 1) {
                        addCalendar(selected, Direction.UP)
                    } else if (selected == 0)
                        addCalendar(selected, Direction.DOWN)
                    val selectedDay = listDaysView[selected]
                    if (viewPagerEvent != null) {
                        viewPagerEvent!!.currentPage(selected)
                        viewPagerEvent!!.currentCalendar(selectedDay.calendar)
                        viewPagerEvent!!.getCustomCalendarView(selectedDay)
                        viewPagerEvent!!.getAllCustomCalendarViews(listDaysView)
                        if (viewPagerEvent!!.refreshEventsPreviousDay())
                            listDaysView[selected - 1].addEvents(viewPagerEvent!!.getActivity(), viewPagerEvent!!.addEventsPreviousDay())
                        if (viewPagerEvent!!.refreshEventsToday())
                            selectedDay.addEvents(viewPagerEvent!!.getActivity(), viewPagerEvent!!.addEventsCurrentDay())
                        if (viewPagerEvent!!.refreshEventsNextDay())
                            listDaysView[selected + 1].addEvents(viewPagerEvent!!.getActivity(), viewPagerEvent!!.addEventsNextDay())
                    }
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                selected = position
            }
        })

    }

    fun addViewPagerListeners(viewPagerEvent: ViewPagerEvent){
        this.viewPagerEvent = viewPagerEvent
        val dp = listDaysView[1]
        val dt = listDaysView[2]
        val dn = listDaysView[3]
        dp.addEvents(viewPagerEvent.getActivity(), viewPagerEvent.addEventsPreviousDay())
        dt.addEvents(viewPagerEvent.getActivity(), viewPagerEvent.addEventsCurrentDay())
        dn.addEvents(viewPagerEvent.getActivity(), viewPagerEvent.addEventsNextDay())
    }

    private fun addCalendar(position: Int, direction: Direction){
        val cv = getNewCalendar(
            listDaysView[position].calendar,
            direction,
            1
        )
        if (direction == Direction.UP) {
            listDaysView.add(cv)
            listDaysView.removeAt(0)
        } else {
            listDaysView.add(0, cv)
            listDaysView.removeAt(listDaysView.size - 1)
        }
        viewPager.adapter!!.notifyDataSetChanged()
    }

    private fun getNewCalendar(calendar: Calendar, direction: Direction, times: Int): OneDayLayout {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.timeInMillis = calendar.timeInMillis

        repeat(times){
            if (direction == Direction.UP)
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
        }
        prop.calendar = cal
        return OneDayLayout(context, prop, cal)
    }

    private enum class Direction {
        UP,
        DOWN
    }

}