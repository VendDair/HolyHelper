package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.ShellUtils
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Commands.backupBootImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream

object Files {
    @SuppressLint("SdCardPath")

    private lateinit var appContext: Context


    enum class Extension(val key: String) {
        IMG(".img"),
    }


    @SuppressLint("SdCardPath")
    fun init(context: Context) {
        appContext = context

        Strings.assets.data = context.filesDir.toString()

        CoroutineScope(Dispatchers.Main).launch {
            createFolder(Strings.assets.data)
            createFolder(Strings.folders.uefi)

            copyAsset("data.zip", ignoreIfPresent = true)
            copyAsset("RemoveEdge.bat", ignoreIfPresent = true)
            copyAsset("sdd.conf", ignoreIfPresent = true)
            copyAsset("RotationShortcutReverseLandscape.lnk", ignoreIfPresent = true)
            copyAsset("RotationShortcut.lnk", ignoreIfPresent = true)
            copyAsset("install.bat", ignoreIfPresent = true)
            copyAsset("dbkp8150.cfg", ignoreIfPresent = true)
            copyAsset("dbkp.hotdog.bin", ignoreIfPresent = true)
            copyAsset("dbkp.cepheus.bin", ignoreIfPresent = true)
            copyAsset("dbkp.nabu.bin", ignoreIfPresent = true)
            copyAsset("Android.lnk", ignoreIfPresent = true)
            copyAsset("ARMRepo.url", ignoreIfPresent = true)
            copyAsset("ARMSoftware.url", ignoreIfPresent = true)
            copyAsset("TestedSoftware.url", ignoreIfPresent = true)
            copyAsset("WorksOnWoa.url", ignoreIfPresent = true)

            if (!checkFile(Strings.assets.dataZip)) {
                Log.d("INFO", "WRONG")
                State.measureTime("UNZIPPING") {
                    decompressZip(Strings.assets.dataZip, Strings.assets.data)
                    setPerms(Strings.assets.mountNtfs, "777")
                }
            }
        }


        /*CoroutineScope(Dispatchers.IO).launch {
            createFolder(Strings.assets.data)
            createFolder(Strings.folders.uefi)

            copyAsset("mount.ntfs", "+x", true)
            copyAsset("sta.exe", ignoreIfPresent = true)
            copyAsset("sdd.exe", ignoreIfPresent = true)
            copyAsset("boot_img_auto-flasher_V1.1.exe", ignoreIfPresent = true)
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
            copyAsset("Optimized_Taskbar_Control_V3.0.exe", ignoreIfPresent = true)
        }*/
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

        ShellUtils.fastCmd("su -c mkdir -p ${getMountDir()}/$path")

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

            if (perms != null) {
                Runtime.getRuntime().exec(arrayOf("chmod", perms, outputFilePath.absolutePath))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error copying file: ${e.message}")
        }
    }

    fun decompressZip(zipFilePath: String, destDirectory: String) {
        val destDir = File(destDirectory)
        if (!destDir.exists()) {
            destDir.mkdirs() // Create the destination directory if it doesn’t exist
        }
        val zipIn = ZipInputStream(FileInputStream(zipFilePath))
        var entry = zipIn.nextEntry
        while (entry != null) {
            val filePath = destDirectory + File.separator + entry.name
            if (!entry.isDirectory) {
                // Extract file
                extractFile(zipIn, filePath)
            } else {
                // Create directory
                File(filePath).mkdirs()
            }
            zipIn.closeEntry()
            entry = zipIn.nextEntry
        }
        zipIn.close()
    }

    private fun extractFile(zipIn: ZipInputStream, filePath: String) {
        val bos = FileOutputStream(filePath)
        val bytesIn = ByteArray(4096) // Buffer for reading
        var read: Int
        while (zipIn.read(bytesIn).also { read = it } != -1) {
            bos.write(bytesIn, 0, read)
        }
        bos.close()
    }

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

    fun copyRotationFiles(context: ComponentActivity) {
        createWinFolder(context, Strings.win.folders.rotation)

        copyFileToWin(
            context,
            Strings.assets.display,
            Strings.win.display
        )
        copyFileToWin(
            context,
            Strings.assets.RotationShortcut,
            Strings.win.RotationShortcut
        )
        copyFileToWin(
            context,
            Strings.assets.RotationShortcutReverseLandscape,
            Strings.win.RotationShortcutReverseLandscape
        )

        copyFileToWin(
            context,
            Strings.assets.RotationShortcutReverseLandscape,
            Strings.win.RotationShortcutReverseLandscape
        )
        copyFileToWin(
            context,
            Strings.assets.RotationShortcut,
            Strings.win.RotationShortcut
        )
        copyFileToWin(
            context,
            Strings.assets.RotationShortcut,
            Strings.win.RotationShortcutDesktop
        )
        copyFileToWin(
            context,
            Strings.assets.RotationShortcutReverseLandscape,
            Strings.win.RotationShortcutReverseLandscapeDesktop
        )
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
        val slotSuffix = ShellUtils.fastCmd("getprop ro.boot.slot_suffix").trim()

        val partitions = ShellUtils.fastCmd("find /dev/block | grep -i 'boot' | tr '\\n' ' '").trim().split(" ")

        for (partition in partitions)
            if (partition.lowercase().endsWith("/boot$slotSuffix") || partition.lowercase().endsWith("/init_boot$slotSuffix")) {
                Log.d("INFO", "boot partition: $partition")
                return partition
            }

        return null
    }

    /*fun getBootPartition(): String? {
        val paths = listOf("boot", "BOOT")

        val partition = paths
            .map { ShellUtils.fastCmd("find /dev/block | grep $it$(getprop ro.boot.slot_suffix)") }
            .firstOrNull { it.isNotEmpty() }
            ?: return run {
                null
            }

        return ShellUtils.fastCmd("su -c realpath $partition")
    }*/

    fun getWinPartition(): String? {
        val partitions = ShellUtils.fastCmd("find /dev/block | grep -Ei 'win|windows|mindows' | tr '\\n' ' '").trim().split(" ")

        for (partition in partitions) {
            val lowerPartition = partition.lowercase()
            if (lowerPartition.endsWith("win") || lowerPartition.endsWith("windows") || lowerPartition.endsWith("mindows")) {
                Log.d("INFO", "win partition: $partition")
                return ShellUtils.fastCmd("su -c realpath $partition").trim()
            }
        }

        return null
    }

    /*fun getWinPartition(context: Context): String? = runBlocking {
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
    }*/

    fun getMountDir(): String {
        return if (Preferences.MOUNTTOMNT.get()) Strings.folders.win1 else Strings.folders.win
    }

    fun getResource(id: Int): Drawable {
        return appContext.getDrawable(id)!!
    }

    /*fun selectUefiImage(callback: () -> Unit = {}) {
        FilePicker.pickFile { path ->
            if (!checkExtension(path, Extension.IMG)) return@pickFile
            copy(path, Strings.uefiImg)
            callback()
        }
    }*/
}