package com.venddair.holyhelper.utils

import android.app.KeyguardManager
import android.content.Context

object LockStateDetector {

    fun isDeviceLocked(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isDeviceLocked
    }
}