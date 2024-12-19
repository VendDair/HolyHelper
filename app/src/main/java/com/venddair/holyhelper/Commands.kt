package com.venddair.holyhelper

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
        execute("su -c dd bs=8M if=${Files.paths["bootBlock"]} of=${Files.paths["bootImage"]}")
        if (isWindowsMounted()) Files.copyFile(Files.paths["bootImage"]!!, Files.paths["mount"]!! + "/boot.img")
    }

    fun bootInWindows() {
        backupBootImage()
        execute("su -c dd if=${Files.paths["uefi"]} of=${Files.paths["bootBlock"]} bs=8M")
    }

    fun getDevice(): String {
        return execute("getprop ro.product.device").replace("\n", "")
    }

    fun mountWindows(): Boolean {
        val mountPath: String = if (Preferences.get("settings").getBoolean("mountToMnt", false)) Files.paths["mount1"]!!
        else Files.paths["mount"]!!

        if (isWindowsMounted()) {
            execute("su -mm -c umount /sdcard/Windows")
            return false
        }
        Files.createFolder(mountPath)

        execute("su -c sh -c 'cd ${Files.paths["data"]} && su -mm -c ${Files.paths["mount.ntfs"]} ${Files.getWinPartition()} $mountPath'")

        return isWindowsMounted()
    }
}