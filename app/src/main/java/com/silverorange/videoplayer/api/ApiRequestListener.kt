package com.silverorange.videoplayer.api

interface ApiRequestListener {
	fun onSuccess(obj: Any?)
	fun onError(error: Any?)
}
