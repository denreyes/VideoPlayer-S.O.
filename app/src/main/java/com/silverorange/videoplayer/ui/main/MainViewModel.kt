package com.silverorange.videoplayer.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.silverorange.videoplayer.api.ApiRequestListener
import com.silverorange.videoplayer.api.models.Videos
import com.silverorange.videoplayer.api.videos.VideosApiCall
import com.silverorange.videoplayer.utils.AppLogger
import com.silverorange.videoplayer.utils.Constants
import org.json.JSONArray
import org.json.JSONException

class MainViewModel : ViewModel() {

    var videos = ArrayList<Videos>()

    private val _emptyVideo = MutableLiveData<Videos>().apply {
        value = Videos()
    }
    var video: MutableLiveData<Videos> = _emptyVideo
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
                } catch (e: JSONException) {
                    onError(e)
                }
            }

            override fun onError(error: Any?) {
                AppLogger.longInfo(Constants.TAG, "ERROR! = " + error)
            }
        })
    }

}
