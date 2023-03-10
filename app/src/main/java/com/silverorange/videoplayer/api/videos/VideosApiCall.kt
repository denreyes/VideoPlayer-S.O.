package com.silverorange.videoplayer.api.videos

import com.silverorange.videoplayer.api.ApiConnector
import com.silverorange.videoplayer.api.ApiRequestListener
import com.silverorange.videoplayer.api.BaseCall

class VideosApiCall private constructor() : BaseCall() {
	private val handler =
		ApiConnector.instance!!.createService(VideosHandler::class.java) as VideosHandler

	fun getVideos(listener: ApiRequestListener?) {
		val call = handler.getVideos()
		call(call, listener!!)
	}

	companion object {
		var instance: VideosApiCall? = null
			get() {
				if (field == null) {
					field = VideosApiCall()
				}
				return field
			}
			private set
	}
}
