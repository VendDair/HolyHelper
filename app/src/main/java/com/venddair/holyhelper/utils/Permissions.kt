package com.venddair.holyhelper.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity

object Permissions {

    // Function to request install permission for unknown sources
    fun requestInstallPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Check if permission is already granted
            if (!context.packageManager.canRequestPackageInstalls()) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
            }
        } else {
            // For versions below Oreo, check if unknown sources are enabled
            if (Settings.Secure.getInt(
                    context.contentResolver,
                    Settings.Secure.INSTALL_NON_MARKET_APPS, 0
                ) == 0
            ) {
                val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                context.startActivity(intent)
            }
        }
    }
}