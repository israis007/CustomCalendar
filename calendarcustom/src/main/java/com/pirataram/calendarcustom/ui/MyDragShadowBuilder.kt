package com.pirataram.calendarcustom.ui

import android.graphics.Point
import android.view.View

class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

    override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
        outShadowSize.set(100, 100)
        outShadowTouchPoint.set(50, 50)
    }
}