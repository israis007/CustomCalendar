package com.pirataram.calendarcustom.ui.viewpagercustom

import android.app.Activity
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarcustom.ui.OneDayLayout
import java.util.*
import kotlin.collections.ArrayList

interface ViewPagerEvent {

    fun selectPage(): Int

    fun currentPage(position: Int)

    fun currentCalendar(calendar: Calendar)

    fun addEventsPreviousDay(): ArrayList<EventModel>

    fun addEventsCurrentDay(): ArrayList<EventModel>

    fun addEventsNextDay(): ArrayList<EventModel>

    fun refreshEventsPreviousDay(): Boolean

    fun refreshEventsToday(): Boolean

    fun refreshEventsNextDay(): Boolean

    fun getActivity(): Activity

    fun getCustomCalendarView(oneDayLayout: OneDayLayout)

    fun getAllCustomCalendarViews(arrayList: ArrayList<OneDayLayout>)

}