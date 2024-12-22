package com.venddair.holyhelper

import android.content.Context

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
    }
}