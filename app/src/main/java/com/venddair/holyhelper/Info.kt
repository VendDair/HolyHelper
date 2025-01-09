package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment

class Info {
    companion object {
        fun winNotMounted(context: Context, after: (result: Boolean) -> Unit = {}) {
            UniversalDialog.showDialog(context,
                title = "Windows is not mounted!",
                text = "Do you want to mount it?",
                image = R.drawable.info,
                buttons = listOf(
                    Pair("YES") {
                        val res = Commands.mountWindows(context)
                        after(res)
                    },
                    Pair("NO") {},
                )
            )
        }

        fun downloadFailed(context: Context, fileName: String) {
            UniversalDialog.showDialog(context,
                title = "Failed to download $fileName!",
                text = "Check your internet connection!\nOr file may not even exist",
                image = R.drawable.info,
                buttons = listOf(
                    Pair("OK") {},
                )
            )
        }

        fun winUnableToMount(context: Context) {
            UniversalDialog.showDialog(context,
                title = "Windows wasn't able to mount!",
                text = "Try to mount to /mnt in settings\nFurther questions to the developer",
                image = R.drawable.info,
                buttons = listOf(
                    Pair("OK") {}
                )
            )
        }

        fun noWinPartition(context: Context) {
            UniversalDialog.showDialog(context,
                title = "No Windows partition was found!",
                text = "You may not have windows installed\nCheck the guide for your device",
                image = R.drawable.info,
                buttons = listOf(
                    Pair("CHECK") {context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.getGuideLink())))},
                    Pair("Later") {}
                )
            )
        }

        fun unableToDownload(context: Context) {
            UniversalDialog.showDialog(context,
                title = "Couldn't download the file",
                text = "Check your internet connection\nFor any additional questions ask the developer",
                image = R.drawable.info,
                buttons = listOf(
                    Pair("OK") {},
                )
            )
        }

        fun notifyAboutUpdate(context: Context, version: String) {
            UniversalDialog.showDialog(context,
                title = "New update was detected!",
                text = "Would you like to download the version $version?",
                image = R.drawable.info,
                buttons = listOf(
                    Pair("YES") { Download.download(context, "https://github.com/VendDair/HolyHelper/releases/download/$version/HolyHelper.apk", "HolyHelper.apk") { fileName ->
                        Download.installAPK(context, fileName)
                    } },
                    Pair("LATER") {}
                )
            )
        }
    }
}