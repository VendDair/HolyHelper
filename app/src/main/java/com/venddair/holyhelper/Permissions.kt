package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object Permissions {
    private const val STORAGE_PERMISSION_CODE = 1001

    // Function to request install permission for unknown sources
    fun requestInstallPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }
}