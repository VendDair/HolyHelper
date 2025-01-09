package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object Permissions {
    fun requestInstallPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }

}