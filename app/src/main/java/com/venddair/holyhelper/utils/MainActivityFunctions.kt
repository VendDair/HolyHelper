package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.UniversalDialog
import com.venddair.holyhelper.activities.MainActivity.Companion.updateMountText
import com.venddair.holyhelper.utils.Files.createFolder
import com.venddair.holyhelper.utils.Files.createWinFolder

object MainActivityFunctions {

    fun backupBoot(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.backup_boot_question),
            image = R.drawable.cd,
            dismissible = false,
            buttons = listOf(
                Pair("windows") {
                    Info.pleaseWait(
                        context,
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
                        context,
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
    
    fun mountWindows(context: Context) {
        //State.isWindowsMounted = isWindowsMounted()
        //updateMountText(context)
        UniversalDialog.showDialog(context,
            title = if (!State.isWindowsMounted) context.getString(
                R.string.mount_question,
                Files.getMountDir()
            ) else context.getString(R.string.unmount_question),
            image = R.drawable.folder,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    val text =
                        if (!State.isWindowsMounted) R.string.mounted else R.string.unmounted
                    Info.pleaseWait(context, text, R.drawable.folder) {
                        Commands.mountWindows(context)
                        updateMountText(context)
                    }

                },
                Pair(context.getString(R.string.no)) {
                    UniversalDialog.dialog.dismiss()
                }
            )
        )
    }

    fun quickboot(context: Context) {
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

    fun sta_creator(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.sta_question),
            image = R.drawable.adrod,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(context, R.string.done, R.drawable.adrod) {
                        Files.copyStaFiles(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun arm_software(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.software_question),
            image = R.drawable.ic_sensor,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(context, R.string.done, R.drawable.ic_sensor) {
                        Files.copyArmSoftwareLinks(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun flash_uefi(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.flash_uefi_question),
            image = R.drawable.ic_uefi,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(context, R.string.done, R.drawable.ic_uefi) {
                        Commands.bootInWindows(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun dump_modem(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.dump_modem_question),
            image = R.drawable.ic_modem,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(context, R.string.done, R.drawable.ic_modem) {
                        Commands.dumpModem(context)
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun atlasos(context: ComponentActivity) {
        UniversalDialog.showDialog(
            context,
            title = context.getString(R.string.atlasos_question),
            image = R.drawable.atlasos,
            dismissible = false,
            buttons = listOf(
                Pair("atlasos") {
                    Info.pleaseWaitProgress(context, R.string.done, R.drawable.atlasos, 2, {
                        Download.downloadAtlasOS(context)
                    })
                },
                Pair("revios") {
                    Info.pleaseWaitProgress(context, R.string.done, R.drawable.atlasos, 2, {
                        Download.downloadReviOS(context)

                    })
                },
                Pair(context.getString(R.string.dismiss)) { UniversalDialog.dialog.dismiss() },
            )
        )
    }

    @SuppressLint("StringFormatInvalid")
    fun dbkp(context: ComponentActivity) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.dbkp_question, Device.getDbkpDeviceName()),
            image = R.drawable.ic_uefi,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(
                        context,
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

    fun usb_host_mode(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.usbhost_question),
            image = R.drawable.folder,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(context, R.string.done, R.drawable.folder) {
                        createWinFolder(context, Strings.win.folders.toolbox)
                        Files.copyFileToWin(
                            context,
                            Strings.assets.USBHostMode,
                            Strings.win.USBHostMode
                        )
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun rotation(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.rotation_question),
            image = R.drawable.cd,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWait(context, R.string.done, R.drawable.cd) {
                        createFolder(Strings.win.folders.rotation)
                        Files.copyFileToWin(
                            context,
                            Strings.assets.display,
                            Strings.win.display
                        )
                        Files.copyFileToWin(
                            context,
                            Strings.assets.RotationShortcut,
                            Strings.win.RotationShortcut
                        )
                        Files.copyFileToWin(
                            context,
                            Strings.assets.RotationShortcutReverseLandscape,
                            Strings.win.RotationShortcutReverseLandscape
                        )

                        Files.copyFileToWin(
                            context,
                            Strings.assets.RotationShortcutReverseLandscape,
                            Strings.win.RotationShortcutReverseLandscape
                        )
                        Files.copyFileToWin(
                            context,
                            Strings.assets.RotationShortcut,
                            Strings.win.RotationShortcut
                        )
                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun frameworks(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.setup_question),
            image = R.drawable.folder,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWaitProgress(context, R.string.done, R.drawable.folder, 19, {
                        createWinFolder(context, Strings.win.folders.toolbox)
                        createWinFolder(context, Strings.win.folders.frameworks)
                        Download.downloadFrameworks(context)
                    }) {
                        Files.copyFileToWin(
                            context,
                            Strings.assets.installBat,
                            Strings.win.installBat
                        )

                    }
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

    fun edge(context: Context) {
        UniversalDialog.showDialog(context,
            title = context.getString(R.string.defender_question),
            image = R.drawable.edge,
            dismissible = false,
            buttons = listOf(
                Pair(context.getString(R.string.yes)) {
                    Info.pleaseWaitProgress(context, R.string.done, R.drawable.edge, 1, {
                        createWinFolder(context, Strings.win.folders.toolbox)
                        Download.downloadDefenderRemover(context)
                    }, {
                        Files.copyFileToWin(context, Strings.assets.edgeremover, Strings.win.edgeremover)

                    })
                },
                Pair(context.getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
            )
        )
    }

}