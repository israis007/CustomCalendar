package com.pirataram.calendarg.ui.models

import com.pirataram.calendarg.ui.models.EventParticipant
import java.io.Serializable

data class AgendaGroup(val name: String,
                       val participants: ArrayList<EventParticipant>): Serializable {
    constructor(): this("", arrayListOf())
}