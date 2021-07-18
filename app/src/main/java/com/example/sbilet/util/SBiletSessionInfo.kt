package com.example.sbilet.util

object SBiletSessionInfo {

    val sessionId: String get() = _sessionId
    val deviceId: String get() = _deviceId

    private var _sessionId: String = ""
    private var _deviceId: String = ""

    fun setSessionId(sessionId: String) {
        _sessionId = sessionId
    }

    fun setDeviceId(deviceId: String) {
        _deviceId = deviceId
    }

    fun setAll(sessionId: String, deviceId: String) {
        _sessionId = sessionId
        _deviceId = deviceId
    }

    fun unSetAll() {
        _sessionId = ""
        _deviceId = ""
    }
}