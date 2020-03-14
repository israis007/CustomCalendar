package com.pirataram.calendarg.ui.models

import java.io.Serializable

data class EventParticipant(val name    : String,
                            val phone   : String,
                            val imageUrl: String?): Serializable {
    constructor(): this("", "", null)
    constructor(name: String, phone: String): this(name, phone, null)
}