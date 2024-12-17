package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.widget.Toast
import java.io.File

object Files {
    @SuppressLint("SdCardPath")
    val paths = mapOf(
        "uefiFolder" to "/sdcard/UEFI",
        "uefi" to "/sdcard/UEFI/uefi.img",
        "main" to "/sdcard",
        "bootBlock" to "/dev/block/by-name/boot",
        "bootImage" to "/sdcard/boot.img",

    )

    fun createFolder(path: String, alert: Boolean = false) {
        if (checkFolder(path)) return
        if (alert) ToastUtil.showToast("Folder $path was created")
        Commands.execute("su -c mkdir $path")
    }

    fun checkFolder(path: String): Boolean {
        val folder = File(path)
        return folder.exists() && folder.isDirectory
    }

}