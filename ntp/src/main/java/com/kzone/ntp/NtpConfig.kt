package com.kzone.ntp

/**
 * @param ntpServerHosts ntp server hosts array.
 * @param updateInterval the interval of recalibrate ntp time.
 * @param retryInterval the interval between requesting again when a ntp request fails.
 * @param timeout – the specified timeout in milliseconds.
 */
data class NtpConfig(
    var ntpServerHosts: Array<out String> = arrayOf("time.android.com","time.google.com","pool.ntp.org","time.windows.com"),
    var updateInterval: Long = 60 * 60 * 1000,
    var retryInterval: Long = 10 * 1000,
    var timeout:Int = 5 * 1000,
)