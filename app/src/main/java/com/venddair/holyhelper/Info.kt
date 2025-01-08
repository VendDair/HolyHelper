package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri

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
                text = "Check your internet connection!",
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
    }
}