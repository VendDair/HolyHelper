package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import java.io.File

object Download {

    @SuppressLint("InlinedApi")
    fun download(context: Context, url: String, fileName: String, callback: (finalFileName: String) -> Unit) {
        // Generate a unique file name if the file already exists
        val finalFileName = getUniqueFileName(fileName)

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Downloading $finalFileName")
            setDescription("Downloading file...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, finalFileName)
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Register the receiver to listen for download completion
        val receiver = object : BroadcastReceiver() {
            @SuppressLint("Range")
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                    context.unregisterReceiver(this) // Unregister the receiver
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor = downloadManager.query(query)

                    if (cursor != null && cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_FAILED) {
                            Info.downloadFailed(context, finalFileName)
                        }
                        callback(finalFileName) // Call the callback with the final file name
                    }
                    cursor?.close() // Close the cursor safely
                }
            }
        }

        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            Context.RECEIVER_EXPORTED)
    }

    private fun getUniqueFileName(fileName: String): String {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var finalFileName = fileName
        var counter = 1

        // Check if the file already exists and generate a new name if necessary
        while (File(downloadsDir, finalFileName).exists()) {
            val extensionIndex = fileName.lastIndexOf('.')
            if (extensionIndex != -1) {
                val nameWithoutExtension = fileName.substring(0, extensionIndex)
                val extension = fileName.substring(extensionIndex)
                finalFileName = "$nameWithoutExtension-$counter$extension"
            } else {
                finalFileName = "$fileName-$counter"
            }
            counter++
        }

        return finalFileName
    }
}
