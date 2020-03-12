package com.pirataram.calendarcustom.ui.viewpagercustom

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.tools.DateHourFormatter
import com.pirataram.calendarcustom.ui.CustomCalendar

class ViewPagerAdapter(private val context: Context,
                       private val views: ArrayList<CustomCalendar>
) :
    PagerAdapter() {

    private val TAG = "ViewPagerAdapter"

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = views.size

    override fun getItemPosition(`object`: Any): Int {
        for (i in 0 until views.size){
            if (`object` == views[i])
                return i
        }
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = views[position]
        if (v.parent != null)
            (v.parent as ViewGroup).removeView(v)
        container.addView(v)
        return v
    }

    override fun getPageTitle(position: Int): CharSequence? =
        DateHourFormatter.getStringFormatted(views[position].calendar, context.getString(R.string.date_mask))

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

}