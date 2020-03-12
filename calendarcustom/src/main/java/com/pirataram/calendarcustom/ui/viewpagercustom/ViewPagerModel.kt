package com.pirataram.calendarcustom.ui.viewpagercustom

import androidx.lifecycle.MutableLiveData
import java.util.*

class ViewPagerModel {
    companion object {
        val currentPage = MutableLiveData<Int>()
        val selectCalendar = MutableLiveData<Calendar>()
    }
}