package com.pirataram.calendarcustom.models

import java.util.*

data class EventListModel(
    val calendar: Calendar,
    val listEvent: ArrayList<EventModel>
)