package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import androidx.activity.ComponentActivity
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Download
import com.venddair.holyhelper.utils.State
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
            if (State.getFailed()) UniversalDialog.dialog.dismiss()
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
            ) { dialog ->
                dialog.setOnDismissListener {
                    State.setFailed(false)
                    isWinUnableToMountDialog = false
                }
            }
            isWinUnableToMountDialog = true
        }

        fun noWinPartition(context: Context) {
            if (isNoWinPartitionDialog) return
            if (State.getFailed()) UniversalDialog.dialog.dismiss()
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.partition),
                image = R.drawable.error,
                dismissible = false,
                buttons = listOf(
                    Pair(context.getString(R.string.guide)) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(Device.getGuideLink())
                            )
                        )
                    },
                    //Pair(context.getString(R.string.later)) {}
                )
            )
            UniversalDialog.dialog.setCancelable(false)
            //isNoWinPartitionDialog = true
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

        fun notifyAboutUpdate(context: ComponentActivity, version: String) {
            UniversalDialog.showDialog(context,
                title = context.getString(R.string.update1),
                text = context.getString(R.string.update_question, version),
                image = R.drawable.info,
                buttons = listOf(
                    Pair(context.getString(R.string.yes)) {
                        pleaseWait(context, R.string.done, R.drawable.info) {
                            Download.download(context, "https://github.com/VendDair/HolyHelper/releases/download/$version/HolyHelper.apk", "HolyHelper.apk") { _, name ->
                                Download.installAPK(context, name)
                            }
                        }
                    },
                    Pair(context.getString(R.string.later)) {
                        pleaseWait(context, R.string.done, R.drawable.info, {
                            Download.download(
                                context,
                                "https://github.com/VendDair/HolyHelper/releases/download/$version/HolyHelper.apk",
                                "HolyHelper.apk"
                            )
                        })
                    }
                )
            )
        }

        fun noRootDetected(context: Context) {
            UniversalDialog.showDialog(
                context,
                title = context.getString(R.string.nonroot),
                image = R.drawable.error,
            )
            UniversalDialog.dialog.setCancelable(false)

        }

        fun done(context: Context, text: String, imageId: Int) {
            UniversalDialog.clear()
            UniversalDialog.dialogText.get()?.text = text
            UniversalDialog.imageView.get()?.setImageResource(imageId)
            UniversalDialog.dialog.setCancelable(true)
            UniversalDialog.progressBar.get()?.visibility = View.GONE
            UniversalDialog.setButtons(context, true, listOf(
                Pair(context.getString(R.string.dismiss)) {}
            ))
            (context as ComponentActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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
            (context as ComponentActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            UniversalDialog.clear()
            UniversalDialog.dialogText.get()?.text = context.getString(R.string.please_wait)
            UniversalDialog.imageView.get()?.setImageResource(imageId)
            UniversalDialog.dialog.setCancelable(false)
            GlobalScope.launch {
                async()
                context.runOnUiThread {
                    onThread()
                    if (!State.getFailed()) done(context, doneText, imageId)
                }
            }
        }

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
        fun pleaseWaitProgress(
            context: Context,
            doneText: Int,
            imageId: Int,
            steps: Int,
            async: suspend () -> Unit = {},
            onThread: () -> Unit = {},
        ) {
            (context as ComponentActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
            UniversalDialog.clear()
            UniversalDialog.setupProgressBar(true, steps)
            UniversalDialog.dialogText.get()?.text = context.getString(R.string.please_wait)
            UniversalDialog.imageView.get()?.setImageResource(imageId)
            UniversalDialog.dialog.setCancelable(false)
            GlobalScope.launch {
async()
                context.runOnUiThread {
                    onThread()
                    if (!State.getFailed()) done(context, doneText, imageId)
                }
            }
        }
    }
}