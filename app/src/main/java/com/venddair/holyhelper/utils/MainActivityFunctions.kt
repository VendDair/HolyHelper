package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.UniversalDialog
import com.venddair.holyhelper.utils.Files.createWinFolder

object MainActivityFunctions {

    fun backupBoot() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.backup_boot_question),
            image = R.drawable.cd,
            dismissible = false,
            buttons = listOf(
                Pair("windows") {
                    Info.pleaseWait(
                        R.string.backuped,
                        R.drawable.cd
                    ) {
                        Commands.backupBootImage(
                            context,
                            true
                        )
                    }
                },
                Pair("android") {
                    Info.pleaseWait(
                        R.string.backuped,
                        R.drawable.cd
                    ) {
                        Commands.backupBootImage(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }
    
    fun mountWindows() {
        //State.isWindowsMounted = isWindowsMounted()
        //updateMountText(context)
        UniversalDialog.showDialog(context,
            title = if (!ViewModel.isWindowsMounted.value) context.getString(
                R.string.mount_question,
                Files.getMountDir()
            ) else context.getString(R.string.unmount_question),
            image = R.drawable.folder,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    val text =
                        if (!ViewModel.isWindowsMounted.value) R.string.mounted else R.string.unmounted
                    Info.pleaseWait(text, R.drawable.folder) {
                        Commands.mountWindows(context)
                        ViewModel.updateMountText()
                    }

                },
                Pair(context.getString(R.string.no)) {
                    UniversalDialog.dialog.dismiss()
                }
            )
        )
    }

    fun quickboot() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.quickboot_question),
            imageScale = 2f,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Commands.bootInWindows(context, true)
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun sta_creator() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.sta_question),
            image = R.drawable.adrod,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.adrod) {
                        Files.copyStaFiles()
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun arm_software() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.software_question),
            image = R.drawable.ic_sensor,
            tintColor = appColors.text,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.ic_sensor) {
                        Files.copyArmSoftwareLinks()
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun flash_uefi() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.flash_uefi_question),
            image = R.drawable.ic_uefi,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.ic_uefi) {
                        Commands.bootInWindows(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun dump_modem() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.dump_modem_question),
            image = R.drawable.ic_modem,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.ic_modem) {
                        Commands.dumpModem(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    @SuppressLint("StringFormatInvalid")
    fun devcfg() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.devcfg_question, Device.getDbkpDeviceName()),
            image = R.drawable.ic_uefi,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWaitProgress(
                        context.getString(R.string.devcfg, Device.getDbkpButton(context)),
                        R.drawable.ic_uefi, 2, {
                        Commands.devcfg()
                    })
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun atlasos() {
        UniversalDialog.showDialog(
            context,
            title = context.getString(R.string.atlasos_question),
            image = R.drawable.atlasos,
            dismissible = false,
            buttons = listOf(
                Pair("atlasos") {
                    Info.pleaseWaitProgress(R.string.done, R.drawable.atlasos, 2, {
                        Download.downloadAtlasOS(context)
                    })
                },
                Pair("revios") {
                    Info.pleaseWaitProgress(R.string.done, R.drawable.atlasos, 2, {
                        Download.downloadReviOS(context)

                    })
                },
                Pair(context.getString(R.string.dismiss)) { UniversalDialog.dialog.dismiss() },
            )
        )
    }

    @SuppressLint("StringFormatInvalid")
    fun dbkp() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.dbkp_question, Device.getDbkpDeviceName()),
            image = R.drawable.ic_uefi,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(
                        context.getString(R.string.dbkp, Device.getDbkpButton(context)),
                        R.drawable.ic_uefi,
                        {
                            Commands.dbkp(context)
                        }) {
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun usb_host_mode() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.usbhost_question),
            image = R.drawable.folder,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.folder) {
                        createWinFolder(Strings.win.folders.toolbox)
                        Files.copyFileToWin(
                            Strings.assets.USBHostMode,
                            Strings.win.USBHostMode
                        )
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun rotation() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.rotation_question),
            image = R.drawable.cd,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.cd) {
                        Files.copyRotationFiles()
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun optimizedTaskbar() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.tablet_question),
            image = R.drawable.ic_sensor,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(R.string.done, R.drawable.cd) {
                        createWinFolder(Strings.win.folders.toolbox)
                        Files.copyFileToWin(Strings.assets.optimizedTaskbar, Strings.win.optimizedTaskbar)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun frameworks() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.setup_question),
            image = R.drawable.folder,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWaitProgress(R.string.done, R.drawable.folder, 19, {
                        createWinFolder(Strings.win.folders.toolbox)
                        createWinFolder(Strings.win.folders.frameworks)
                        Download.downloadFrameworks(context)
                    }) {
                        Files.copyFileToWin(
                            Strings.assets.installBat,
                            Strings.win.installBat
                        )

                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun edge() {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.defender_question),
            image = R.drawable.edge,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWaitProgress(R.string.done, R.drawable.edge, 1, {
                        createWinFolder(Strings.win.folders.toolbox)
                        Download.downloadDefenderRemover(context)
                    }, {
                        Files.copyFileToWin(Strings.assets.edgeremover, Strings.win.edgeremover)

                    })
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

}