package com.gentera.agendamodule.model.data

import java.io.Serializable

class EventActivity(val resume  : String,
                    val code    : String,
                    val tasks   : ArrayList<String /* ActivityTask */>?): Serializable {
    constructor(): this("", "", null)
    constructor(resume: String, code: String)
            : this(resume, code, null)
}