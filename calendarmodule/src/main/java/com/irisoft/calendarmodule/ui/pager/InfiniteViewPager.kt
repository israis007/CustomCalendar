package com.irisoft.calendarmodule.ui.pager

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.irisoft.calendarmodule.BuildConfig
import com.irisoft.calendarmodule.ui.OnInfinitePageChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfiniteViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    companion object {
        private val TAG: String = InfiniteViewPager::class.java.simpleName
        const val STATE_SUPER = "STATE_SUPER"
        const val STATE_ADAPTER = "STATE_ADAPTER"
    }

    var position: Int = 0
        private set
    var positionOffset: Float = 0f
        private set
    var positionOffsetPixels: Int = 0
        private set

    private var positionCurrent = -1
    var listener: OnInfinitePageChangeListener? = null

    init {
        addOnPageChangeListener(object: OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                @Suppress("UNCHECKED_CAST")
                val adapter = adapter as InfinitePagerAdapter<Any, View>? ?: return
                if (state != ViewPager.SCROLL_STATE_IDLE)
                    return

                setCurrentItem(adapter.setPosition(positionCurrent), false)

                listener?.onPageScrollStateChanged(state)
                this@InfiniteViewPager.position = 0
                this@InfiniteViewPager.positionOffset = 0f
                this@InfiniteViewPager.positionOffsetPixels = 0
                listener?.onPageScrolled(adapter.currentIndicator, 0f, 0)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val adapter = adapter as InfinitePagerAdapter<*, *>? ?: return
                this@InfiniteViewPager.position = position - adapter.currentPosition
                this@InfiniteViewPager.positionOffset = positionOffset
                this@InfiniteViewPager.positionOffsetPixels = positionOffsetPixels
                listener?.onPageScrolled(adapter.currentIndicator, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                positionCurrent = position
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "on page $position")

                val adapter = adapter as InfinitePagerAdapter<*, *>? ?: return
                listener?.onPageSelected(adapter.currentIndicator)
            }
        })
    }

    override fun setCurrentItem(item: Int) {
        if (item != (adapter as InfinitePagerAdapter<*, *>).currentPosition)
            throw UnsupportedOperationException("Cannot change page index unless its 1.")
        super.setCurrentItem(item)
    }

    override fun setOffscreenPageLimit(limit: Int) {
        if (limit != offscreenPageLimit)
            throw UnsupportedOperationException("OffscreenPageLimit cannot be changed.")
        super.setOffscreenPageLimit(limit)
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        if (adapter is InfinitePagerAdapter<*, *>) {
            super.setAdapter(adapter)
            super.setOffscreenPageLimit(adapter.pageCount)
            positionCurrent = adapter.currentPosition
            currentItem = adapter.currentPosition
        } else
            throw IllegalArgumentException("Adapter should be an instance of InfinitePagerAdapter.")
        super.setAdapter(adapter)
    }

    fun <T : Any, V : View> setCurrentIndicator(indicator: T){
        val adapter = adapter as InfinitePagerAdapter<*, *>
        val currentIndicator = adapter.currentIndicator
        if (currentIndicator!!.javaClass != indicator.javaClass)
            return

        GlobalScope.launch(Dispatchers.Main) {
            @Suppress("UNCHECKED_CAST")
            val pagerAdapter = adapter as InfinitePagerAdapter<T, V>
            pagerAdapter.reset(indicator)
            setCurrentItem(pagerAdapter.currentPosition, false)
            listener?.onPageScrollStateChanged(SCROLL_STATE_IDLE)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val adapter = adapter as InfinitePagerAdapter<*, *>?
        if (adapter == null) {
            Log.d(TAG, "onSaveInstanceState adapter == null")
            return super.onSaveInstanceState()
        }

        val bundle = Bundle()
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState())
        bundle.putString(STATE_ADAPTER, adapter.currentIndicatorString)

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val adapter = adapter as InfinitePagerAdapter<*, *>?
        when {
            adapter == null -> {
                if (BuildConfig.DEBUG)
                    Log.w(TAG, "onRestoreInstanceState adapter == null")
                super.onRestoreInstanceState(state)
            }
            state is Bundle -> {
                adapter.currentIndicatorString = state.getString(STATE_ADAPTER)!!
                super.onRestoreInstanceState(state.getParcelable(STATE_SUPER))
            }
            else -> super.onRestoreInstanceState(state)
        }
    }
}