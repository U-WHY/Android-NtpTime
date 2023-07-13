package com.kzone.ntptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kzone.ntp.NtpTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.tv)
        val btnRefresh = findViewById<Button>(R.id.btn_refresh)
        val btnInit = findViewById<Button>(R.id.btn_reinit)
        btnRefresh.setOnClickListener {
            tv.text =
                if (NtpTime.hasSynced)
                    NtpTime.date.toString() + "\n" + NtpTime.timeMillis
                else
                    "ntp has not init"
        }
        btnInit.setOnClickListener {
            NtpTime.syncTime()
        }
    }
}