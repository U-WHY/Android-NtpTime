package com.kzone.ntp

class NtpException(ntpConfig: NtpConfig, message: String) : Exception(message + "\n" + ntpConfig) {

}