package com.pirataram.calendarcustom.models

data class DrawEventModel(
    var events: Int = 1,
    var cols: Int = 1,
    val eventModel: EventModel
) {
}