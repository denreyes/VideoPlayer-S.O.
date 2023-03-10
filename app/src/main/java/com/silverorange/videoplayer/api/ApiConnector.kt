package com.silverorange.videoplayer.api

import com.silverorange.videoplayer.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConnector {
	private var builder: Retrofit.Builder
	private var retrofit: Retrofit
	private var logging: HttpLoggingInterceptor
	private var httpClient: okhttp3.OkHttpClient.Builder

	private constructor() {
		val baseUrl = Constants.BASE_URL
		val gson = GsonBuilder().setPrettyPrinting().create()
		builder = Retrofit.Builder().baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create(gson))
		retrofit = builder.build()
		logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
		httpClient = okhttp3.OkHttpClient.Builder()
	}

	private constructor(connectionTimeout: Long, writeTimeout: Long, readTimeout: Long) {
		val baseUrl = Constants.BASE_URL
		val gson = GsonBuilder().setPrettyPrinting().create()
		builder = Retrofit.Builder().baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create(gson))
		retrofit = builder.build()
		logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
		httpClient = okhttp3.OkHttpClient.Builder().connectTimeout(connectionTimeout, TimeUnit.MINUTES)
			.writeTimeout(writeTimeout, TimeUnit.MINUTES).readTimeout(readTimeout, TimeUnit.MINUTES)
	}

	fun <S> createService(serviceClass: Class<S>?): S {
		if (!httpClient.interceptors().contains(logging)) {
			httpClient.addInterceptor(logging)
		}
		builder.client(httpClient.build())
		retrofit = builder.build()
		return retrofit.create(serviceClass)
	}

	companion object {
		var instance: ApiConnector? = null
			private set

		fun init() {
			instance = ApiConnector()
		}

		fun init(connectionTimeout: Long, writeTimeout: Long, readTimeout: Long) {
			instance = ApiConnector(connectionTimeout, writeTimeout, readTimeout)
		}
	}
}
