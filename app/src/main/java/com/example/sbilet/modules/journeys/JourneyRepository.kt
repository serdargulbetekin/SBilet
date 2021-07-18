package com.example.sbilet.modules.journeys

import com.example.sbilet.config.SBiletReqeustExecutor
import com.example.sbilet.constants.EQUIPMENT_ID
import com.example.sbilet.constants.VERSION
import com.example.sbilet.modules.main.bus.BusPlaneLocationItem
import com.example.sbilet.util.SbiletDate
import com.example.sbilet.util.serviceFormat
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject

class JourneyRepository(private val sBiletReqeustExecutor: SBiletReqeustExecutor) {

    fun singleJourneyList(
        from: BusPlaneLocationItem,
        to: BusPlaneLocationItem,
        date: SbiletDate,
    ): Single<List<JourneyItem>> {
        return sBiletReqeustExecutor.singleApi(
            endPoint = "journey/getbusjourneys",
            postParams = mapOf(
                "language" to "tr-TR",
                "data" to JSONObject().apply {
                    put("origin-id", from.id)
                    put("destination-id", to.id)
                    put("departure-date", date.serviceFormat())
                }
            ),
            isPostParamsJson = true,
            parser = { result ->
                parseJourney(result)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun parseJourney(body: String): List<JourneyItem> {
        val journeyList = mutableListOf<JourneyItem>()
        val jsonObject = JSONObject(body)

        val dataArray = jsonObject.optJSONArray("data")
        val dataArrayLength = dataArray?.length() ?: 0

        for (index in 0 until dataArrayLength) {
            val dataObject = dataArray?.optJSONObject(index)
            dataObject?.let {
                val journeyObject = it.optJSONObject("journey")
                journeyList.add(
                    JourneyItem(
                        from = it.optString("origin-location") ?: "",
                        to = it.optString("destination-location") ?: "",
                        departure = SbiletDate.convertToSbiletDate(
                            journeyObject?.optString("departure") ?: ""
                        ),
                        arrival = SbiletDate.convertToSbiletDate(
                            journeyObject?.optString("arrival") ?: ""
                        ),
                        price = journeyObject?.optString("original-price") ?: "",
                    )
                )
            }
        }
        return journeyList.toList()
    }
}