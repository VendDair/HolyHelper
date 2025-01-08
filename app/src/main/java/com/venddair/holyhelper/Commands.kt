package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import com.topjohnwu.superuser.ShellUtils
import java.io.BufferedReader
import java.io.InputStreamReader

object Commands {

    fun dumpModem() {
        ShellUtils.fastCmd("su -c dd bs=8M if=/dev/block/by-name/modemst1 of=$(find ${Files.getMountDir()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs1 bs=4M")
        ShellUtils.fastCmd("su -c dd bs=8M if=/dev/block/by-name/modemst2 of=$(find ${Files.getMountDir()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs2 bs=4M")
    }

    fun isWindowsMounted(context: Context): Boolean {
        var isMounted = false
        Files.getWinPartition(context) { path ->
            isMounted = ShellUtils.fastCmd("su -c mount | grep $path").isNotEmpty()
        }

        return isMounted
    }

    fun askUserToMountIfNotMounted(context: Context, callback: () -> Unit) {
        if (!isWindowsMounted(context)) Info.winNotMounted(context) { mounted ->
            if (mounted)
                callback()
        }
        else callback()
    }

    fun backupBootImage(context: Context) {
        ShellUtils.fastCmd("su -c dd bs=8M if=${Paths.bootPartition} of=${Paths.bootImage}")
        if (isWindowsMounted(context)) {
            val winpath = if (Preferences.get("settings").getBoolean("mountToMnt", false)) Paths.winPath1 else Paths.winPath
            Files.copy(Paths.bootPartition, "$winpath/boot.img")
        }
    }

    fun bootInWindows(context: Context, reboot: Boolean = false) {
        backupBootImage(context)
        ShellUtils.fastCmd("su -c dd if=${Paths.uefiImg} of=${Paths.bootPartition} bs=8M")
        if (reboot) ShellUtils.fastCmd("su -c reboot")
    }

    fun getDevice(): String {
        return ShellUtils.fastCmd("getprop ro.product.device").replace("\n", "")
    }

    fun mountWindows(context: Context): Boolean {
        val mountPath: String = if (Preferences.get("settings").getBoolean("mountToMnt", false)) Paths.winPath1
        else Paths.winPath

        if (isWindowsMounted(context)) {
            ShellUtils.fastCmd("su -mm -c umount $mountPath")
            return true
        }
        Files.createFolder(mountPath)

        Files.getWinPartition(context) { path ->
            ShellUtils.fastCmd("su -mm -c ${Paths.mountNtfs} $path $mountPath")
        }

        if (!isWindowsMounted(context)) {
            Info.winUnableToMount(context)
            return false
        }

        return true
    }

    @SuppressLint("SdCardPath")
    fun dbkp(context: Context) {
        Files.setupDbkpFiles(context)
        Download.download(context, "https://github.com/n00b69/woa-op7/releases/download/DBKP/dbkp", "dbkp") { dbkp ->
            Files.moveFile("${Paths.downloads}/$dbkp", Paths.data)
            Files.setPerms(Paths.dbkpAsset, "777")

            val (url, fileName) = when (getDevice()) {
                "guacamole", "OnePlus7Pro", "OnePlus7Pro4G" ->
                    "https://github.com/n00b69/woa-op7/releases/download/DBKP/guacamole.fd" to listOf("guacamole.fd", "hotdog")
                "hotdog", "OnePlus7TPro", "OnePlus7TPro4G" ->
                    "https://github.com/n00b69/woa-op7/releases/download/DBKP/hotdog.fd" to listOf("hotdog.fd", "hotdog")
                "cepheus" ->
                    "https://github.com/n00b69/woa-everything/releases/download/Files/cepheus.fd" to listOf("cepheus.fd", "cepheus")
                "nabu" ->
                    "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5/releases/download/1.0/nabu.fd" to listOf("nabu.fd", "nabu")
                else -> return@download
            }

            val dbkpDir = "/sdcard/dbkp"

            // Execute commands
            Download.download(context, url, fileName[0]) { name ->
                Files.moveFile("${Paths.downloads}/$name", dbkpDir)
                ShellUtils.fastCmd("cd $dbkpDir")
                ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) unpack boot.img\" | su -c sh")
                ShellUtils.fastCmd("su -mm -c ${Paths.data}/dbkp kernel ${fileName[0]} output dbkp8150.cfg dbkp.${fileName[1]}.bin")
                ShellUtils.fastCmd("su -mm -c rm kernel")
                ShellUtils.fastCmd("su -mm -c mv output kernel")
                ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) repack boot.img\" | su -c sh")
                ShellUtils.fastCmd("su -mm -c cp new-boot.img /sdcard/patched-boot.img")
                ShellUtils.fastCmd("rm -r $dbkpDir")

                if (getDevice() == "cepheus") {
                    ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot bs=16M")
                }
                else {
                    ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_a bs=16M")
                    ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_b bs=16M")
                }

            }

        }



        /*when (getDevice()) {
            "guacamole", "OnePlus7Pro", "OnePlus7Pro4G" -> {
                Download.download(context, "https://github.com/n00b69/woa-op7/releases/download/DBKP/guacamole.fd", "guacamole.fd") { name ->
                    Files.moveFile(Paths.downloads+"/$name", "/sdcard/dbkp/guacamole.fd")
                    ShellUtils.fastCmd("cd /sdcard/dbkp")
                    ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) unpack boot.img\" | su -c sh")
                    ShellUtils.fastCmd("su -mm -c ${Paths.dbkpAsset} /sdcard/dbkp/kernel /sdcard/dbkp/guacamole.fd /sdcard/dbkp/output /sdcard/dbkp/dbkp8150.cfg /sdcard/dbkp/dbkp.hotdog.bin")
                    Files.remove("/sdcard/dbkp/kernel")
                    Files.moveFile("output" , "kernel")
                    ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) repack boot.img\" | su -c sh")
                    Files.copy("new-boot.img", "/sdcard/new-boot.img")
                    Files.moveFile("/sdcard/new-boot.img", "/sdcard/patched-boot.img")
                    Files.remove("rm -r /sdcard/dbkp")
                    ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_a bs=16M")
                    ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_b bs=16M")
                }
            }
        }*/

    }
}