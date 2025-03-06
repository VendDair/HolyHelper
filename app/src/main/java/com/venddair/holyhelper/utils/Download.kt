package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.UniversalDialog
import com.venddair.holyhelper.utils.Files.createWinFolder
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

    fun download(
        context: Context,
        url: String,
        fileName: String,
        callback: (path: String, fileName: String) -> Unit,
    ) {
        // Generate a unique file name if the file already exists
        val path = "${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}/$fileName"
        Files.remove(Strings.holyHelperAPK)

        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("Downloading $fileName")
            setDescription("Downloading file...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
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
                            Info.downloadFailed(context, fileName)
                        }
                        callback(path, fileName) // Call the callback with the final file name
                    }
                    cursor?.close() // Close the cursor safely
                }
            }
        }

        ContextCompat.registerReceiver(
            context, receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

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
    /*fun installAPK(context: ComponentActivity, fileName: String) {
        // Locate the APK file in the Downloads directory.
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val apkFile = File(downloadsDir, fileName)
        if (!apkFile.exists()) {
            Toast.makeText(context, "APK file does not exist", Toast.LENGTH_SHORT).show()
            return
        }
        // Get the APK's content URI via FileProvider.
        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)

        // If permission is already granted (or not needed), install immediately.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || context.packageManager.canRequestPackageInstalls()) {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            return
        }*/

    fun installAPK(context: ComponentActivity, fileName: String) {
        //Permissions.requestInstallPermission(context)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val apkFile = File(downloadsDir, fileName)

        if (!apkFile.exists()) {
            Toast.makeText(context, "APK file does not exist", Toast.LENGTH_SHORT).show()
            return
        }

        val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!context.packageManager.canRequestPackageInstalls()) {
                // Open settings to allow unknown apps installation for this app
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)

                // Start a background thread to wait for permission and retry installation
                Thread {
                    while (!context.packageManager.canRequestPackageInstalls()) {
                        Thread.sleep(200) // Wait until permission is granted
                    }
                    installAPK(context, fileName) // Retry APK installation
                }.start()

                return
            }
        }

        // Install APK for API 24+
        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(installIntent)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
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

    suspend fun downloadFrameworks(context: Context) = coroutineScope {
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

        val downloadJobs = urls.map { (url, fileName) ->
            async(Dispatchers.IO) {
                val path = download(context, url, fileName) ?: return@async null

                withContext(Dispatchers.Main) {
                    UniversalDialog.increaseProgress(1)
                    Files.moveFileToWin(context, path, "${Strings.win.folders.frameworks}/$fileName")
                }
                path
            }
        }

        downloadJobs.awaitAll()

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
            Files.moveFileToWin(context, path, Strings.win.defenderRemover)
        }
    }

    suspend fun downloadAtlasOS(context: ComponentActivity) = coroutineScope {
        context.runOnUiThread {
            createWinFolder(context, Strings.win.folders.toolbox)
        }
        val download1 = async(Dispatchers.IO) {
            val path = download(
                context,
                "https://github.com/n00b69/modified-playbooks/releases/download/AtlasOS/AtlasPlaybook.apbx",
                "AtlasPlaybook.apbx"
            )
                ?: return@async
            context.runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(context, path, Strings.win.atlasPlaybook)
            }
        }

        val download2 = async(Dispatchers.IO) {
            val path = download(
                context,
                "https://download.ameliorated.io/AME%20Wizard%20Beta.zip",
                "AMEWizardBeta.zip"
            )
                ?: return@async
            context.runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(context, path, Strings.win.ameWizard)
            }
        }

        awaitAll(download1, download2)
    }

    suspend fun downloadReviOS(context: ComponentActivity) = coroutineScope {
        context.runOnUiThread {
            createWinFolder(context, Strings.win.folders.toolbox)
        }

        val download1 = async(Dispatchers.IO) {
            val path = download(
                context,
                "https://github.com/n00b69/modified-playbooks/releases/download/ReviOS/ReviPlaybook.apbx",
                "ReviPlaybook.apbx"
            )
                ?: return@async
            context.runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(context, path, Strings.win.reviPlaybook)
            }
        }

        val download2 = async(Dispatchers.IO) {
            val path = download(
                context,
                "https://download.ameliorated.io/AME%20Wizard%20Beta.zip",
                "AMEWizardBeta.zip"
            )
                ?: return@async
            context.runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(context, path, Strings.win.ameWizard)
            }
        }

        awaitAll(download1, download2)
    }

}
