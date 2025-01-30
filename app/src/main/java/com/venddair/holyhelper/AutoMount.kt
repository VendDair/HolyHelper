package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoMount : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (!Preferences.getBoolean(Preferences.Preference.SETTINGS, Preferences.Key.AUTOMOUNT, false)) return
        Commands.mountWindows(context)

    }
}