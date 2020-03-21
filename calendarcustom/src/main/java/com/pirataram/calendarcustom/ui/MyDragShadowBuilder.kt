package com.pirataram.calendarcustom.ui

import android.graphics.Point
import android.view.View

class MyDragShadowBuilder : View.DragShadowBuilder() {

    override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
        outShadowSize.set(1, 1)
        outShadowTouchPoint.set(0, 0)
    }
}