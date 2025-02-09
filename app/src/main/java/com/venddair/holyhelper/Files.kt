package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.ShellUtils
import com.venddair.holyhelper.Commands.backupBootImage
import com.venddair.holyhelper.Commands.notifyIfNoWinPartition
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

        Paths.data = context.filesDir.toString()

        CoroutineScope(Dispatchers.IO).launch {
            createFolder(Paths.data)
            createFolder(Paths.uefiFolder)
            createFolder(Paths.winPath)

            copyAsset("mount.ntfs", "+x")
            copyAsset("sta.exe")
            copyAsset("sdd.exe")
            copyAsset("boot_img_auto-flasher_V1.0.exe")
            copyAsset("sdd.conf")
            copyAsset("libntfs-3g.so")
            copyAsset("libfuse-lite.so", "777")
            copyAsset("Android.lnk")
            copyAsset("ARMRepo.url")
            copyAsset("ARMSoftware.url")
            copyAsset("TestedSoftware.url")
            copyAsset("WorksOnWoa.url")
            copyAsset("dbkp8150.cfg")
            copyAsset("dbkp.hotdog.bin")
            copyAsset("dbkp.cepheus.bin")
            copyAsset("dbkp.nabu.bin")
            copyAsset("usbhostmode.exe")
            copyAsset("display.exe")
            copyAsset("RotationShortcut.lnk")
            copyAsset("install.bat")
            copyAsset("RemoveEdge.bat")
        }

/*        Thread {
            createFolder(Paths.uefiFolder)
            createFolder(Paths.winPath)
            createFolder(Paths.data)

            copyAsset("mount.ntfs", "+x")
            copyAsset("sta.exe")
            copyAsset("sdd.exe")
            copyAsset("boot_img_auto-flasher_V1.0.exe")
            copyAsset("sdd.conf")
            copyAsset("boot_img_auto-flasher_V1.0.exe")
            copyAsset("libntfs-3g.so", "777")
            copyAsset("libfuse-lite.so", "777")
            copyAsset("Android.lnk")
            copyAsset("ARMRepo.url")
            copyAsset("ARMSoftware.url")
            copyAsset("TestedSoftware.url")
            copyAsset("WorksOnWoa.url")
            copyAsset("dbkp8150.cfg")
            copyAsset("dbkp.hotdog.bin")
            copyAsset("dbkp.cepheus.bin")
            copyAsset("dbkp.nabu.bin")
            copyAsset("usbhostmode.exe")
            copyAsset("display.exe")
            copyAsset("RotationShortcut.lnk")
            copyAsset("install.bat")
            copyAsset("RemoveEdge.bat")
        }.start()*/


    }

    fun checkExtension(path: String, extension: Extension): Boolean {
        return path.endsWith(extension.key)
    }

    fun createFolder(path: String, alert: Boolean = false) {
/*        if (checkFolder(path)) return
        if (alert) ToastUtil.showToast("Folder $path was created")*/
        ShellUtils.fastCmd("su -c mkdir $path")
    }

    @SuppressLint("SdCardPath")
    suspend fun setupDbkpFiles(context: ComponentActivity) = coroutineScope {
        withContext(Dispatchers.Main) {
            backupBootImage(context)
        }
        createFolder("/sdcard/dbkp")
        remove("/sdcard/original-boot.img")
        copy(Paths.bootImage, "/sdcard/dbkp/boot.img")
        moveFile(Paths.bootImage, "/sdcard/original-boot.img")
        copy(Paths.dbkp8150CfgAsset, "/sdcard/dbkp")
        copy(Paths.dbkpHotdogBinAsset, "/sdcard/dbkp")
        copy(Paths.dbkpCepheusBinAsset, "/sdcard/dbkp")
        copy(Paths.dbkpNabuBinAsset, "/sdcard/dbkp")
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

    fun copyAsset(name: String, perms: String? = null, alert: Boolean = false) {
        val outputFilePath = File(Paths.data, name) // Ensure Paths.data isn't NTFS-mounted

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

            if (alert) ToastUtil.showToast("Asset $name copied to ${outputFilePath.parent}")
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
        createWinFolder(context, Paths.sta)
        copyFileToWin(context, Paths.staAsset, Paths.staBin)
        copyFileToWin(context, Paths.staLinkAsset, Paths.staLink)

        copyFileToWin(context, Paths.sddAsset, Paths.sdd)
        copyFileToWin(context, Paths.sddConfigAsset, Paths.sddConfig)

        copyFileToWin(context, Paths.autoFlasherAsset, Paths.autoFlasher)
    }

    fun copyArmSoftwareLinks(context: Context) {
        createFolder(Paths.toolbox)
        copyFileToWin(context, Paths.ARMRepoLinkAsset, Paths.ARMRepoLink)
        copyFileToWin(context, Paths.ARMSoftwareLinkAsset, Paths.ARMSoftwareLink)
        copyFileToWin(context, Paths.TestedSoftwareLinkAsset, Paths.TestedSoftwareLink)
        copyFileToWin(context, Paths.WorksOnWoaLinkAsset, Paths.WorksOnWoaLink)
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
        ) Paths.winPath1 else Paths.winPath
    }

    fun getResource(id: Int): Drawable {
        return appContext.getDrawable(id)!!
    }

    fun selectUefiImage(callback: () -> Unit = {}) {
        FilePicker.pickFile { path ->
            if (!checkExtension(path, Extension.IMG)) return@pickFile
            copy(path, Paths.uefiImg)
            callback()
        }
    }
}