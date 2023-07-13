package com.kzone.ntp

import android.os.SystemClock
import java.util.Date

object NtpTime {
    private var elapsedTimeMillis: Long = 0
    private var ntpTimeMillis: Long = 0
    private var ntpConfig: NtpConfig = NtpConfig()

    private val mThread: Thread by lazy {
        Thread {
            while (true) {
                kotlin.runCatching {
                    LogUtil.d("trying to get current ntp time")
                    val tempNtpTimeMillis = NtpRequestUtil.getCurrentNtpTimeMillis(ntpConfig)
                    val success = tempNtpTimeMillis > 0
                    if (success) {
                        ntpTimeMillis = tempNtpTimeMillis
                        elapsedTimeMillis = SystemClock.elapsedRealtime()
                        LogUtil.d("new ntp time:$ntpTimeMillis")
                    } else {
                        LogUtil.e("All NTP servers are unreachable. Retry later.")
                        LogUtil.e("If multiple retries still fail, please check the network status or configure a new list of NTP servers.")
                    }
                    if (Thread.interrupted()) {
                        LogUtil.e("ntp thread is interrupted, begin reinit")
                        return@runCatching
                    }
                    Thread.sleep(if (success) ntpConfig.updateInterval else ntpConfig.retryInterval)
                }.onFailure {
                    if (it is InterruptedException) {
                        LogUtil.e("ntp thread recive InterruptedException, begin sync")
                    } else {
                        LogUtil.e("get ntp time fail because ${it.message}, try again.")
                    }
                }
            }
        }
    }

    val hasSynced: Boolean
        get() = ntpTimeMillis > 0

    /**
     * Get the NTP timestamp, throw an exception if synchronization is not completed
     */
    val timeMillis: Long
        get() {
            if (!hasSynced)
                throw NtpException(ntpConfig, "NTP time has not been obtained now.")
            val currentElapsedTime = SystemClock.elapsedRealtime()
            if (currentElapsedTime < elapsedTimeMillis)
                throw NtpException(ntpConfig, "An error occurred in the internal timer based on Android system boot time.")
            return ntpTimeMillis + SystemClock.elapsedRealtime() - elapsedTimeMillis
        }

    /**
     * Get the NTP timestamp, return the system timestamp if synchronization is not completed
     */
    val safeTimeMillis: Long
        get() {
            if (!hasSynced) return System.currentTimeMillis()
            val currentElapsedTime = SystemClock.elapsedRealtime()
            if (currentElapsedTime < elapsedTimeMillis) return System.currentTimeMillis()
            return ntpTimeMillis + currentElapsedTime - elapsedTimeMillis
        }

    val date: Date
        get() = Date(timeMillis)

    val safeDate: Date
        get() = Date(safeTimeMillis)

    /**
     * Synchronize NTP time immediately
     */
    fun syncTime() {
        if (mThread.isAlive) {
            mThread.interrupt()
        } else {
            mThread.start()
        }
    }

    /**
     * Synchronize NTP time immediately
     * @param ntpServers Send synchronization requests to these NTP servers.
     */
    fun syncTime(ntpServers: List<String>) {
        ntpConfig.ntpServerHosts = ntpServers.toTypedArray()
        syncTime()
    }

    /**
     * Synchronize NTP time immediately
     * @param ntpServers Send synchronization requests to these NTP servers.
     */
    fun syncTime(vararg ntpServers: String) {
        LogUtil.e(ntpServers[0]+","+ntpServers[1])
        ntpConfig.ntpServerHosts = ntpServers
        syncTime()
    }

    /**
     * Synchronize NTP time immediately
     * @param ntpConfig see [NtpConfig]
     */
    fun syncTime(ntpConfig: NtpConfig) {
        this.ntpConfig = ntpConfig
        syncTime()
    }
}