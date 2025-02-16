package com.venddair.holyhelper

import android.annotation.SuppressLint
import com.topjohnwu.superuser.ShellUtils

@SuppressLint("SdCardPath")
object Strings {

    const val version = "1.0.3"


    object assets {
        lateinit var data: String

        val staLink: String get() = "${data}/Android.lnk"
        val sta: String get() = "${data}/sta.exe"

        val sdd: String get() = "${data}/sdd.exe"
        val sddConfig: String get() = "${data}/sdd.conf"

        val autoFlasher: String get() = "${data}/boot_img_auto-flasher_V1.0.exe"

        val mountNtfs: String get() = "${data}/mount.ntfs"

        val ARMRepoLink: String get() = "${data}/ARMRepo.url"
        val ARMSoftwareLink: String get() = "${data}/ARMSoftware.url"
        val TestedSoftwareLink: String get() = "${data}/TestedSoftware.url"
        val WorksOnWoaLink: String get() = "${data}/WorksOnWoa.url"

        val installBat: String get() = "${data}/install.bat"

        val dbkp8150Cfg: String get() = "${data}/dbkp8150.cfg"
        val dbkpHotdogBin: String get() = "${data}/dbkp.hotdog.bin"
        val dbkpCepheusBin: String get() = "${data}/dbkp.cepheus.bin"
        val dbkpNabuBin: String get() = "${data}/dbkp.nabu.bin"
        val dbkp: String get() = "${data}/dbkp"

        val USBHostMode: String get() = "${data}/usbhostmode.exe"

        val display: String get() = "${data}/display.exe"
        val RotationShortcut: String get() = "${data}/RotationShortcut.lnk"
        val RotationShortcutReverseLandscape: String get() = "${data}/RotationShortcutReverseLandscape.lnk"

        val edgeremover: String get() = "${data}/RemoveEdge.bat"
    }

    object win {
        object folders {
            val toolbox = "Toolbox"

            val sta = "sta"

            val rotation = "Toolbox/Rotation"
            val frameworks = "Toolbox/Frameworks"
        }

        val staBin = "sta/sta.exe"
        val staLink = "Users/Public/Desktop/Android.lnk"

        val sddBin = "sta/sdd.exe"
        val sddConfig = "sta/sdd.conf"

        val autoFlasher = "sta/boot_img_auto-flasher_V1.0.exe"

        val USBHostMode = "Toolbox/usbhostmode.exe"

        val display  = "Toolbox/Rotation/display.exe"
        val RotationShortcut = "Toolbox/RotationShortcut.lnk"
        val RotationShortcutReverseLandscape = "Toolbox/RotationShortcutReverseLandscape.lnk"

        val installBat = "Toolbox/Frameworks/install.bat"

        val ARMRepoLink = "Toolbox/ARMRepo.url"
        val ARMSoftwareLink = "Toolbox/ARMSoftware.url"
        val TestedSoftwareLink = "Toolbox/TestedSoftware.url"
        val WorksOnWoaLink = "Toolbox/WorksOnWoa.url"

        val edgeremover = "Toolbox/RemoveEdge.bat"

        val atlasPlaybook = "Toolbox/AtlasPlaybook.apbx"
        val reviPlaybook = "Toolbox/ReviPlaybook.apbx"
        val ameWizard = "Toolbox/AMEWizardBeta.zip"

        val defenderRemover = "Toolbox/DefenderRemover.exe"
    }

    object folders {
        const val uefi = "/sdcard/UEFI"
        const val win = "/mnt/sdcard/Windows"
        const val win1 = "/mnt/Windows"
    }


    val uefiImg: String get() = ShellUtils.fastCmd("su -c find /sdcard/UEFI/ -type f | grep .img | head -n 1")

    const val bootImage = "/sdcard/boot.img"
    const val bootImage1 = "/sdcard/Windows/boot.img"


















    val RotationShortcut = "Users/Public/Desktop/RotationShortcut.lnk"
    val RotationShortcutReverseLandscape = "Users/Public/Desktop/RotationShortcutReverseLandscape.lnk"



}