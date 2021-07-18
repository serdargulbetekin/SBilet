package com.example.sbilet.modules.journeys

import android.os.Parcelable
import com.example.sbilet.modules.main.bus.BusPlaneLocationItem
import com.example.sbilet.util.SbiletDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationJourneyItem(
    val from: BusPlaneLocationItem,
    val to: BusPlaneLocationItem,
    val date: SbiletDate
) : Parcelable