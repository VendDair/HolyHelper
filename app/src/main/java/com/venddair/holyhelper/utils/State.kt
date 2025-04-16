package com.venddair.holyhelper.utils

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.ui.theme.AppColors
import com.venddair.holyhelper.ui.themes.Theme
import java.lang.ref.WeakReference

var failed = false

lateinit var deviceConfig: DeviceConfig

lateinit var appColors: AppColors

lateinit var AppTheme: Theme

lateinit var ViewModel: MainViewModel

val isPortrait: Boolean
    @Composable
    get() = LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

fun isDeviceLocked(context: Context): Boolean {
    val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
    return keyguardManager.isDeviceLocked
}

private lateinit var navControllerRef: WeakReference<NavController>
var NavController: NavController
    get() {
        return navControllerRef.get()!!
    }
    set(value) {
        navControllerRef = WeakReference(value)
    }

private lateinit var contextRef: WeakReference<ComponentActivity>
var context: ComponentActivity
    get() {
        return contextRef.get()!!
    }
    set(value) {
        contextRef = WeakReference(value)
    }

fun restartApp() {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)

    Runtime.getRuntime().exit(0)
}