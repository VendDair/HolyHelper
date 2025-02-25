package com.venddair.holyhelper.utils

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.venddair.holyhelper.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

object State {

    //lateinit var pendingJobView: WeakReference<PendingJob>

    lateinit var viewModel: MainViewModel

    lateinit var coroutine: CoroutineScope

    var isWindowsMounted = false

    lateinit var deviceConfig: DeviceConfig

    var blurAmount: Dp by mutableStateOf(0.dp)

    var mountText: String by mutableStateOf("")
    var lastBackup: String? by mutableStateOf(null)

    var winPartition: String? = null
    var bootPartition: String? = null

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