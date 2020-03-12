package com.pirataram.calendarcustom.tools

import android.graphics.Canvas
import android.graphics.Rect
import com.pirataram.calendarcustom.models.PropertiesObject
import java.util.*
import kotlin.math.abs

class CanvasDrawer {

    companion object {
        fun DrawNowLine(propertiesObject: PropertiesObject, canvas: Canvas, width: Float){
            val xTemp = propertiesObject.getCoorXToDrawVerticalLine()
            val coorY = propertiesObject.getCoorYToDrawHorizontalLines()
            val paint = propertiesObject.getLineNowPaint()
            val rect = Rect()
            val cale = Calendar.getInstance(Locale.getDefault())
            val diff = propertiesObject.getDifferenceOfHours()
            val difBet = (coorY[diff + 1] - coorY[diff]) / 59
            val yT =
                coorY[diff] + (difBet * cale[Calendar.MINUTE])
            val times = propertiesObject.clock_line_now_height
            var half = NumberHelper.getHalfNumber(times) * -1

            //Draw text hour now
            if (propertiesObject.clock_line_now_show_hour) {
                paint.textSize = propertiesObject.clock_text_size * .8f
                val text = DateHourFormatter.getStringFormatted(
                    cale,
                    propertiesObject.clock_text_mask
                )
                val color = paint.color
                paint.getTextBounds(text, 0, text.length, rect)
                paint.color = propertiesObject.clock_background
                val cx =
                    propertiesObject.getCoorXToDrawHorizontalLines() - rect.width() * 1.25f
                canvas.drawOval(
                    cx * 0.90f,
                    yT - paint.textSize * .50f,
                    propertiesObject.getCoorXToDrawHorizontalLines(),
                    yT + paint.textSize * .75f,
                    paint
                )
                paint.color = color
                canvas.drawText(text, cx, yT + paint.textSize / 2, paint)
            }

            repeat(abs(half) * 2 + 1) {
                canvas.drawLine(xTemp, yT + half, width, yT + half, paint)
                half++
            }
            canvas.drawCircle(xTemp, yT, propertiesObject.clock_line_now_radius, paint)

        }
    }
}