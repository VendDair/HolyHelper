package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.os.Environment
import com.topjohnwu.superuser.ShellUtils

@SuppressLint("SdCardPath")
object Paths {

    const val data = "/data/data/com.venddair.holyhelper/files"

    const val uefiFolder = "/sdcard/UEFI"

    //const val uefiImg = "/sdcard/UEFI/uefi.img"
    //val uefiImg: String get() = "$data/uefi.img"
    val uefiImg: String get() = ShellUtils.fastCmd("su -c find /sdcard/UEFI/ -type f | grep .img | head -n 1")

    const val internalStorage = "/sdcard"

    val downloads: String get() = "/sdcard/${Environment.DIRECTORY_DOWNLOADS}"

    //val bootPartition: String get() = "/dev/block/by-name/boot$(getprop ro.boot.slot_suffix)"
    const val bootImage = "/sdcard/boot.img"
    const val bootImage1 = "/sdcard/Windows/boot.img"

    const val winPath = "/mnt/sdcard/Windows"
    const val winPath1 = "/mnt/Windows"

    val sta: String get() = "sta"
    val staBin: String get() = "sta/sta.exe"
    val staLink: String get() = "Users/Public/Desktop/Android.lnk"
    const val staLinkAsset = "/data/local/tmp/holyhelper/Android.lnk"
    const val staAsset = "/data/local/tmp/holyhelper/sta.exe"

    const val sddAsset = "/data/local/tmp/holyhelper/sdd.exe"
    val sdd: String get() = "sta/sdd.exe"
    const val sddConfigAsset = "/data/local/tmp/holyhelper/sdd.conf"
    val sddConfig: String get() = "sta/sdd.conf"

    const val autoFlasherAsset = "/data/local/tmp/holyhelper/boot_img_auto-flasher_V1.0.exe"
    val autoFlasher: String get() = "sta/boot_img_auto-flasher_V1.0.exe"


    const val mountNtfs = "/data/local/tmp/holyhelper/mount.ntfs"

    const val ARMRepoLinkAsset = "/data/local/tmp/holyhelper/ARMRepo.url"
    const val ARMSoftwareLinkAsset = "/data/local/tmp/holyhelper/ARMSoftware.url"
    const val TestedSoftwareLinkAsset = "/data/local/tmp/holyhelper/TestedSoftware.url"
    const val WorksOnWoaLinkAsset = "/data/local/tmp/holyhelper/WorksOnWoa.url"

    val toolbox: String get() = Files.getMountDir() + "/Toolbox"
    val frameworks: String get() = Files.getMountDir() + "/Toolbox/Frameworks"

    val installAsset = "/data/local/tmp/holyhelper/install.bat"

    val ARMRepoLink get() = "Toolbox/ARMRepo.url"
    val ARMSoftwareLink get() = "Toolbox/ARMSoftware.url"
    val TestedSoftwareLink get() = "Toolbox/TestedSoftware.url"
    val WorksOnWoaLink get() = "Toolbox/WorksOnWoa.url"

    val dbkp8150CfgAsset = "/data/local/tmp/holyhelper/dbkp8150.cfg"
    val dbkpHotdogBinAsset = "/data/local/tmp/holyhelper/dbkp.hotdog.bin"
    val dbkpCepheusBinAsset = "/data/local/tmp/holyhelper/dbkp.cepheus.bin"
    val dbkpNabuBinAsset = "/data/local/tmp/holyhelper/dbkp.nabu.bin"
    val dbkpAsset = "/data/local/tmp/holyhelper/dbkp"

    val USBHostModeAsset = "/data/local/tmp/holyhelper/usbhostmode.exe"

    val displayAsset = "/data/local/tmp/holyhelper/display.exe"
    val RotationShortcutAsset = "/data/local/tmp/holyhelper/RotationShortcut.lnk"

    val edgeremover = "/data/local/tmp/holyhelper/RemoveEdge.bat"

    const val version = "1.0"


}