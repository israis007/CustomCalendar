package com.pirataram.calendarg.ui.models

import java.io.Serializable

data class AgendaDay(var timestamp  : Long,
                     var events     : ArrayList<AgendaEvent>): Serializable {
    constructor(): this(0L, arrayListOf<AgendaEvent>())
}