package com.pirataram.calendarcustom.ui.viewpagercustom

import com.pirataram.calendarcustom.models.EventModel
import java.util.*

interface ViewPagerEvent {

    fun selectPage(position: Int)

    fun currentPage(): Int

    fun currentCalendar(): Calendar

    fun addEvents(eventList: ArrayList<EventModel>)

}