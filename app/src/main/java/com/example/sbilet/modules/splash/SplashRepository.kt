package com.example.sbilet.modules.splash

import com.example.sbilet.config.SBiletReqeustExecutor
import com.example.sbilet.constants.EQUIPMENT_ID
import com.example.sbilet.constants.VERSION
import com.example.sbilet.util.SBiletSessionInfo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class SplashRepository(private val sBiletReqeustExecutor: SBiletReqeustExecutor) {

    fun singleGetSession(): Single<Unit> {
        return sBiletReqeustExecutor.singleApi(
            endPoint = "client/getsession",
            postParams = mapOf(
                "type" to 3,
                "connection" to JSONObject(),
                "application" to JSONObject().apply {
                    put("version", VERSION)
                    put("equipment-id", EQUIPMENT_ID)
                },
            ),
            isPostParamsJson = true,
            isSessionRequest = true,
            parser = { result ->
                parseTokenInfo(result)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun parseTokenInfo(body: String) {
        val jsonObject = JSONObject(body)
        val dataObject = jsonObject.optJSONObject("data")
        val sessionId = dataObject?.optString("session-id") ?: ""
        val deviceId = dataObject?.optString("device-id") ?: ""
        SBiletSessionInfo.setAll(sessionId, deviceId)
    }
}