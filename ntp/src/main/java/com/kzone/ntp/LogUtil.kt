package com.kzone.ntp

import android.util.Log

object LogUtil {
    private val TAG = "Android.Ntp"

    fun v(msg: String) {
        Log.v(TAG, msg)
    }

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }

    fun e(msg: String) {
        Log.e(TAG, msg)
    }
}