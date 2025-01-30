package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        fun downloadFailed(context: Context, fileName: String = "") {
            UniversalDialog.showDialog(
                context,
                title = context.getString(R.string.download_file_failed_title, fileName),
                text = context.getString(R.string.download_failed_subtitle),
                image = R.drawable.error,
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
                image = R.drawable.error,
                buttons = listOf(
                    Pair(context.getString(R.string.chat)) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(Device.getGroupLink())
                            )
                        )
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
                image = R.drawable.error,
                buttons = listOf(
                    Pair(context.getString(R.string.guide)) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(Device.getGuideLink())
                            )
                        )
                    },
                    Pair(context.getString(R.string.later)) {}
                )
            ) {
                isNoWinPartitionDialog = false
            }
            isNoWinPartitionDialog = true
        }

        fun unableToDownload(context: Context) {
            UniversalDialog.showDialog(
                context,
                title = context.getString(R.string.download_failed_title),
                text = context.getString(R.string.download_failed_subtitle),
                image = R.drawable.error,
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
                    Pair(context.getString(R.string.yes)) {
                        pleaseWait(context, R.string.done, R.drawable.info, {
                            Download.download(
                                context,
                                "https://github.com/VendDair/HolyHelper/releases/download/$version/HolyHelper.apk",
                                "HolyHelper.apk"
                            )
                        }, {
                            Download.installAPK(context, "HolyHelper.apk")
                        })
                    },
                    Pair(context.getString(R.string.later)) {}
                )
            )
        }

        /*        fun uefiNotFound(context: Context, reboot: Boolean = false) {
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
                }*/

        fun noRootDetected(context: Context) {
            UniversalDialog.showDialog(
                context,
                title = context.getString(R.string.nonroot),
                image = R.drawable.error,
            )
            UniversalDialog.dialog.setCancelable(false)

        }

        fun done(context: Context, text: String, imageId: Int) {
            UniversalDialog.showDialog(context,
                title = text,
                image = imageId,
                buttons = listOf(
                    Pair(context.getString(R.string.dismiss)) {}
                )
            )
        }

        fun done(context: Context, textId: Int, imageId: Int) {
            done(context, context.getString(textId), imageId)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun pleaseWait(
            context: Context,
            doneText: String,
            imageId: Int,
            async: suspend () -> Unit = {},
            onThread: () -> Unit = {},
        ) {
            UniversalDialog.showDialog(
                context,
                title = context.getString(R.string.please_wait),
                image = imageId,
            ) { dialog ->
                dialog.setCancelable(false)
                GlobalScope.launch {
                    async()
                    (context as ComponentActivity).runOnUiThread {
                        onThread()
                        dialog.cancel()
                        if (!State.failed) done(context, doneText, imageId)
                        else State.failed = false
                    }
                }
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun pleaseWait(
            context: Context,
            doneText: Int,
            imageId: Int,
            async: suspend () -> Unit = {},
            onThread: () -> Unit = {},
        ) {
            pleaseWait(context, context.getString(doneText), imageId, async, onThread)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun pleaseWaitDownload(
            context: Context,
            doneText: Int,
            imageId: Int,
            downloadMax: Int,
            async: suspend () -> Unit = {},
            onThread: () -> Unit = {},
        ) {
            UniversalDialog.showDialog(
                context,
                title = context.getString(R.string.please_wait),
                image = imageId,
                download = true,
                downloadMax = downloadMax
            ) { dialog ->
                dialog.setCancelable(false)
                GlobalScope.launch {
                    async()
                    (context as ComponentActivity).runOnUiThread {
                        onThread()
                        dialog.cancel()
                        done(context, doneText, imageId)
                    }
                }
            }
        }
    }
}