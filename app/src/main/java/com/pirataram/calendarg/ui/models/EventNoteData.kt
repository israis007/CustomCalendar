package com.gentera.agendamodule.model.data

import java.io.Serializable

data class EventNoteData(var username   : String,
                         var modifyDate : String,
                         var imageUrl   : String?,
                         var note       : String?): Serializable {
    constructor()
            : this("", "", null, null)
    constructor(username: String, modifyDate: String)
            : this(username, modifyDate, null, null)
    constructor(username: String, modifyDate: String, note: String)
            : this(username, modifyDate, null, note)
}