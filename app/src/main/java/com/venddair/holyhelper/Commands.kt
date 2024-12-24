package com.venddair.holyhelper

import android.content.Context
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

    fun dumoModem() {
        execute("su -c dd bs=8M if=/dev/block/by-name/modemst1 of=$(find ${Files.getMountDir()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs1 bs=4M")
        execute("su -c dd bs=8M if=/dev/block/by-name/modemst2 of=$(find ${Files.getMountDir()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs2 bs=4M")
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

    fun bootInWindows(reboot: Boolean = false) {
        backupBootImage()
        execute("su -c dd if=${Paths.uefiImg} of=${Paths.bootPartition} bs=8M")
        if (reboot) execute("su -c reboot")
    }

    fun getDevice(): String {
        return execute("getprop ro.product.device").replace("\n", "")
    }

    fun mountWindows(context: Context): Boolean {
        val mountPath: String = if (Preferences.get("settings").getBoolean("mountToMnt", false)) Paths.winPath1
        else Paths.winPath

        if (isWindowsMounted()) {
            execute("su -mm -c umount /sdcard/Windows")
            return true
        }
        Files.createFolder(mountPath)

        execute("su -c sh -c 'cd ${Paths.data} && su -mm -c ${Paths.mountNtfs} ${Files.getWinPartition()} $mountPath'")

        if (!isWindowsMounted()) {
            Info.winUnableToMount(context)
            return false
        }

        return true
    }
}