package com.pirataram.calendarcustom.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.Constants
import com.pirataram.calendarcustom.tools.DateHourFormatter
import java.util.*
import kotlin.math.max
import kotlin.properties.Delegates

class ClockLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var propertiesObject: PropertiesObject

    constructor(context: Context, propertiesObject: PropertiesObject): this(context){
        this.propertiesObject = propertiesObject
        _hourHeight = propertiesObject.clock_text_size
    }

    val TAG = ClockLayout::class.java.simpleName
    private var _hourHeight: Float = 0f
    var hourHeight: Float
        get() = _hourHeight
        set(value) {
            val v = value.coerceIn(
                if (hourHeightMin > 0) hourHeightMin else null,
                if (hourHeightMax > 0) hourHeightMax else null
            )
            if (_hourHeight == v)
                return

            _hourHeight = v
            requestLayout()
        }
    var hourHeightMin: Float by Delegates.observable(0f) { _, _, new ->
        if (new > 0 && hourHeight < new)
            hourHeight = new
    }
    var hourHeightMax: Float by Delegates.observable(0f) { _, _, new ->
        if (new > 0 && hourHeight > new)
            hourHeight = new
    }

    private val hourBounds = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "withMS: $widthMeasureSpec  |  heightMS: $heightMeasureSpec")
        val height = paddingTop + paddingBottom + max(
            suggestedMinimumHeight,
            (propertiesObject.getheighByPx() * ((propertiesObject.getHoursToDraw()) + 1)).toInt()
        )
        setMeasuredDimension(getDefaultSize(suggestedMinimumHeight, widthMeasureSpec), height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null)
            return

        if (!propertiesObject.isValidHours())
            return

        val xTemp = propertiesObject.getCoorXToDrawVerticalLine()
        val caln = propertiesObject.getCalendarToDraw()
        val paint = propertiesObject.getClockPaint()
        val coorX = propertiesObject.getCoorXToDrawHorizontalLines()
        val coorY = propertiesObject.getCoorYToDrawHorizontalLines()

        //Draw worktime
        if (propertiesObject.clock_worktime_show) {
            canvas.drawRect(
                propertiesObject.getCoorXToDrawVerticalLine(),
                0f,
                width.toFloat(),
                coorY[propertiesObject.clock_worktime_min_hour - propertiesObject.clock_min_hour],
                propertiesObject.getGridWorkTimePaint()
            )
            canvas.drawRect(
                propertiesObject.getCoorXToDrawVerticalLine(),
                coorY[propertiesObject.clock_worktime_max_hour - propertiesObject.clock_min_hour],
                width.toFloat(),
                height.toFloat(),
                propertiesObject.getGridWorkTimePaint()
            )
        }

        repeat(propertiesObject.getHoursToDraw()) {
            val text = DateHourFormatter.getStringFormatted(caln, propertiesObject.clock_text_mask)
            paint.getTextBounds(text, 0, text.length, hourBounds)
            val gbfc = propertiesObject.getheighByPx() * (it + 1)
            //Draw Text's clock
            canvas.drawText(
                text,
                propertiesObject.clock_text_margin_start,
                gbfc,
                paint
            )
            //Increase a hour each time to draw next hour
            caln[Calendar.HOUR_OF_DAY] = caln[Calendar.HOUR_OF_DAY] + 1
            if (propertiesObject.clock_horizontal_dividers_show)
                canvas.drawLine(
                    coorX,
                    coorY[it],
                    width.toFloat(),
                    coorY[it],
                    propertiesObject.getGridHorizontalPaint()
                )
        }
        //Draw Line Vertical
        if (propertiesObject.clock_vertical_dividers_show)
            canvas.drawLine(xTemp, 0f, xTemp, coorY[propertiesObject.getHoursToDraw()], propertiesObject.getGridVerticalPaint())
    }

}