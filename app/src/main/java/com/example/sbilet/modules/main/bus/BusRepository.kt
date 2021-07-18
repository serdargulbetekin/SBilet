package com.example.sbilet.modules.main.bus


import android.annotation.SuppressLint
import com.example.sbilet.config.SBiletReqeustExecutor
import com.example.sbilet.modules.main.bus.BusViewModel.Companion.CACHED_DATA
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class BusRepository(private val sBiletReqeustExecutor: SBiletReqeustExecutor) {


    @SuppressLint("CheckResult")
    fun getLocations(): Single<List<BusPlaneLocationItem>> {
        if (CACHED_DATA.isNullOrEmpty()) {
            return singleBusLocations().doOnSuccess {
                CACHED_DATA = it
            }
        }
        return Single.just(CACHED_DATA)
    }

    private fun singleBusLocations(): Single<List<BusPlaneLocationItem>> {
        return sBiletReqeustExecutor.singleApi(
            endPoint = "location/getbuslocations",
            postParams = mapOf(
                "language" to "tr-TR",
            ),
            isPostParamsJson = true,
            parser = { result ->
                parseBusLocation(result)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun parseBusLocation(body: String): List<BusPlaneLocationItem> {
        val busLocationItemList = mutableListOf<BusPlaneLocationItem>()
        val jsonObject = JSONObject(body)
        val dataArray = jsonObject.optJSONArray("data")
        val dataArrayLength = dataArray?.length() ?: 0

        for (index in 0 until dataArrayLength) {
            val dataObject = dataArray?.optJSONObject(index)
            dataObject?.let {
                val id = it.optInt("id")
                val parentId = it.optInt("parent-id") ?: 0
                val type = it.optString("type") ?: ""
                val name = it.optString("name") ?: ""
                val geoLocationObject = it.optJSONObject("geo-location")
                val lat = geoLocationObject?.optDouble("latitude") ?: 0.0
                val lng = geoLocationObject?.optDouble("longitude") ?: 0.0
                val zoom = it.optInt("zoom")
                val keywords = it.optString("keywords") ?: ""
                busLocationItemList.add(
                    BusPlaneLocationItem(
                        id = id,
                        parentId = parentId,
                        type = type,
                        name = name,
                        lat = lat,
                        lng = lng,
                        zoom = zoom,
                        keyword = keywords
                    )
                )
            }
        }
        return busLocationItemList
    }
}