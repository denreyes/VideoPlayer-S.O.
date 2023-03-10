package com.silverorange.videoplayer.utils

import android.util.Log
import com.silverorange.videoplayer.BuildConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object AppLogger {
	fun info(tag: String?, string: String) {
		if (BuildConfig.DEBUG) {
			if (!string.isEmpty()) {
				if (string.length > 4000) {
					Log.i(tag, string.substring(0, 4000))
					info(tag, string.substring(4000))
				} else {
					Log.i(tag, string)
				}
			} else {
				Log.e(Constants.TAG, "Nothing to log")
			}
		}
	}

	fun info(string: String) {
		if (BuildConfig.DEBUG) {
			if (!string.isEmpty()) {
				Log.i(Constants.TAG, string)
			} else {
				Log.e(Constants.TAG, "Nothing to log")
			}
		}
	}

	fun error(string: String) {
		if (BuildConfig.DEBUG) {
			if (!string.isEmpty()) {
				val maxLogSize = 4000
				for (i in 0..string.length / maxLogSize) {
					val start = i * maxLogSize
					var end = (i + 1) * maxLogSize
					end = if (end > string.length) string.length else end
					Log.e(Constants.TAG, string.substring(start, end))
				}
			} else {
				Log.e(Constants.TAG, "Nothing to log")
			}
		}
	}

	fun warn(string: String) {
		if (BuildConfig.DEBUG) {
			if (!string.isEmpty()) {
				Log.w(Constants.TAG, string)
			} else {
				Log.e(Constants.TAG, "Nothing to log")
			}
		}
	}

	fun longInfo(tag: String?, msg: String) {
		try {
			if (BuildConfig.DEBUG) {
				if (!msg.isEmpty()) {
					if (msg.length > 4000) {
						Log.i(tag, msg.substring(0, 4000))
						longInfo(tag, msg.substring(4000))
					} else {
						Log.i(tag, msg)
					}
				} else {
					error("LongInfo msg is empty")
				}
			}
		} catch (ignored: Exception) {
		}
	}

	fun longInfo(msg: String) {
		try {
			if (BuildConfig.DEBUG) {
				if (!msg.isEmpty()) {
					if (msg.length > 4000) {
						info(msg.substring(0, 4000))
						longInfo(msg.substring(4000))
					} else {
						try {
							info(msg)
						} catch (ignored: OutOfMemoryError) {
						}
					}
				} else {
					error("LongInfo msg is empty")
				}
			}
		} catch (ignored: Exception) {
		}
	}

	fun longError(msg: String) {
		if (!msg.isEmpty()) {
			if (BuildConfig.DEBUG) {
				if (msg.length > 4000) {
					error(msg.substring(0, 4000))
					longError(msg.substring(4000))
				} else {
					error(msg)
				}
			}
		} else {
			error("LongError msg is empty")
		}
	}

	fun longWarn(msg: String) {
		if (!msg.isEmpty()) {
			if (BuildConfig.DEBUG) {
				if (msg.length > 4000) {
					warn(msg.substring(0, 4000))
					longWarn(msg.substring(4000))
				} else {
					warn(msg)
				}
			}
		} else {
			error("LongWarn msg is empty")
		}
	}

	fun prettyPrint(`object`: JSONObject) {
		try {
			val text = `object`.toString(2)
			val temp = text.split("\n".toRegex()).toTypedArray()
			for (s in temp) {
				info(s)
			}
		} catch (e: JSONException) {
			e.printStackTrace()
		}
	}

	fun printJSONObject(tag: String?, `object`: JSONObject) {
		if (BuildConfig.DEBUG) {
			try {
				val text = `object`.toString(2)
				val temp = text.split("\n".toRegex()).toTypedArray()
				for (s in temp) {
					Log.i(tag, s)
				}
			} catch (e: JSONException) {
				e.printStackTrace()
				printJson(`object`)
			}
		}
	}

	fun printJson(`object`: Any?) {
		if (BuildConfig.DEBUG) {
			if (`object` is JSONObject) {
				try {
					longInfo(`object`.toString(2))
				} catch (e: JSONException) {
					e.printStackTrace()
				}
			} else if (`object` is JSONArray) {
				try {
					longInfo(`object`.toString(2))
				} catch (e: JSONException) {
					e.printStackTrace()
				}
			} else if (`object` != null) {
				longInfo(`object`.toString())
			} else {
				error("Nothing to Log")
			}
		}
	}

	fun printRetrofitError(error: Any?) {
		if (BuildConfig.DEBUG) {
			if (error is JSONObject) {
				try {
					longError(error.toString(2))
				} catch (e: JSONException) {
					e.printStackTrace()
				}
			} else if (error is Exception) {
				if (BuildConfig.DEBUG) {
					error("Exception Stacktrace")
					error.printStackTrace()
				}
			} else if (error is Throwable) {
				if (BuildConfig.DEBUG) {
					error("Throwable Stacktrace")
					error.printStackTrace()
				}
			} else if (error != null) {
				longError(error.toString())
			} else {
				error("Nothing to log")
			}
		}
	}

	fun printMap(map: Map<String, String>) {
		for (key in map.keys) {
			longInfo(key + ": " + map[key])
		}
	}
}
