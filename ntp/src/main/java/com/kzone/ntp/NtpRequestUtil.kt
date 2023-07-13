package com.kzone.ntp

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.*

object NtpRequestUtil {
    private const val NTP_PORT = 123
    private const val NTP_PACKET_SIZE = 48
    private const val NTP_TIMESTAMP_OFFSET = 2208988800L
    private val executor: ExecutorService = ThreadPoolExecutor(
        0,
        Int.MAX_VALUE,
        30L,
        TimeUnit.SECONDS,
        SynchronousQueue()
    )

    /**
     * Get the NTP server time by making concurrent requests.
     * @return The earliest NTP timestamp returned among all NTP requests.
     */
    internal fun getCurrentNtpTimeMillis(ntpConfig: NtpConfig): Long{
        val completionService = ExecutorCompletionService<Long>(executor)
        for (host in ntpConfig.ntpServerHosts){
            completionService.submit { fetchNtpTime(host,ntpConfig.timeout) }
        }
        for (i in 0 until ntpConfig.ntpServerHosts.size){
            val ntpTimeMillis = completionService.take().get()
            if (ntpTimeMillis > 0) return ntpTimeMillis
        }
        return 0
    }

    /**
     * Gets the NTP time from the NTP server.
     * @param serverHost NTP server name.
     * @param timeout – the specified timeout in milliseconds.
     * @return The NTP time in milliseconds.
     */
    private fun fetchNtpTime(serverHost: String, timeout: Int): Long {
        var ntpTime: Long = 0
        val socket = DatagramSocket()
        LogUtil.d("ntp request begin->serverHost:$serverHost")
        kotlin.runCatching {
            val ntpPacket = ByteArray(NTP_PACKET_SIZE)
            ntpPacket[0] = 0x1B

            val address = InetAddress.getByName(serverHost)
            socket.soTimeout = timeout

            val requestPacket = DatagramPacket(ntpPacket, ntpPacket.size, address, NTP_PORT)
            socket.send(requestPacket)

            val responsePacket = DatagramPacket(ntpPacket, ntpPacket.size)
            socket.receive(responsePacket)

            val seconds = readUint32(ntpPacket, 40)
            val fraction = readUint32(ntpPacket, 44)
            ntpTime = (seconds - NTP_TIMESTAMP_OFFSET) * 1000 + (fraction * 1000L / 0x100000000L)
            LogUtil.d("ntp request success->serverHost:$serverHost ntpTimeMillis:$ntpTime")
        }.onFailure {
            LogUtil.d("ntp request fail->serverHost:$serverHost $it")
            if (it is InterruptedException) LogUtil.e("???????")
            if (it is InterruptedException) throw it
        }
        socket.close()
        return ntpTime
    }

    private fun readUint32(bytes: ByteArray, offset: Int): Long {
        val buffer = ByteBuffer.wrap(bytes, offset, 4)
        buffer.order(ByteOrder.BIG_ENDIAN)
        return buffer.int.toLong() and 0xFFFFFFFFL
    }
}
