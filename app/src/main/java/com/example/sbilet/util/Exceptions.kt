package com.example.sbilet.util


open class CommonException(message: String = "") : Exception(message)

class ContentEmptyException(message: String = "") : CommonException(message)
class CannotReachServerException(message: String = "") : CommonException(message)
class NotAuthorizedException(message: String = "") : CommonException(message)
class NotConnectedException(message: String = "") : CommonException(message)
class ParseException(message: String = "") : CommonException(message)
class ServerException(message: String = "") : CommonException(message)


internal class SBiletApiException(message: String?) : CommonException(
    message ?: "Unknown Error..."
)