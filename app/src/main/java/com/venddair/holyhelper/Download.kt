package com.venddair.holyhelper

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File

object Download {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    fun download(url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Downloading $fileName")
        request.setDescription("Downloading file...")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        //request.setDestinationInExternalPublicDir(File(File(appContext.filesDir, "UEFI")), fileName)
        //request.setDestinationInExternalPublicDir(, fileName)

        val downloadManager = appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}