package com.venddair.holyhelper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference

object State {

    lateinit var pendingJobView: WeakReference<PendingJob>

    lateinit var coroutine: CoroutineScope

    fun coroutineInit() {
        coroutine = CoroutineScope(Dispatchers.Main)
    }

    private var failed: Boolean = false

    fun setFailed(state: Boolean) {
        if (state) coroutine.cancel()
        failed = state
    }

    fun getFailed(): Boolean { return failed }

}