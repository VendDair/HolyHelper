package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import com.topjohnwu.superuser.ShellUtils

@SuppressLint("SdCardPath")
object Paths {

    lateinit var data: String

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
    val staLinkAsset: String get() = "${data}/Android.lnk"
    val staAsset: String get() = "${data}/sta.exe"

    val sddAsset: String get() = "${data}/sdd.exe"
    val sdd: String get() = "sta/sdd.exe"
    val sddConfigAsset: String get() = "${data}/sdd.conf"
    val sddConfig: String get() = "sta/sdd.conf"

    val autoFlasherAsset: String get() = "${data}/boot_img_auto-flasher_V1.0.exe"
    val autoFlasher: String get() = "sta/boot_img_auto-flasher_V1.0.exe"


    val mountNtfs: String get() = "${data}/mount.ntfs"

    val ARMRepoLinkAsset: String get() = "${data}/ARMRepo.url"
    val ARMSoftwareLinkAsset: String get() = "${data}/ARMSoftware.url"
    val TestedSoftwareLinkAsset: String get() = "${data}/TestedSoftware.url"
    val WorksOnWoaLinkAsset: String get() = "${data}/WorksOnWoa.url"

    val toolbox: String get() = Files.getMountDir() + "/Toolbox"
    val frameworks: String get() = Files.getMountDir() + "/Toolbox/Frameworks"

    val installAsset: String get() = "${data}/install.bat"

    val ARMRepoLink get() = "Toolbox/ARMRepo.url"
    val ARMSoftwareLink get() = "Toolbox/ARMSoftware.url"
    val TestedSoftwareLink get() = "Toolbox/TestedSoftware.url"
    val WorksOnWoaLink get() = "Toolbox/WorksOnWoa.url"

    val dbkp8150CfgAsset: String get() = "${data}/dbkp8150.cfg"
    val dbkpHotdogBinAsset: String get() = "${data}/dbkp.hotdog.bin"
    val dbkpCepheusBinAsset: String get() = "${data}/dbkp.cepheus.bin"
    val dbkpNabuBinAsset: String get() = "${data}/dbkp.nabu.bin"
    val dbkpAsset: String get() = "${data}/dbkp"

    val USBHostModeAsset: String get() = "${data}/usbhostmode.exe"

    val displayAsset: String get() = "${data}/display.exe"
    val RotationShortcutAsset: String get() = "${data}/RotationShortcut.lnk"

    val edgeremover: String get() = "${data}/RemoveEdge.bat"

    const val version = "1.0"


}