package com.pirataram.calendarcustom.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.pirataram.calendarcustom.R
import com.pirataram.calendarcustom.models.PropertiesObject
import com.pirataram.calendarcustom.tools.CanvasDrawer
import com.pirataram.calendarcustom.tools.Constants

class OverLineDrawLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var propertiesObject: PropertiesObject

    constructor(context: Context, propertiesObject: PropertiesObject): this(context){
        this.propertiesObject = propertiesObject
        if (propertiesObject.clock_line_now_show_draw_on == PropertiesObject.Companion.Direction.UP) {
            Constants.calendarChanged.observeForever {
                invalidate()
                requestLayout()
            }
        }
        Constants.heightChange.observeForever {
            val newValue = Constants.heightChange.value!!
            if (newValue > 0f) {
                propertiesObject.clock_text_margin_top = newValue
                invalidate()
                requestLayout()
            }
        }
        setBackgroundColor(ContextCompat.getColor(context, R.color.grid_text))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null)
            return

        if (!propertiesObject.isValidHours())
            return

        //Draw Now Line
        if (propertiesObject.clock_line_now_show_draw_on == PropertiesObject.Companion.Direction.UP &&
            propertiesObject.clock_line_now_show &&
            !propertiesObject.isOutOfHour() &&
            propertiesObject.isToday()
        ) {
            CanvasDrawer.DrawNowLine(propertiesObject, canvas, width.toFloat())
        }
    }
}