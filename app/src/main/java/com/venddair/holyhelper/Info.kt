package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.venddair.holyhelper.Commands.bootInWindows

class Info {
    companion object {

        /*var isWinNotMountedDialog = false*/
        var isWinUnableToMountDialog = false
        var isNoWinPartitionDialog = false

        /*fun winNotMounted(context: Context, after: (result: Boolean) -> Unit = {}) {
            if (isWinNotMountedDialog) return
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
            ) {
                isWinNotMountedDialog = false
            }
            isWinNotMountedDialog = true
        }*/

        fun downloadFailed(context: Context, fileName: String) {
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.download_file_failed_title, fileName),
                text = context.getString(R.string.download_failed_subtitle),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.dismiss)) {},
                )
            )
        }

        fun winUnableToMount(context: Context) {
            if (isWinUnableToMountDialog) return
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.mountfail),
                text = context.getString(R.string.internalstorage),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.chat)) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Device.getGroupLink())))
                    },
                    Pair(context.getString(R.string.dismiss)) {}
                )
            ) {
                isWinUnableToMountDialog = false
            }
            isWinUnableToMountDialog = true
        }

        fun noWinPartition(context: Context) {
            if (isNoWinPartitionDialog) return
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.partition),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.guide)) {context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Device.getGuideLink())))},
                    Pair(context.getString(R.string.later)) {}
                )
            ) {
                isNoWinPartitionDialog = false
            }
            isNoWinPartitionDialog = true
        }

        fun unableToDownload(context: Context) {
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.download_failed_title),
                text = context.getString(R.string.download_failed_subtitle),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.dismiss)) {},
                )
            )
        }

        fun notifyAboutUpdate(context: Context, version: String) {
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.update1),
                text = context.getString(R.string.update_question, version),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.yes)) { Download.download(context, "https://github.com/VendDair/HolyHelper/releases/download/$version/HolyHelper.apk", "HolyHelper.apk") { _, fileName ->
                        Download.installAPK(context, fileName)
                    } },
                    Pair(context.getString(R.string.later)) {}
                )
            )
        }

        fun bootBackedUpSuccessfully(context: Context) {
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.backuped),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.dismiss)) {}
                )
            )
        }

        fun uefiNotFound(context: Context, reboot: Boolean = false) {
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.uefi_not_found),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.select)) {
                        Files.selectUefiImage {
                            bootInWindows(reboot)
                        }
                    }
                )
            )
        }
    }
}