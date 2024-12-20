package com.venddair.holyhelper

import android.annotation.SuppressLint

@SuppressLint("SdCardPath")
object Paths {
    val uefiFolder = "/sdcard/UEFI"
    val uefiImg = "/sdcard/UEFI/uefi.img"
    val internalStorage = "/sdcard"
    val bootPartition = "/dev/block/by-name/boot"
    val bootImage = "/sdcard/boot.img"
    val winPath = "/sdcard/Windows"
    val winPath1 = "/mnt/sdcard/Windows"
    val sta = "/sdcard/Windows/sta"
    val staBin = "/sdcard/Windows/sta/sta.exe"
    val staLink = "/sdcard/Windows/Users/Public/Desktop/Switch to Android.lnk"
    val staLinkAsset = "/data/local/tmp/holyhelper/Switch to Android.lnk"
    val staAsset = "/data/local/tmp/holyhelper/sta/sta.exe"
    val data = "/data/local/tmp/holyhelper"
    val mountNtfs = "/data/local/tmp/holyhelper/mount.ntfs"
}