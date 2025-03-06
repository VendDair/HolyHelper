package com.venddair.holyhelper.utils

import androidx.activity.ComponentActivity
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Commands.updateChecked

object Update {

    fun checkUpdate(context: ComponentActivity) {
        if (Preferences.DISABLEUPDATES.get()) return
        if (updateChecked) return
        Download.getRemoteFileContent(
            context,
            "https://github.com/VendDair/HolyHelper/releases/download/files/version"
        ) { content ->
            val version = content.replace("\n", "")
            if (isVersionNew(version)) {
                Info.notifyAboutUpdate(context, version)
                updateChecked = true
            }
            updateChecked = true
        }
    }

    private fun isVersionNew(version: String): Boolean {

        return versionToInt(version) > versionToInt(Strings.version)
    }

    private fun versionToInt(version: String): Int {
        return version.replace(".", "").toInt()
    }
}