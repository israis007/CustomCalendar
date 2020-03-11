package com.pirataram.calendarcustom.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.EventModel
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import com.pirataram.calendarcustom.tools.NumberHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class LinearCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var propertiesObject: PropertiesObject
    private var lastCalendar = Calendar.getInstance()

    constructor(context: Context, propertiesObject: PropertiesObject) : this(context) {
        this.propertiesObject = propertiesObject
        orientation = VERTICAL
        setBackgroundColor(ContextCompat.getColor(context, R.color.grid_text))
    }


    private fun rePaint(){
        invalidate()
        requestLayout()
    }

    /*override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null)
            return

        if (!propertiesObject.isValidHours())
            return

        val xTemp = propertiesObject.clock_text_margin_end / 2
        val coorY = propertiesObject.getCoorYToDrawHorizontalLines()

        //Draw Now Line
        if (propertiesObject.clock_line_show) {
            if (!propertiesObject.isOutOfHour()) {
                if (propertiesObject.isToday()) {
                    val paint = propertiesObject.getLineNowPaint()
                    val diff = propertiesObject.getDifferenceOfHours()
                    val difBet = (coorY[diff + 1] - coorY[diff]) / 59
                    val yT =
                        coorY[diff] + (difBet * Calendar.getInstance(Locale.getDefault())[Calendar.MINUTE])
                    val times = propertiesObject.clock_line_now_height
                    var half = NumberHelper.getHalfNumber(times) * -1
                    repeat(abs(half) * 2 + 1) {
                        canvas.drawLine(xTemp, yT + half, width.toFloat(), yT + half, paint)
                        half++
                    }
                    canvas.drawCircle(xTemp, yT, propertiesObject.clock_line_now_radius, paint)
                }
            }
        }
    }*/
}