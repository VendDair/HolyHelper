package com.venddair.holyhelper.utils

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.ui.theme.AppColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference

object State {

    var launch: Boolean = false

    lateinit var Colors: AppColors

    object BaseColors {
        val color = "#FF404040"
        val textColor = "#FFFFFFFF"
        val guideGroupColor = "#FF382076"
    }

    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)

        Runtime.getRuntime().exit(0)
    }

    lateinit var viewModel: MainViewModel

    lateinit var context: WeakReference<Context>

    lateinit var coroutine: CoroutineScope

    var isWindowsMounted = false

    lateinit var deviceConfig: DeviceConfig

    var blurAmount: Dp by mutableStateOf(0.dp)

    var mountText: String by mutableStateOf("")
    var lastBackup: String? by mutableStateOf(null)

    var winPartition: String? = null
    var bootPartition: String? = null

    fun isDeviceLocked(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isDeviceLocked
    }

    fun coroutineInit() {
        coroutine = CoroutineScope(Dispatchers.Main)
    }

    private var failed: Boolean = false

    fun setFailed(state: Boolean) {
        if (state) coroutine.cancel()
        failed = state
    }

    fun getFailed(): Boolean { return failed }


    fun measureTime(name: String, function: () -> Unit) {
        val startTime = System.currentTimeMillis()

        function()

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", "$name: $elapsedTime")
    }

}