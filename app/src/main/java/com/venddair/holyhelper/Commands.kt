package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.ShellUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


object Commands {

    var updateChecked = false

    var notifyIfNoWinPartition = true

    fun isWindowsMounted(): Boolean {
        //val winPartition = Files.getWinPartition(context) ?: return false

        //return ShellUtils.fastCmd("mount | grep $winPartition").isNotEmpty()
        //return Cmd.execute("mount | grep $winPartition").isNotEmpty()
        return Cmd.execute("grep -B1 -A5 \"${State.winPartition}\" /proc/mounts | tail -10").isNotEmpty()
    }


    fun backupBootImage(context: Context, windows: Boolean = false) {
        val startTime = System.currentTimeMillis()

        if (windows && !State.isWindowsMounted) mountWindows(context, false)
        val bootImage = if (windows) Paths.bootImage1 else Paths.bootImage

        if (State.bootPartition == null) return

        //ShellUtils.fastCmd("su -c dd bs=8m if=${bootPartition} of=${bootImage}")
        Cmd.execute("su -c dd bs=8m if=${State.bootPartition} of=${bootImage}")
        MainActivity.updateMountText(context)

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", "Backup boot image: $elapsedTime")
    }

    fun bootInWindows(context: Context, reboot: Boolean = false) {
        if (!Files.checkFile(Paths.uefiImg)) return

        if (!Files.checkFile(Paths.bootImage)) backupBootImage(context)
        if (!Files.checkFile(Paths.bootImage1)) backupBootImage(context, true)

        if (State.bootPartition == null) return

        ShellUtils.fastCmd("su -c dd if=\"${Paths.uefiImg}\" of=${State.bootPartition} bs=8M")
        if (reboot) ShellUtils.fastCmd("su -c reboot")
    }

    private fun tryMount(mountPath: String) {
        //val winPartition = Files.getWinPartition(context) ?: return

        val command = "su -mm -c 'cd ${Paths.data} && ./mount.ntfs ${State.winPartition} $mountPath'"
        //val command = "su -mm -c 'cd ${Paths.data} && ./mount.ntfs -o big_writes,noatime,norecovery $winPartition $mountPath'"
        //ShellUtils.fastCmd(command)
        val result = Cmd.execute(command)
        State.isWindowsMounted = result.isEmpty()
    }


    fun mountWindows(context: Context, unmountIfMounted: Boolean = true): Boolean {
        val startTime = System.currentTimeMillis()

/*        State.setFailed(true)

        CoroutineScope(Dispatchers.Main).launch {
            Info.winUnableToMount(context)
        }

        return false*/

        val mountDir = Files.getMountDir()

        //if (isWindowsMounted(context)) {
        if (State.isWindowsMounted) {
            if (unmountIfMounted) {
                //ShellUtils.fastCmd("su -mm -c umount $mountDir")
                Cmd.execute("su -mm -c umount $mountDir")

                CoroutineScope(Dispatchers.Main).launch {
                    Files.remove(mountDir)
                }
                State.isWindowsMounted = false
                MainActivity.updateMountText(context)

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime

                Log.d("INFO", "unmounting: $elapsedTime")
                return true
            }
            State.isWindowsMounted = true
            return true
        }

        Files.createFolder(mountDir)
        tryMount(mountDir)

        //var mounted = isWindowsMounted(context)

        if (!State.isWindowsMounted && mountDir == Paths.winPath) {
            Preferences.putBoolean(Preferences.Preference.SETTINGS, Preferences.Key.MOUNTTOMNT, true)
            tryMount(mountDir)
            if (!State.isWindowsMounted) {
                Preferences.putBoolean(Preferences.Preference.SETTINGS, Preferences.Key.MOUNTTOMNT, false)
            }
        }

        if (!State.isWindowsMounted) {
            State.setFailed(true)
            Info.winUnableToMount(context)
            State.isWindowsMounted = false
            return false
        }

        State.isWindowsMounted = true
        MainActivity.updateMountText(context)

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", "mounting: $elapsedTime")

        return true
    }

    fun checkUpdate(context: ComponentActivity) {
        if (Preferences.getBoolean(
                Preferences.Preference.SETTINGS,
                Preferences.Key.DISABLEUPDATES,
                false
            )
        ) return
        if (updateChecked) return
        Download.getRemoteFileContent(
            context,
            "https://github.com/VendDair/HolyHelper/releases/download/files/version"
        ) { content ->
            val version = content.replace("\n", "")
            if (version != Paths.version) {
                Info.notifyAboutUpdate(context, version)
                updateChecked = true
            }
        }
    }

    @SuppressLint("SdCardPath", "StringFormatInvalid")
    suspend fun dbkp(context: ComponentActivity) = coroutineScope {
        Files.setupDbkpFiles(context)

        val dbkp = Download.download(
            context,
            "https://github.com/n00b69/woa-op7/releases/download/DBKP/dbkp",
            "dbkp"
        )
        if (dbkp == null) {
            State.setFailed(true)
            return@coroutineScope
        }

        Files.moveFile(dbkp, Paths.data)
        Files.setPerms(Paths.dbkpAsset, "777")

        val (url, fileName) = Device.getDbkpDownloadInfo()


        val dbkpDir = "/sdcard/dbkp"

        // Execute commands
        val path = Download.download(context, url, fileName[0])
        if (path == null) {
            State.setFailed(true)
            return@coroutineScope
        }
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
        } else {
            ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_a bs=16M")
            ShellUtils.fastCmd("dd if=/sdcard/patched-boot.img of=/dev/block/by-name/boot_b bs=16M")
        }
    }
}