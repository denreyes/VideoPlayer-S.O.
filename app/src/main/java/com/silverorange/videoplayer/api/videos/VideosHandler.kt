package com.silverorange.videoplayer.api.videos

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface VideosHandler {
	@GET("videos")
	fun getVideos(): Call<ResponseBody?>?
}
