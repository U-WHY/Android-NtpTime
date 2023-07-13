package com.kzone.ntp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.util.Log

class KNtpProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.packageManager?.getApplicationInfo(context!!.packageName, PackageManager.GET_META_DATA)?.let {
            kotlin.runCatching {
                it.metaData.getString("KNTP_LIST")?.let {
                    NtpTime.syncTime(*it.split(",").map { it.trim() }.toTypedArray())
                    return false
                }
            }
            kotlin.runCatching {
                it.metaData.getInt("KNTP_LIST").let {
                    NtpTime.syncTime(*context!!.resources.getStringArray(it))
                    return false
                }
            }
        }
        NtpTime.syncTime()
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}