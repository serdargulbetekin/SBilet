package com.example.sbilet.util

import android.content.Context
import com.example.sbilet.modules.journeys.LocationJourneyItem
import com.example.sbilet.modules.main.bus.BusViewModel
import com.example.sbilet.modules.main.plane.PlaneViewModel
import com.google.gson.Gson

class QueryPreferencesHelper(
    context: Context,
    private val gson: Gson
) {

    private val sharedPreferences =
        context.getSharedPreferences("Sbilet", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()


    fun setBusQuery(locationJourneyItem: LocationJourneyItem) {
        val json = gson.toJson(locationJourneyItem)
        editor.putString(BusViewModel.SP_BUS_QUERY, json)
        editor.apply()
    }

    fun getBusQuery(): LocationJourneyItem? {
        val json = sharedPreferences.getString(BusViewModel.SP_BUS_QUERY, "")
        return gson.fromJson(json, LocationJourneyItem::class.java)
    }

    fun setPlaneQuery(locationJourneyItem: LocationJourneyItem) {
        val json = gson.toJson(locationJourneyItem)
        editor.putString(PlaneViewModel.SP_PLANE_QUERY, json)
        editor.apply()
    }

    fun getPlaneQuery(): LocationJourneyItem? {
        val json = sharedPreferences.getString(PlaneViewModel.SP_PLANE_QUERY, "")
        return gson.fromJson(json, LocationJourneyItem::class.java)
    }


}

