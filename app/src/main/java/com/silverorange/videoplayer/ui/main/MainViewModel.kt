package com.silverorange.videoplayer.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.silverorange.videoplayer.api.ApiRequestListener
import com.silverorange.videoplayer.api.models.Videos
import com.silverorange.videoplayer.api.videos.VideosApiCall
import com.silverorange.videoplayer.utils.AppLogger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    var videos = ArrayList<Videos>()

    private val _emptyVideo = MutableLiveData<Videos>().apply {
        value = Videos()
    }
    var video: MutableLiveData<Videos> = _emptyVideo

    private val _nextVideoExists = MutableLiveData<Boolean>().apply {
        value = false
    }
    var nextVideoExists: MutableLiveData<Boolean> = _nextVideoExists

    private val _prevVideoExists = MutableLiveData<Boolean>().apply {
        value = false
    }
    var prevVideoExists: MutableLiveData<Boolean> = _prevVideoExists
    var index = 0

    fun fetchVideos() {
        VideosApiCall.instance?.getVideos(object : ApiRequestListener {

            override fun onSuccess(obj: Any?) {
                try {
                    val jsonItems = JSONArray(obj.toString())
                    videos = Gson().fromJson(
                        jsonItems.toString(),
                        object : TypeToken<List<Videos>>() {}.type
                    )
                    if(videos.size > 0) {
                        index = 0
                        video.value = videos[index]
                    }
                    nextVideoExists.value = videos.size > 0
                    prevVideoExists.value = false
                } catch (e: JSONException) {
                    onError(e)
                }
            }

            override fun onError(error: Any?) {
                AppLogger.longInfo("DENSHO", "ERROR! = " + error)
            }
        })
    }

    fun nextVideo() {
        if(videos.size > 0 && index < videos.size - 1) {
            index++
            video.value = videos[index]

            nextVideoExists.value = index < videos.size - 1
            prevVideoExists.value = index > 0
        }
    }

    fun prevVideo() {
        if(videos.size > 0 && index > 0) {
            index--
            video.value = videos[index]

            nextVideoExists.value = index < videos.size - 1
            prevVideoExists.value = index > 0
        }
    }
}
