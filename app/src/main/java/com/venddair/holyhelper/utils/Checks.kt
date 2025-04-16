package com.venddair.holyhelper.utils

import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.activities.isInternetAvailable

// returns false if failed
fun mountWindowsIfNot(): Boolean {
    if (!ViewModel.isWindowsMounted.value) Commands.mountWindows(context, false)
    return !ViewModel.isWindowsMounted.value
}

fun checkInternet(): Boolean {
    if (!context.isInternetAvailable()) {
        Info.noInternet()
        return true
    }
    return false
}

// returns false if granted
fun checkRoot(): Boolean {
    if (Shell.isAppGrantedRoot() != true) {
        Info.noRootDetected(context)
        return true
    }
    return false
}

fun checkAppRestriction(): Boolean {
    if (Device.isRestricted) {
        Info.appRestricted(context)
        return true
    }
    return false
}