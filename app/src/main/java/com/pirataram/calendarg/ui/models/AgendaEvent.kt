package com.pirataram.calendarg.ui.models

import com.gentera.agendamodule.model.data.EventActivity
import com.gentera.agendamodule.model.data.EventNoteData
import java.io.Serializable

data class AgendaEvent(val type         : Int,
                       val title        : String,
                       var initTime     : Long,
                       var endTime      : Long,
                       val place        : String,
                       val placeUrl     : String?,
                       val group        : AgendaGroup,
                       val activity     : ArrayList<EventActivity>,
                       val noteData     : ArrayList<EventNoteData>): Serializable {
    constructor(): this(0, "", 0L, 0L, "",
                        null, AgendaGroup(),
                        arrayListOf(), arrayListOf())
}