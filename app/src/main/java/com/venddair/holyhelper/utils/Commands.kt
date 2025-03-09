package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.ShellUtils
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


object Commands {

    fun dumpModem(context: Context) {
        if (!State.isWindowsMounted) mountWindows(context, false)
        if (State.getFailed()) return
        ShellUtils.fastCmd("su -c dd bs=8M if=/dev/block/by-name/modemst1 of=$(find ${Files.getMountDir()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs1 bs=4M")
        ShellUtils.fastCmd("su -c dd bs=8M if=/dev/block/by-name/modemst2 of=$(find ${Files.getMountDir()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs2 bs=4M")
    }

    fun isWindowsMounted(): Boolean {
        return ShellUtils.fastCmd("grep -B1 -A5 \"${State.winPartition}\" /proc/mounts | tail -10").isNotEmpty()
    }


    fun backupBootImage(context: Context, windows: Boolean = false) {
        val startTime = System.currentTimeMillis()

        if (windows && !State.isWindowsMounted) mountWindows(context, false)
        val bootImage = if (windows) Strings.bootImage1 else Strings.bootImage

        if (State.bootPartition == null) return

        ShellUtils.fastCmd("su -c dd bs=8m if=${State.bootPartition} of=${bootImage}")
        MainActivity.updateMountText(context)
        MainActivity.updateLastBackupDate(context)

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", "Backup boot image: $elapsedTime")
    }

    fun bootInWindows(context: Context, reboot: Boolean = false) {
        if (!Files.checkFile(Strings.uefiImg)) return

        val backupBoot = Preferences.BACKUPBOOT.get()
        val backupToAndroid = Preferences.BACKUPBOOTANDROID.get()
        val backupToWindows = Preferences.BACKUPBOOTWINDOWS.get()

        if (!Files.checkFile(Strings.bootImage) && backupBoot && backupToAndroid) backupBootImage(context)
        if (!Files.checkFile(Strings.bootImage1) && backupBoot && backupToWindows) backupBootImage(context, true)

        if (State.bootPartition == null) return

        ShellUtils.fastCmd("su -c dd if=\"${Strings.uefiImg}\" of=${State.bootPartition} bs=8M")
        if (reboot) ShellUtils.fastCmd("su -c reboot")
    }

    private fun tryMount(mountPath: String) {

        val command = "su -mm -c 'cd ${Strings.assets.data} && ./mount.ntfs ${State.winPartition} $mountPath'"
        val result = ShellUtils.fastCmd(command)
        State.viewModel.isWindowsMounted.update { result.isEmpty() }
    }


    fun mountWindows(context: Context, unmountIfMounted: Boolean = true): Boolean {
        val startTime = System.currentTimeMillis()

/*        State.setFailed(true)

        CoroutineScope(Dispatchers.Main).launch {
            Info.winUnableToMount(context)
        }

        return false*/

        val mountDir = Files.getMountDir()

        if (State.isWindowsMounted) {
            if (unmountIfMounted) {
                ShellUtils.fastCmd("su -mm -c umount $mountDir")

                CoroutineScope(Dispatchers.Main).launch {
                    Files.remove(mountDir)
                }
                State.viewModel.isWindowsMounted.update { false }
                MainActivity.updateMountText(context)

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime

                Log.d("INFO", "unmounting: $elapsedTime")
                return true
            }
            State.viewModel.isWindowsMounted.update { true }
            return true
        }

        Files.createFolder(mountDir)
        tryMount(mountDir)


        if (!State.isWindowsMounted && mountDir == Strings.folders.win) {
            Preferences.MOUNTTOMNT.set(true)
            tryMount(mountDir)
            if (!State.isWindowsMounted) Preferences.MOUNTTOMNT.set(false)
        }

        if (!State.isWindowsMounted) {
            State.setFailed(true)
            Info.winUnableToMount(context)
            State.viewModel.isWindowsMounted.update { false }
            return false
        }

        State.viewModel.isWindowsMounted.update { true }
        MainActivity.updateMountText(context)

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", "mounting: $elapsedTime")

        return true
    }

    @SuppressLint("SdCardPath", "StringFormatInvalid")
    suspend fun dbkp(context: ComponentActivity) {
        Files.setupDbkpFiles(context)

        val dbkp = Download.download(
            context,
            "https://github.com/n00b69/woa-op7/releases/download/DBKP/dbkp",
            "dbkp"
        )
        if (dbkp == null) {
            State.setFailed(true)
            return
        }

        Files.moveFile(dbkp, Strings.assets.data)
        Files.setPerms(Strings.assets.dbkp, "777")

        val (url, fileName) = Device.getDbkpDownloadInfo()


        val dbkpDir = "/sdcard/dbkp"

        // Execute commands
        val path = Download.download(context, url, fileName[0])
        if (path == null) {
            State.setFailed(true)
            return
        }
        Files.moveFile(path, dbkpDir)
        ShellUtils.fastCmd("cd $dbkpDir")
        ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) unpack boot.img\" | su -c sh")
        ShellUtils.fastCmd("su -mm -c ${Strings.assets.data}/dbkp kernel ${fileName[0]} output dbkp8150.cfg dbkp.${fileName[1]}.bin")
        ShellUtils.fastCmd("su -mm -c rm kernel")
        ShellUtils.fastCmd("su -mm -c mv output kernel")
        ShellUtils.fastCmd("echo \"$(su -mm -c find /data/adb -name magiskboot) repack boot.img\" | su -c sh")
        ShellUtils.fastCmd("su -mm -c cp new-boot.img /sdcard/patched-boot.img")
        ShellUtils.fastCmd("rm -rf $dbkpDir")

        if (Device.get() == "cepheus") {
            ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot bs=16M")
        } else {
            ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_a bs=16M")
            ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_b bs=16M")
        }
    }
}