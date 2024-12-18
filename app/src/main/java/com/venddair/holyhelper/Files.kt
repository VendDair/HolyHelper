package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object Files {
    @SuppressLint("SdCardPath")

    private lateinit var appContext: Context



    @SuppressLint("SdCardPath")
    val paths = mapOf(
        "uefiFolder" to "/sdcard/UEFI",
        "uefi" to "/sdcard/UEFI/uefi.img",
        "main" to "/sdcard",
        "bootBlock" to "/dev/block/by-name/boot",
        "bootImage" to "/sdcard/boot.img",
        "mount" to "/sdcard/Windows",
        "mount1" to "/mnt/Windows",
        "data" to "/data/local/tmp/holyhelper",
        "mount.ntfs" to "/data/local/tmp/holyhelper/mount.ntfs"
    )

    fun init(context: Context) {
        appContext = context

        createFolder(paths["mount"]!!)
        createFolder(paths["data"]!!)

        copyAsset("mount.ntfs","+x")
        copyAsset("libntfs-3g.so", "777")
        copyAsset("libfuse-lite.so", "777")
    }

    fun createFolder(path: String, alert: Boolean = false) {
        if (checkFolder(path)) return
        if (alert) ToastUtil.showToast("Folder $path was created")
        Commands.execute("su -c mkdir $path")
    }

    fun copyFile(path: String, newPath: String) {
        Commands.execute("su -c cp $path $newPath")
    }

    fun copyAsset(name: String, perms: String? = null, alert: Boolean = false) {

        val outputFilePath = paths["data"]!! + "/$name"

        //if (checkFile(outputFilePath)) return

        try {
            val tempFile = File(appContext.cacheDir, name)
            val inputStream: InputStream = appContext.assets.open(name)
            val outputStream = FileOutputStream(tempFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            Commands.execute("su -c cp ${tempFile.absolutePath} $outputFilePath")

            if (perms != null) Commands.execute("su -c chmod $perms $outputFilePath")

            if (alert) ToastUtil.showToast("Asset $name was copied to path ${paths["data"]}")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error copying file: ${e.message}")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            println("Process was interrupted: ${e.message}")
        }
    }

    fun checkFolder(path: String): Boolean {
        val folder = File(path)
        return folder.exists() && folder.isDirectory
    }
    fun checkFile(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.isFile
    }

    fun getWinPartition(): String {
        return Commands.execute("su -c realpath /dev/block/by-name/win")
    }

    fun getResource(id: Int): Drawable {
        return appContext.getDrawable(id)!!
    }

    fun getResourceFromDevice(): Drawable {
        return when (Commands.getDevice()) {
            "a52sxq" -> getResource(R.drawable.a52sxq)
            "judyln", "judyp", "judypn", "joan", "andromeda", "guacamoleb", "hotdogb", "OnePlus7T", "OnePlus7",
            "sagit", "t860", "t865" -> getResource(R.drawable.unknown)
            "alphalm", "alphaplus", "alpha_lao_com", "alphalm_lao_com", "alphaplus_lao_com" -> getResource(R.drawable.alphaplus)
            "betaplus", "betalm", "beta_lao_com", "betaplus_lao_com", "betalm_lao_com" -> getResource(R.drawable.betalm)
            "flashlmdd", "flash_lao_com", "flashlm", "flashlmdd_lao_com" -> getResource(R.drawable.flashlmdd)
            "mh2lm", "mh2plus", "mh2plus_lao_com", "mh2lm_lao_com", "mh2lm5g", "mh2lm5g_lao_com" -> getResource(R.drawable.mh2lm)
            "beryllium" -> getResource(R.drawable.beryllium)
            "bhima", "vayu" -> getResource(R.drawable.vayu)
            "cepheus" -> getResource(R.drawable.cepheus)
            "cheeseburger" -> getResource(R.drawable.cheeseburger)
            "dumpling" -> getResource(R.drawable.dumpling)
            "chiron" -> getResource(R.drawable.chiron)
            "curtana2", "curtana_india", "curtana_cn", "curtanacn", "durandal", "durandal_india",
            "excalibur", "excalibur2", "excalibur_india", "gram", "joyeuse", "miatoll" -> getResource(R.drawable.miatoll)
            "dipper" -> getResource(R.drawable.dipper)
            "equuleus" -> getResource(R.drawable.equuleus)
            "G973F", "SM-G973F", "beyond1lte", "beyond1qlte", "G973U", "G973U1", "SM-G973U", "SM-G973U1", "G9730",
            "SM-G9730", "G973N", "SM-G973N", "G973X", "SM-G973X", "G973C", "SM-G973C", "SCV41", "SM-SC41", "beyond1" -> getResource(R.drawable.beyond1)
            "lisa" -> getResource(R.drawable.lisa)
            "nabu" -> getResource(R.drawable.nabu)
            "perseus" -> getResource(R.drawable.perseus)
            "pipa" -> getResource(R.drawable.pipa)
            "polaris" -> getResource(R.drawable.polaris)
            "Pong", "pong" -> getResource(R.drawable.pong)
            "raphael", "raphaelin", "raphaels", "davinci" -> getResource(R.drawable.raphael)
            "surya" -> getResource(R.drawable.vayu)
            "OnePlus6", "fajita" -> getResource(R.drawable.fajita)
            "OnePlus6T", "enchilada" -> getResource(R.drawable.enchilada)
            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "OnePlus7TPro5G", "OnePlus7TProNR" -> getResource(R.drawable.hotdog)
            "guacamole", "OnePlus7Pro", "OnePlus7Pro4G", "hotdogg", "OP7ProNRSpr" -> getResource(R.drawable.guacamole)
            "q2q" -> getResource(R.drawable.q2q)
            "winnerx", "winner" -> getResource(R.drawable.winner)
            "xpeng" -> getResource(R.drawable.xpeng)
            "venus" -> getResource(R.drawable.venus)
            "alioth" -> getResource(R.drawable.alioth)
            "marble" -> getResource(R.drawable.marble)
            "RMX2061" -> getResource(R.drawable.rmx2061)
            "RMX2170" -> getResource(R.drawable.rmx2170)
            "cmi" -> getResource(R.drawable.cmi)
            "houji" -> getResource(R.drawable.houji)
            "meizu20pro" -> getResource(R.drawable.meizu20pro)
            "husky" -> getResource(R.drawable.husky)
            "redfin" -> getResource(R.drawable.redfin)
            "e3q" -> getResource(R.drawable.e3q)
            "dm3q", "dm3" -> getResource(R.drawable.dm3q)
            else -> getResource(R.drawable.unknown)
        }
    }
}