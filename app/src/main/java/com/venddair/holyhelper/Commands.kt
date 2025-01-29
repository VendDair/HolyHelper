package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.ShellUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Commands {

    var updateChecked = false

    var notifyIfNoWinPartition = true

    fun isWindowsMounted(context: Context): Boolean {
        var isMounted = false
        Files.getWinPartition(context) { path ->
            isMounted = ShellUtils.fastCmd("su -c mount | grep $path").isNotEmpty()
        }

        return isMounted
    }

    fun backupBootImage(context: Context, mountButton: Button? = null, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            ShellUtils.fastCmd("su -c dd bs=8M if=${Paths.bootPartition} of=${Paths.bootImage}")

            withContext(Dispatchers.Main) {
                if (mountWindows(context, false)) {
                    Files.copyFileToWin(context, Paths.bootImage, "boot.img")
                    mountButton?.setTitle(if (isWindowsMounted(context)) context.getString(R.string.mnt_title, context.getString(R.string.unmountt)) else context.getString(R.string.mnt_title, context.getString(R.string.mountt)))
                    callback()
                }
            }
        }

    }

    fun bootInWindows(context: Context, reboot: Boolean = false) {
        /*if (!Files.checkFile(Paths.uefiImg)) {
            Info.uefiNotFound(context, reboot)
            return
        }*/
        ShellUtils.fastCmd("su -c dd if=${Paths.uefiImg} of=${Paths.bootPartition} bs=8M")
        if (reboot) ShellUtils.fastCmd("su -c reboot")
        /*backupBootImage(context) {

        }*/

    }

    private fun tryMount(context: Context, mountPath: String) {
        Files.getWinPartition(context) { path ->
            ShellUtils.fastCmd("cd ${Paths.data}")
            ShellUtils.fastCmd("su -mm -c ./mount.ntfs $path $mountPath")
            ShellUtils.fastCmd("cd")
        }
    }

    fun mountWindows(context: Context, unmountIfMounted: Boolean = true): Boolean {

        if (isWindowsMounted(context) && unmountIfMounted) {
            ShellUtils.fastCmd("su -mm -c umount ${Files.getMountDir()}")
            Files.remove(Files.getMountDir())
            return true
        }

        if (isWindowsMounted(context)) return true

        Files.createFolder(Files.getMountDir())

        tryMount(context, Files.getMountDir())

        // Mount to /mnt if /mnt/sdcard failed
        if (!isWindowsMounted(context) && Files.getMountDir() == Paths.winPath) {
            Preferences.putBoolean(Preferences.Preference.SETTINGS, Preferences.Key.MOUNTTOMNT, true)
            tryMount(context, Files.getMountDir())
            if (!isWindowsMounted(context)) Preferences.putBoolean(Preferences.Preference.SETTINGS, Preferences.Key.MOUNTTOMNT, false)
        }
        if (!isWindowsMounted(context)) {
            Info.winUnableToMount(context)
            return false
        }

        return true
    }

    fun checkUpdate(context: ComponentActivity) {
        if (Preferences.getBoolean(Preferences.Preference.SETTINGS, Preferences.Key.DISABLEUPDATES, false)) return
        if (updateChecked) return
        Download.getRemoteFileContent(context, "https://github.com/VendDair/HolyHelper/releases/download/files/version") { content ->
            val version = content.replace("\n", "")
            if (version != Paths.version) {
                Info.notifyAboutUpdate(context, version)
                updateChecked = true
            }
        }
    }

    @SuppressLint("SdCardPath", "StringFormatInvalid")
    fun dbkp(context: ComponentActivity) {
        Files.setupDbkpFiles(context) {
            Download.download(context, "https://github.com/n00b69/woa-op7/releases/download/DBKP/dbkp", "dbkp") { dbkp, _ ->
                Files.moveFile(dbkp, Paths.data)
                Files.setPerms(Paths.dbkpAsset, "777")

                val (url, fileName) = Device.getDbkpDownloadInfo()

                val dbkpButton = Device.getDbkpButton(context)

                val dbkpDir = "/sdcard/dbkp"

                // Execute commands
                Download.download(context, url, fileName[0]) { path, _ ->
                    Files.moveFile(path, dbkpDir)
                    ShellUtils.fastCmd("cd $dbkpDir")
                    ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) unpack boot.img\" | su -c sh")
                    ShellUtils.fastCmd("su -mm -c ${Paths.data}/dbkp kernel ${fileName[0]} output dbkp8150.cfg dbkp.${fileName[1]}.bin")
                    ShellUtils.fastCmd("su -mm -c rm kernel")
                    ShellUtils.fastCmd("su -mm -c mv output kernel")
                    ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) repack boot.img\" | su -c sh")
                    ShellUtils.fastCmd("su -mm -c cp new-boot.img /sdcard/patched-boot.img")
                    ShellUtils.fastCmd("rm -r $dbkpDir")

                    if (Device.get() == "cepheus") {
                        ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot bs=16M")
                    }
                    else {
                        ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_a bs=16M")
                        ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_b bs=16M")
                    }

                    UniversalDialog.showDialog(context,
                        title = context.getString(R.string.dbkp, dbkpButton),
                        image = R.drawable.ic_uefi,
                        buttons = listOf(
                            Pair(context.getString(R.string.dismiss)) {}
                        )
                    )

                }

            }
        }
    }
}