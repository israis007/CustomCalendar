package com.irisoft.calendarmodule.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.irisoft.calendarmodule.R
import java.util.*
import kotlin.math.max
import kotlin.properties.Delegates

class HoursView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View (context, attrs, defStyleAttr) {

    private var _hourHeight: Float = 0f
    var hourHeight: Float
        get() = _hourHeight
        set(value) {
            val v = value.coerceIn(if (hourHeightMin > 0) hourHeightMin else null,
                if (hourHeightMax > 0) hourHeightMax else null)
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

    private var hourSize: Int = 0
    private var hourColor: Int = 0
    private lateinit var hourPaint: TextPaint
    private var hourMask: String = context.getString(R.string.hour_mask)

    init {
        context.withStyledAttributes(attrs, R.styleable.HoursView, defStyleAttr, R.style.Calendar_HoursViewStyle) {
            _hourHeight = getDimension(R.styleable.HoursView_hourHeight, 0f)
            hourHeightMin = getDimension(R.styleable.HoursView_hourHeightMin, 0f)
            hourHeightMax = getDimension(R.styleable.HoursView_hourHeightMax, 0f)
            val text = getString(R.styleable.HoursView_hourMask)
            hourMask = if (!text.isNullOrEmpty()) text else context.getString(R.string.hour_mask)
            hourSize = getDimensionPixelSize(R.styleable.HoursView_hourSize, 0)
            hourColor = getColor(R.styleable.HoursView_hourColor, Color.BLACK)
            hourPaint = TextPaint().apply {
                color = hourColor
                isAntiAlias = true
                textSize = hourSize.toFloat()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_hourHeight * DAY_IN_HOURS).toInt())
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            height)
    }

    private val hourBounds = Rect()
    private val calendar = Calendar.getInstance(Locale.getDefault())

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null)
            return

        val left = paddingLeft
        val top = paddingTop
        val right = width - paddingRight

        fun getStartForCentered(width: Float): Float {
            return left.toFloat() + (right - left - width) / 2
        }

        fun getBottomForCentered(center: Float, height: Int): Float {
            return center + height / 2
        }

        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR_OF_DAY] = 0
        val hours = DAY_IN_HOURS + 10
        repeat(hours){
            val text = Tools.getFormatedTextFromDate(calendar, hourMask).trim()
            hourPaint.getTextBounds(text, 0, text.length, hourBounds)
            canvas.drawText(text,
                getStartForCentered(hourBounds.width().toFloat()),
                getBottomForCentered(top + _hourHeight * (it + 1), hourBounds.height()),
                hourPaint)
            calendar[Calendar.HOUR_OF_DAY] = if (calendar[Calendar.HOUR_OF_DAY] >= 23) 0 else calendar[Calendar.HOUR_OF_DAY] + 1
        }
    }
}