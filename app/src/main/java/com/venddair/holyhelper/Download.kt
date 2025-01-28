package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Download {

    @SuppressLint("InlinedApi")
    fun download(context: Context, url: String, fileName: String, callback: (path: String, finalFileName: String) -> Unit) {
        // Generate a unique file name if the file already exists
        val finalFileName = getUniqueFileName(fileName)
        val path = Paths.downloads + "/$finalFileName"

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
                        callback(path, finalFileName) // Call the callback with the final file name
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

    fun installAPK(context: Context, fileName: String) {
        // Get the full path to the APK file in the Downloads directory
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val apkFile = File(downloadsDir, fileName)

        // Check if the APK file exists
        if (!apkFile.exists()) {
            Toast.makeText(context, "APK file does not exist", Toast.LENGTH_SHORT).show()
            return
        }

        // Use FileProvider to get the content URI
        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)

        // Create an intent to install the APK
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Start the installation activity
        context.startActivity(intent)
    }


    fun getRemoteFileContent(context: ComponentActivity, fileUrl: String, callback: (fileContent: String) -> Unit) {
        Thread {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                // Check the response code
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the contents of the file
                    BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                        val content = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            content.appendLine(line)
                            }
                        context.runOnUiThread {
                            callback(content.toString())
                        }
                    }
                } else {
                    println("Failed to fetch the file: HTTP error code ${connection.responseCode}")
                    Info.unableToDownload(context)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Info.unableToDownload(context)
            }
        }.start()

    }

    fun downloadFrameworks(context: Context) {
        val urls = mapOf(
            "https://github.com/n00b69/woasetup/releases/download/Installers/PhysX-9.13.0604-SystemSoftware-Legacy.msi" to "PhysX-9.13.0604-SystemSoftware-Legacy.msi",
            "https://github.com/n00b69/woasetup/releases/download/Installers/PhysX_9.23.1019_SystemSoftware.exe" to "PhysX_9.23.1019_SystemSoftware.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/xnafx40_redist.msi" to "xnafx40_redist.msi",
            "https://github.com/n00b69/woasetup/releases/download/Installers/opengl.appx" to "opengl.1.2409.2.0.appx",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2005vcredist_x64.EXE" to "2005vcredist_x64.EXE",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2005vcredist_x86.EXE" to "2005vcredist_x86.EXE",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2008vcredist_x64.exe" to "2008vcredist_x64.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2008vcredist_x86.exe" to "2008vcredist_x86.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2010vcredist_x64.exe" to "2010vcredist_x64.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2010vcredist_x86.exe" to "2010vcredist_x86.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2012vcredist_x64.exe" to "2012vcredist_x64.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2012vcredist_x86.exe" to "2012vcredist_x86.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2013vcredist_x64.exe" to "2013vcredist_x64.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2013vcredist_x86.exe" to "2013vcredist_x86.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2015VC_redist.x64.exe" to "2015VC_redist.x64.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2015VC_redist.x86.exe" to "2015VC_redist.x86.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/2022VC_redist.arm64.exe" to "2022VC_redist.arm64.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/dxwebsetup.exe" to "dxwebsetup.exe",
            "https://github.com/n00b69/woasetup/releases/download/Installers/oalinst.exe" to "oalinst.exe"
        )
        if (Commands.mountWindows(context, false)) {
            for ((url, fileName) in urls) {
                download(context, url, fileName) { path, name ->
                    Files.copyFileToWin(context, path, "Toolbox/Frameworks/$name")
                }
            }
        }
    }

    fun downloadDefenderRemover(context: Context) {
        download(
            context,
            "https://github.com/n00b69/woasetup/releases/download/Installers/DefenderRemover.exe",
            "DefenderRemover.exe"
        ) { path, name ->
            Files.copyFileToWin(context, path, "Toolbox/$name")
        }
    }

}
