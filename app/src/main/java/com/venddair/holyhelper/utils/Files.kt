package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.ShellUtils
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Commands.backupBootImage
import com.venddair.holyhelper.utils.Commands.notifyIfNoWinPartition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object Files {
    @SuppressLint("SdCardPath")

    private lateinit var appContext: Context


    enum class Extension(val key: String) {
        IMG(".img"),
    }


    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SdCardPath")
    fun init(context: Context) {
        appContext = context

        Strings.assets.data = context.filesDir.toString()

        CoroutineScope(Dispatchers.IO).launch {
            createFolder(Strings.assets.data)
            createFolder(Strings.folders.uefi)

            copyAsset("mount.ntfs", "+x", true)
            copyAsset("sta.exe", ignoreIfPresent = true)
            copyAsset("sdd.exe", ignoreIfPresent = true)
            copyAsset("boot_img_auto-flasher_V1.0.exe", ignoreIfPresent = true)
            copyAsset("sdd.conf", ignoreIfPresent = true)
            copyAsset("libntfs-3g.so", ignoreIfPresent = true)
            copyAsset("libfuse-lite.so", ignoreIfPresent = true)
            copyAsset("Android.lnk", ignoreIfPresent = true)
            copyAsset("ARMRepo.url", ignoreIfPresent = true)
            copyAsset("ARMSoftware.url", ignoreIfPresent = true)
            copyAsset("TestedSoftware.url", ignoreIfPresent = true)
            copyAsset("WorksOnWoa.url", ignoreIfPresent = true)
            copyAsset("dbkp8150.cfg", ignoreIfPresent = true)
            copyAsset("dbkp.hotdog.bin", ignoreIfPresent = true)
            copyAsset("dbkp.cepheus.bin", ignoreIfPresent = true)
            copyAsset("dbkp.nabu.bin", ignoreIfPresent = true)
            copyAsset("usbhostmode.exe", ignoreIfPresent = true)
            copyAsset("display.exe", ignoreIfPresent = true)
            copyAsset("RotationShortcut.lnk", ignoreIfPresent = true)
            copyAsset("install.bat", ignoreIfPresent = true)
            copyAsset("RemoveEdge.bat", ignoreIfPresent = true)
            copyAsset("RotationShortcutReverseLandscape.lnk", ignoreIfPresent = true)
        }
    }

    fun checkExtension(path: String, extension: Extension): Boolean {
        return path.endsWith(extension.key)
    }

    fun createFolder(path: String) {
        ShellUtils.fastCmd("su -c mkdir -p $path")
    }

    @SuppressLint("SdCardPath")
    suspend fun setupDbkpFiles(context: ComponentActivity) = coroutineScope {
        withContext(Dispatchers.Main) {
            backupBootImage(context)
        }
        createFolder("/sdcard/dbkp")
        remove("/sdcard/original-boot.img")
        copy(Strings.bootImage, "/sdcard/dbkp/boot.img")
        moveFile(Strings.bootImage, "/sdcard/original-boot.img")
        copy(Strings.assets.dbkp8150Cfg, "/sdcard/dbkp")
        copy(Strings.assets.dbkpHotdogBin, "/sdcard/dbkp")
        copy(Strings.assets.dbkpCepheusBin, "/sdcard/dbkp")
        copy(Strings.assets.dbkpNabuBin, "/sdcard/dbkp")
    }

    fun copy(path: String, newPath: String) {
        ShellUtils.fastCmd("su -c cp $path $newPath")
    }

    fun setPerms(path: String, perms: String) {
        ShellUtils.fastCmd("su -c chmod $perms $path")
    }

    fun createWinFolder(context: Context, path: String) {
        if (!State.isWindowsMounted) Commands.mountWindows(context, false)
        if (State.getFailed()) return

        ShellUtils.fastCmd("su -c mkdir ${getMountDir()}/$path ")

    }

    fun copyFileToWin(context: Context, path: String, newPath: String) {
        if (!State.isWindowsMounted) Commands.mountWindows(context, false)
        if (State.getFailed()) {
            ShellUtils.fastCmd("su -c cp $path /sdcard/${newPath.split("/").last()}")
            return
        }
        ShellUtils.fastCmd("su -c cp $path ${getMountDir()}/$newPath")
    }

    fun moveFileToWin(context: Context, path: String, newPath: String) {
        if (!State.isWindowsMounted) Commands.mountWindows(context, false)
        if (State.getFailed()) {
            ShellUtils.fastCmd("su -c mv $path /sdcard/${newPath.split("/").last()}")
            return
        }
        ShellUtils.fastCmd("su -c mv $path ${getMountDir()}/$newPath")
    }

    fun moveFile(path: String, newPath: String) {
        ShellUtils.fastCmd("su -c mv $path $newPath")
    }

    fun remove(path: String) {
        ShellUtils.fastCmd("su -c rm -rf $path")
    }

    fun copyAsset(name: String, perms: String? = null, ignoreIfPresent: Boolean = false) {
        val outputFilePath = File(Strings.assets.data, name)

        if (checkFile("${Strings.assets.data}/$name") && ignoreIfPresent) return

        try {
            val inputStream: InputStream = appContext.assets.open(name)
            val outputStream = FileOutputStream(outputFilePath)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Only set permissions if NOT on NTFS and necessary
            if (perms != null) {
                Runtime.getRuntime().exec(arrayOf("chmod", perms, outputFilePath.absolutePath))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error copying file: ${e.message}")
        }
    }

    /*fun copyAsset(name: String, perms: String? = null, alert: Boolean = false) {

        val outputFilePath = Paths.data + "/$name"

        try {
            val tempFile = File(appContext.cacheDir, name)
            val inputStream: InputStream = appContext.assets.open(name)
            val outputStream = FileOutputStream(tempFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            ShellUtils.fastCmd("su -c cp ${tempFile.absolutePath} $outputFilePath")

            if (perms != null) ShellUtils.fastCmd("su -c chmod $perms $outputFilePath")

            if (alert) ToastUtil.showToast("Asset $name was copied to path ${Paths.data}")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error copying file: ${e.message}")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            println("Process was interrupted: ${e.message}")
        }
    }*/

    fun copyStaFiles(context: Context) {
        remove("${getMountDir()}/switchtoandroid")
        createWinFolder(context, Strings.win.folders.sta)
        copyFileToWin(context, Strings.assets.sta, Strings.win.staBin)
        copyFileToWin(context, Strings.assets.staLink, Strings.win.staLink)

        copyFileToWin(context, Strings.assets.sdd, Strings.win.sddBin)
        copyFileToWin(context, Strings.assets.sddConfig, Strings.win.sddConfig)

        copyFileToWin(context, Strings.assets.autoFlasher, Strings.win.autoFlasher)
    }

    fun copyArmSoftwareLinks(context: Context) {
        createWinFolder(context, Strings.win.folders.toolbox)
        copyFileToWin(context, Strings.assets.ARMRepoLink, Strings.win.ARMRepoLink)
        copyFileToWin(context, Strings.assets.ARMSoftwareLink, Strings.win.ARMSoftwareLink)
        copyFileToWin(context, Strings.assets.TestedSoftwareLink, Strings.win.TestedSoftwareLink)
        copyFileToWin(context, Strings.assets.WorksOnWoaLink, Strings.win.WorksOnWoaLink)
    }

    fun checkFolder(path: String): Boolean {
        val folder = File(path)
        return folder.exists() && folder.isDirectory
    }

    fun checkFile(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.isFile
    }

    fun getBootPartition(): String? {
        val paths = listOf("boot", "BOOT")

        val partition = paths
            .map { ShellUtils.fastCmd("find /dev/block | grep $it$(getprop ro.boot.slot_suffix)") }
            .firstOrNull { it.isNotEmpty() }
            ?: return run {
                null
            }

        return ShellUtils.fastCmd("su -c realpath $partition")
    }

    fun getWinPartition(context: Context): String? = runBlocking {
        val regex = "win|windows|mindows|Win|Windows|Mindows"

        val findCmd = "su -c find /dev/block | grep -E '$regex'"
        val result = ShellUtils.fastCmd(findCmd).trim()

        if (result.isNotEmpty()) {
            val firstMatch = result.lines().first().trim()

            Log.d("INFO", ShellUtils.fastCmd("su -c realpath $firstMatch").trim())

            ShellUtils.fastCmd("su -c realpath $firstMatch").trim()
        } else {
            if (notifyIfNoWinPartition) {
                Info.noWinPartition(context)
            }
            null
        }
    }

    /*fun getWinPartition(context: Context): String? = runBlocking {
        val paths = listOf("win", "windows", "mindows", "Win", "Windows", "Mindows")

        coroutineScope {  // All coroutines must complete before returning
            paths.map { path ->
                async(Dispatchers.IO) {  // Check all paths in parallel
                    ShellUtils.fastCmd("find /dev/block | grep $path").takeIf { it.isNotEmpty() }
                    //Cmd.execute("su -c find /dev/block | grep $path").takeIf { it.isNotEmpty() }
                }
            }.firstOrNull { deferred ->  // Get first successful result
                deferred.await() != null
            }?.let { result ->
                ShellUtils.fastCmd("su -c realpath ${result.await()}")
            } ?: run {  // If all failed
                if (notifyIfNoWinPartition) {
                    withContext(Dispatchers.Main) {
                        Info.noWinPartition(context)
                    }
                }
                null
            }
        }
    }*/

/*    fun getWinPartition(context: Context): String? {
        val paths = listOf(
            "win",
            "windows",
            "mindows",
            "Win",
            "Windows",
            "Mindows"
        )

        val partition = paths
            .map { ShellUtils.fastCmd("find /dev/block | grep $it") }
            .firstOrNull { it.isNotEmpty() }
            ?: return run {
                if (notifyIfNoWinPartition) Info.noWinPartition(context)
                null
            }

        return ShellUtils.fastCmd("su -c realpath $partition")
    }*/

    fun getMountDir(): String {
        return if (Preferences.getBoolean(
                Preferences.Preference.SETTINGS,
                Preferences.Key.MOUNTTOMNT,
                false
            )
        ) Strings.folders.win1 else Strings.folders.win
    }

    fun getResource(id: Int): Drawable {
        return appContext.getDrawable(id)!!
    }

    fun selectUefiImage(callback: () -> Unit = {}) {
        FilePicker.pickFile { path ->
            if (!checkExtension(path, Extension.IMG)) return@pickFile
            copy(path, Strings.uefiImg)
            callback()
        }
    }
}