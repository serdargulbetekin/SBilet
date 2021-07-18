package com.example.sbilet.modules.main.bus

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusPlaneLocationItem(
    val id: Int,
    val parentId: Int,
    val type: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val zoom: Int,
    val keyword: String
):Parcelable