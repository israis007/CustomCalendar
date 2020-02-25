package com.irisoft.calendarmodule.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class ClockScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    var onScrollChangeListener: ((Int) -> Unit)? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollChangeListener?.invoke(t)
    }
}