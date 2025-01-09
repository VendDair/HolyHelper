package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.os.Environment

@SuppressLint("SdCardPath")
object Paths {
    const val uefiFolder = "/sdcard/UEFI"
    const val uefiImg = "/sdcard/UEFI/uefi.img"

    const val internalStorage = "/sdcard"

    val downloads: String get() = "/sdcard/${Environment.DIRECTORY_DOWNLOADS}"

    const val bootPartition = "/dev/block/by-name/boot"
    const val bootImage = "/sdcard/boot.img"

    const val winPath = "/sdcard/Windows"
    const val winPath1 = "/mnt/sdcard/Windows"

    val sta: String get() = Files.getMountDir() + "/sta"
    val staBin: String get() = Files.getMountDir() + "/sta/sta.exe"
    val staLink: String get() = Files.getMountDir() + "/Users/Public/Desktop/Android.lnk"
    const val staLinkAsset = "/data/local/tmp/holyhelper/Android.lnk"
    const val staAsset = "/data/local/tmp/holyhelper/sta.exe"

    const val sddAsset = "/data/local/tmp/holyhelper/sdd.exe"
    val sdd: String get() = Files.getMountDir() + "/sta/sdd.exe"
    const val sddConfigAsset = "/data/local/tmp/holyhelper/sdd.conf"
    val sddConfig: String get() = Files.getMountDir() + "/sta/sdd.conf"

    const val autoFlasherAsset = "/data/local/tmp/holyhelper/boot_img_auto-flasher_V1.0.exe"
    val autoFlasher: String get() = Files.getMountDir() + "/sta/boot_img_auto-flasher_V1.0.exe"

    const val data = "/data/local/tmp/holyhelper"

    const val mountNtfs = "/data/local/tmp/holyhelper/mount.ntfs"

    const val ARMRepoLinkAsset = "/data/local/tmp/holyhelper/ARMRepo.url"
    const val ARMSoftwareLinkAsset = "/data/local/tmp/holyhelper/ARMSoftware.url"
    const val TestedSoftwareLinkAsset = "/data/local/tmp/holyhelper/TestedSoftware.url"
    const val WorksOnWoaLinkAsset = "/data/local/tmp/holyhelper/WorksOnWoa.url"

    val ARMRepoLink get() = Files.getMountDir() + "/Toolbox/ARMRepo.url"
    val ARMSoftwareLink get() = Files.getMountDir() + "/Toolbox/ARMSoftware.url"
    val TestedSoftwareLink get() = Files.getMountDir() + "/Toolbox/TestedSoftware.url"
    val WorksOnWoaLink get() = Files.getMountDir() + "/Toolbox/WorksOnWoa.url"

    val dbkp8150CfgAsset = "/data/local/tmp/holyhelper/dbkp8150.cfg"
    val dbkpHotdogBinAsset = "/data/local/tmp/holyhelper/dbkp.hotdog.bin"
    val dbkpCepheusBinAsset = "/data/local/tmp/holyhelper/dbkp.cepheus.bin"
    val dbkpNabuBinAsset = "/data/local/tmp/holyhelper/dbkp.nabu.bin"
    val dbkpAsset = "/data/local/tmp/holyhelper/dbkp"

    const val version = "1.0"


}