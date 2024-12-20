package com.venddair.holyhelper

import android.graphics.drawable.shapes.PathShape
import java.io.BufferedReader
import java.io.InputStreamReader

object Commands {
    fun execute(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(command)

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }

            process.waitFor()

            output.toString()
        } catch (e: Exception) {
            "Error executing command: ${e.message}"
        }
    }

    fun isWindowsMounted(): Boolean {
        val isMounted = execute("su -c mount | grep ${Files.getWinPartition()}")
        return isMounted.isNotEmpty()
    }

    fun backupBootImage() {
        execute("su -c dd bs=8M if=${Paths.bootPartition} of=${Paths.bootImage}")
        if (isWindowsMounted()) {
            val winpath = if (Preferences.get("settings").getBoolean("mountToMnt", false)) Paths.winPath1 else Paths.winPath
            Files.copyFile(Paths.bootPartition, "$winpath/boot.img")
        }
    }

    fun bootInWindows() {
        backupBootImage()
        execute("su -c dd if=${Paths.uefiImg} of=${Paths.bootPartition} bs=8M")
    }

    fun getDevice(): String {
        return execute("getprop ro.product.device").replace("\n", "")
    }

    fun mountWindows(): Boolean {
        val mountPath: String = if (Preferences.get("settings").getBoolean("mountToMnt", false)) Paths.winPath1
        else Paths.winPath

        if (isWindowsMounted()) {
            execute("su -mm -c umount /sdcard/Windows")
            return false
        }
        Files.createFolder(mountPath)

        execute("su -c sh -c 'cd ${Paths.data} && su -mm -c ${Paths.mountNtfs} ${Files.getWinPartition()} $mountPath'")

        return isWindowsMounted()
    }
}