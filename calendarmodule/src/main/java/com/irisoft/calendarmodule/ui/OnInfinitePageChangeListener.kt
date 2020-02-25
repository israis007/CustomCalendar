package com.irisoft.calendarmodule.ui

import androidx.viewpager.widget.ViewPager

interface OnInfinitePageChangeListener {

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param indicator Indicator of the first page currently being displayed.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    fun onPageScrolled(indicator: Any?, positionOffset: Float, positionOffsetPixels: Int)

    /**
     * This method will be invoked when a new page has been selected.
     * @param indicator the indicator of this page.
     */
    fun onPageSelected(indicator: Any?)

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager.SCROLL_STATE_IDLE
     * @see ViewPager.SCROLL_STATE_DRAGGING
     * @see ViewPager.SCROLL_STATE_SETTLING
     */
    fun onPageScrollStateChanged(state: Int)

}
