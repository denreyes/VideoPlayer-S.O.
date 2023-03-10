package com.silverorange.videoplayer.api

import android.util.Log
import com.silverorange.videoplayer.utils.AppLogger
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

open class BaseCall {
	private var call: Call<ResponseBody?>? = null
	protected fun call(call: Call<ResponseBody?>?, listener: ApiRequestListener) {
		this.call = call
		this.call!!.enqueue(object : Callback<ResponseBody?> {
			override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
				try {
					val serverResponse: String
					val jsonObject: JSONObject
					if (response.isSuccessful) {
						if (response.body() != null) {
							serverResponse = response.body()!!.string()
							if (serverResponse.length > 0 && serverResponse[0].toString()
									.equals("[", ignoreCase = true)
							) {
								val jsonArray = JSONArray(serverResponse)
								AppLogger.printJson(jsonArray)
								listener.onSuccess(jsonArray)
							} else {
								jsonObject = JSONObject(serverResponse)
								AppLogger.printJson(jsonObject)
								listener.onSuccess(jsonObject)
							}
						} else {
							listener.onSuccess("")
						}
					} else {
						serverResponse = response.errorBody()!!.string()
						jsonObject = JSONObject(serverResponse)
						listener.onError(jsonObject)
					}
				} catch (var6: JSONException) {
					var6.printStackTrace()
					listener.onError(var6)
				} catch (var6: IOException) {
					var6.printStackTrace()
					listener.onError(var6)
				}
			}

			override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
				t.printStackTrace()
				listener.onError(t)
			}
		})
	}

	fun forceStop() {
		if (call != null && call!!.isExecuted) {
			call!!.cancel()
		}
	}

}
