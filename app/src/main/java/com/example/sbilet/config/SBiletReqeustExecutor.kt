package com.example.sbilet.config

import android.content.Context
import com.example.sbilet.constants.BASE_URL
import com.example.sbilet.constants.EQUIPMENT_ID
import com.example.sbilet.constants.FIRST_AUTH_TOKEN
import com.example.sbilet.constants.VERSION
import com.example.sbilet.util.SBiletApiException
import com.example.sbilet.util.SBiletSessionInfo
import io.reactivex.Single
import okhttp3.OkHttpClient
import org.json.JSONObject

class SBiletReqeustExecutor(
    context: Context,
    client: OkHttpClient,
    requestCreator: RequestCreator,
) : RequestExecutor(context, client, requestCreator) {

    fun <T> singleApi(
        endPoint: String,
        postParams: Map<String, Any>? = null,
        isPostParamsJson: Boolean = false,
        isSessionRequest: Boolean = false,
        parser: (String) -> T
    ): Single<T> {
        val jsonParams = if (isPostParamsJson) {
            val jsonObject = JSONObject()
            postParams?.forEach { entry ->
                jsonObject.put(entry.key, entry.value)
            }
            if (!isSessionRequest) {
                val sessionObject = JSONObject().apply {
                    put("session-id", SBiletSessionInfo.sessionId)
                    put("device-id", SBiletSessionInfo.deviceId)
                }
                jsonObject.put("device-session", sessionObject)
            }
            jsonObject
        } else {
            null
        }

        return single(url = getApiUrl() + endPoint,
            headers = getHeaders(),
            formData = if (!isPostParamsJson) postParams?.mapValues { it.value.toString() } else null,
            body = if (isPostParamsJson) jsonParams?.toString() else null,
            parser = { result ->
                val jsonObject = JSONObject(result)
                val isSuccess = jsonObject.optString("status") == "Success"
                val errorDescription = jsonObject.optString("message", "")
                    ?: "Teknik nedenlerden dolayı işleme devam edilemiyor"
                if (!isSuccess) {
                    throw SBiletApiException(message = errorDescription)
                } else {
                    parser(result)
                }
            })


    }

    private fun getHeaders(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put("Authorization", "Basic $FIRST_AUTH_TOKEN")

        }
    }

    private fun getApiUrl(): String {
        return BASE_URL
    }

}

