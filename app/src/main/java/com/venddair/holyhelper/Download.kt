package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Download {

    @SuppressLint("SdCardPath")
    suspend fun download(context: Context, fileUrl: String, fileName: String): String? =
        withContext(Dispatchers.IO) {
            val destinationPath = File(context.getExternalFilesDir(null), fileName)
            var connection: HttpURLConnection? = null

            try {
                val url = URL(fileUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) {
                        Info.downloadFailed(context)
                    }
                    throw IOException("HTTP error code: ${connection.responseCode}")
                }

                connection.inputStream.use { inputStream ->
                    FileOutputStream(destinationPath).use { outputStream ->
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                    }
                }

                destinationPath.absolutePath
            } catch (e: Exception) {
                destinationPath.delete() // Delete partially downloaded file on error
                withContext(Dispatchers.Main) {
                    Info.downloadFailed(context)
                }
                null
            } finally {
                connection?.disconnect()
            }
        }

    @SuppressLint("InlinedApi")
            /*suspend fun download(context: Context, url: String, fileName: String): List<String> =
                suspendCancellableCoroutine { continuation ->
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

                    val receiver = object : BroadcastReceiver() {
                        @SuppressLint("Range")
                        override fun onReceive(context: Context, intent: Intent) {
                            if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                                context.unregisterReceiver(this)
                                val query = DownloadManager.Query().setFilterById(downloadId)
                                val cursor = downloadManager.query(query)

                                try {
                                    if (cursor?.moveToFirst() == true) {
                                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                                        //if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                        if (false) {
                                            continuation.resume(listOf(path, finalFileName))
                                        } else {
                                            (context as ComponentActivity).runOnUiThread {
                                                Info.downloadFailed(context, finalFileName)
                                            }
                                            continuation.cancel()
                                            //continuation.resumeWithException(Exception("Download failed"))
                                        }
                                    }
                                } catch (e: Exception) {
                                    continuation.resumeWithException(e)
                                } finally {
                                    cursor?.close()
                                }
                            }
                        }
                    }

                    // Handle coroutine cancellation
                    continuation.invokeOnCancellation {
                        context.unregisterReceiver(receiver)
                        downloadManager.remove(downloadId)
                    }

                    context.registerReceiver(
                        receiver,
                        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                        Context.RECEIVER_EXPORTED
                    )
                }*/

    fun download(
        context: Context,
        url: String,
        fileName: String,
        callback: (path: String, finalFileName: String) -> Unit,
    ) {
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
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_FAILED) {
                            Info.downloadFailed(context, finalFileName)
                        }
                        callback(path, finalFileName) // Call the callback with the final file name
                    }
                    cursor?.close() // Close the cursor safely
                }
            }
        }

        context.registerReceiver(
            receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            Context.RECEIVER_EXPORTED
        )
    }

    private fun getUniqueFileName(fileName: String): String {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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

    @SuppressLint("SdCardPath", "ObsoleteSdkInt", "QueryPermissionsNeeded")
    fun installAPK(context: Context, fileName: String) {
        // Get root external storage directory (usually /sdcard/)
        val externalDir = Environment.getExternalStorageDirectory()
        val apkFile = File(externalDir, fileName)

        // Check file existence
        if (!apkFile.exists()) {
            Toast.makeText(
                context,
                "APK file $fileName not found in root storage",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        try {
            // Create content URI using FileProvider
            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            // Create install intent
            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                // For Android 8.0+ devices
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                }
            }

            // Verify intent can be handled
            if (installIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(installIntent)
            } else {
                Toast.makeText(
                    context,
                    "No app found to handle APK installation",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Installation failed: ${e.message}", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(context, "Invalid APK file path", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permission denied to access APK file", Toast.LENGTH_SHORT)
                .show()
        }
    }


    fun getRemoteFileContent(
        context: ComponentActivity,
        fileUrl: String,
        callback: (fileContent: String) -> Unit,
    ) {
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

    suspend fun downloadFrameworks(context: Context) {
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

        coroutineScope {
            // Create a list of deferred results for each download
            val downloadJobs = urls.map { (url, fileName) ->
                async {
                    if (State.getFailed()) return@async null
                    val path = download(context, url, fileName) ?: return@async null
                    (context as ComponentActivity).runOnUiThread {
                        UniversalDialog.increaseProgress(1)

                        Files.copyFileToWin(context, path, "Toolbox/Frameworks/$fileName")
                    }
                    path // Return the path for further processing if needed
                }
            }
            // Await all downloads to complete
            downloadJobs.awaitAll()
        }

    }

    @SuppressLint("SdCardPath")
    suspend fun downloadDefenderRemover(context: Context) {
        val path = download(
            context,
            "https://github.com/n00b69/woasetup/releases/download/Installers/DefenderRemover.exe",
            "DefenderRemover.exe"
        )
            ?: return
        (context as ComponentActivity).runOnUiThread {
            UniversalDialog.progressBar.get()?.progress = 1
            Files.moveFileToWin(context, path, "Toolbox/DefenderRemover.exe")
        }
    }

}
