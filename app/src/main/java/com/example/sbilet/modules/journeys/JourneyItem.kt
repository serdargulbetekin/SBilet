package com.example.sbilet.modules.journeys

import com.example.sbilet.util.SbiletDate

data class JourneyItem(
    val from: String,
    val to: String,
    val departure: SbiletDate,
    val arrival: SbiletDate,
    val price: String
)