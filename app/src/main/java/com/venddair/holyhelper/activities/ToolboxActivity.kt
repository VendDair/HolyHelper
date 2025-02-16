package com.venddair.holyhelper.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.venddair.holyhelper.utils.Download
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.R
import com.venddair.holyhelper.UniversalDialog
import com.venddair.holyhelper.utils.Files.createFolder
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Files.createWinFolder
import com.venddair.holyhelper.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ToolboxActivity : ComponentActivity() {
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.toolbox)

        findViewById<TextView>(R.id.topBarText).text = getString(R.string.toolbox_title)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })

        val dbkpButton = findViewById<LinearLayout>(R.id.dbkpButton)
        val flashUefiButton = findViewById<LinearLayout>(R.id.flashUefi)
        val dumpModemButton = findViewById<LinearLayout>(R.id.dumpModem)

        if (!Files.checkFile(Strings.uefiImg)) {
            val title = findViewById<TextView>(R.id.flashUefiTitle)
            val subtitle = findViewById<TextView>(R.id.flashUefiSubtitle)
            flashUefiButton.isEnabled = false
            title.text = getString(R.string.uefi_not_found)
            subtitle.text = getString(R.string.uefi_not_found_subtitle, Device.get())
        }


        if (!State.deviceConfig.isDbkp) {
            dbkpButton.visibility = View.GONE
            if (!Device.isLandscape(this)) {
                findViewById<Space>(R.id.space).visibility = View.GONE
            } else {
                findViewById<Space>(R.id.space1).visibility = View.GONE
            }
        }

        if (!State.deviceConfig.isDbkp && !State.deviceConfig.isDumpModem && Device.isLandscape(this)) {
            findViewById<LinearLayout>(R.id.uhh).visibility = View.GONE
            findViewById<Space>(R.id.space).visibility = View.GONE
        }


        if (!State.deviceConfig.isDumpModem) {
            dumpModemButton.visibility = View.GONE
            if (!Device.isLandscape(this)) {
                findViewById<Space>(R.id.modem_space).visibility = View.GONE
            } else {
                findViewById<Space>(R.id.space1).visibility = View.GONE
            }
        }

        findViewById<LinearLayout>(R.id.staButton).setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.sta_question),
                image = R.drawable.adrod,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.adrod) {
                            Files.copyStaFiles(this)
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        findViewById<LinearLayout>(R.id.armButton).setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.software_question),
                image = R.drawable.ic_sensor,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.ic_sensor) {
                            Files.copyArmSoftwareLinks(this)
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        findViewById<LinearLayout>(R.id.atlasosButton).setOnClickListener {
            UniversalDialog.showDialog(
                this,
                title = getString(R.string.atlasos_question),
                image = R.drawable.atlasos,
                dismissible = false,
                buttons = listOf(
                    Pair("atlasos") {
                        Info.pleaseWaitProgress(this, R.string.done, R.drawable.atlasos, 2, {
                            downloadAtlasOS()
                        })
                    },
                    Pair("revios") {
                        Info.pleaseWaitProgress(this, R.string.done, R.drawable.atlasos, 2, {
                            downloadReviOS()

                        })
                    },
                    Pair(getString(R.string.dismiss)) { UniversalDialog.dialog.dismiss() },
                )
            )
        }

        dbkpButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.dbkp_question, Device.getDbkpDeviceName()),
                image = R.drawable.ic_uefi,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(
                            this,
                            getString(R.string.dbkp, Device.getDbkpButton(this)),
                            R.drawable.ic_uefi,
                            {
                                Commands.dbkp(this)
                            }) {
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        findViewById<LinearLayout>(R.id.usbhostmode).setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.usbhost_question),
                image = R.drawable.folder,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.folder) {
                            createWinFolder(this, Strings.win.folders.toolbox)
                            Files.copyFileToWin(
                                this,
                                Strings.assets.USBHostMode,
                                Strings.win.USBHostMode
                            )
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        findViewById<LinearLayout>(R.id.rotation).setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.rotation_question),
                image = R.drawable.cd,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.cd) {
                            createFolder(Strings.win.folders.rotation)
                            Files.copyFileToWin(
                                this,
                                Strings.assets.display,
                                Strings.win.display
                            )
                            Files.copyFileToWin(
                                this,
                                Strings.assets.RotationShortcut,
                                Strings.win.RotationShortcut
                            )
                            Files.copyFileToWin(
                                this,
                                Strings.assets.RotationShortcutReverseLandscape,
                                Strings.win.RotationShortcutReverseLandscape
                            )

                            Files.copyFileToWin(
                                this,
                                Strings.assets.RotationShortcutReverseLandscape,
                                Strings.RotationShortcutReverseLandscape
                            )
                            Files.copyFileToWin(
                                this,
                                Strings.assets.RotationShortcut,
                                Strings.RotationShortcut
                            )
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )

        }

        findViewById<LinearLayout>(R.id.frameworkInstallers).setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.setup_question),
                image = R.drawable.folder,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWaitProgress(this, R.string.done, R.drawable.folder, 19, {
                            createWinFolder(this, Strings.win.folders.toolbox)
                            createWinFolder(this, Strings.win.folders.frameworks)
                            Download.downloadFrameworks(this)
                        }) {
                            Files.copyFileToWin(
                                this,
                                Strings.assets.installBat,
                                Strings.win.installBat
                            )

                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )

        }

        findViewById<LinearLayout>(R.id.edgeremover).setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.defender_question),
                image = R.drawable.edge,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWaitProgress(this, R.string.done, R.drawable.edge, 1, {
                            createWinFolder(this, Strings.win.folders.toolbox)
                            Download.downloadDefenderRemover(this)
                        }, {
                            Files.copyFileToWin(this, Strings.assets.edgeremover, Strings.win.edgeremover)

                        })
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        flashUefiButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.flash_uefi_question),
                image = R.drawable.ic_uefi,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.ic_uefi) {
                            Commands.bootInWindows(this)
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        dumpModemButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.dump_modem_question),
                image = R.drawable.ic_modem,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.ic_modem) {
                            Commands.dumpModem(this)
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }
    }

    private suspend fun downloadAtlasOS() = coroutineScope {
        runOnUiThread {
            createWinFolder(this@ToolboxActivity, Strings.win.folders.toolbox)
        }
        val download1 = async(Dispatchers.IO) {
            val path = Download.download(
                this@ToolboxActivity,
                "https://github.com/n00b69/modified-playbooks/releases/download/AtlasOS/AtlasPlaybook.apbx",
                "AtlasPlaybook.apbx"
            )
                ?: return@async
            runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(this@ToolboxActivity, path, Strings.win.atlasPlaybook)
            }
        }

        val download2 = async(Dispatchers.IO) {
            val path = Download.download(
                this@ToolboxActivity,
                "https://download.ameliorated.io/AME%20Wizard%20Beta.zip",
                "AMEWizardBeta.zip"
            )
                ?: return@async
            runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(this@ToolboxActivity, path, Strings.win.ameWizard)
            }
        }

        awaitAll(download1, download2)
    }

    private suspend fun downloadReviOS() = coroutineScope {
        runOnUiThread {
            createWinFolder(this@ToolboxActivity, Strings.win.folders.toolbox)
        }

        val download1 = async(Dispatchers.IO) {
            val path = Download.download(
                this@ToolboxActivity,
                "https://github.com/n00b69/modified-playbooks/releases/download/ReviOS/ReviPlaybook.apbx",
                "ReviPlaybook.apbx"
            )
                ?: return@async
            runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(this@ToolboxActivity, path, Strings.win.reviPlaybook)
            }
        }

        val download2 = async(Dispatchers.IO) {
            val path = Download.download(
                this@ToolboxActivity,
                "https://download.ameliorated.io/AME%20Wizard%20Beta.zip",
                "AMEWizardBeta.zip"
            )
                ?: return@async
            runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(this@ToolboxActivity, path, Strings.win.ameWizard)
            }
        }

        awaitAll(download1, download2)
    }
}