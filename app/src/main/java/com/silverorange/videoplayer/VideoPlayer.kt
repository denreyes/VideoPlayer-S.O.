package com.silverorange.videoplayer

import android.app.Application
import com.silverorange.videoplayer.api.ApiConnector

class VideoPlayer : Application() {

	override fun onCreate() {
		super.onCreate()
		ApiConnector.init()
	}
}
