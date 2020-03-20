package com.pirataram.calendarcustom.ui

import com.pirataram.calendarcustom.models.PropertiesObject
import java.util.*

interface OneLayoutEvent{

    fun endDrag(startDate: Calendar, endTime: Calendar)

    fun onDragging(newEvent: PropertiesObject.CoorYNewEvent)
}