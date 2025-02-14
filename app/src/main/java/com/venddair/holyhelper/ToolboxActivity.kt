package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.venddair.holyhelper.Files.createFolder
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
        if (!Files.checkFile(Paths.uefiImg)) {
            val title = findViewById<TextView>(R.id.flashUefiTitle)
            val subtitle = findViewById<TextView>(R.id.flashUefiSubtitle)
            flashUefiButton.isEnabled = false
            title.text = getString(R.string.uefi_not_found)
            subtitle.text = getString(R.string.uefi_not_found_subtitle, Device.get())
        }


        if (!Device.isDbkpSupported()) {
            dbkpButton.visibility = View.GONE
            if (!Device.isLandscape(this)) {
                findViewById<Space>(R.id.space).visibility = View.GONE
            } else {
                findViewById<Space>(R.id.space1).visibility = View.GONE
                findViewById<LinearLayout>(R.id.uhh).visibility = View.GONE
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
                            Files.createFolder(Paths.toolbox)
                            Files.copyFileToWin(
                                this,
                                Paths.USBHostModeAsset,
                                "Toolbox/usbhostmode.exe"
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
                            createFolder(Paths.rotation)
                            Files.copyFileToWin(this, Paths.displayAsset, "Toolbox/Rotation/display.exe")
                            Files.copyFileToWin(
                                this,
                                Paths.RotationShortcutAsset,
                                "Toolbox/RotationShortcut.lnk"
                            )
                            Files.copyFileToWin(this, Paths.RotationShortcutReverseLandscapeAsset, "Toolbox/RotationShortcutReverseLandscape.lnk")

                            Files.copyFileToWin(this, Paths.RotationShortcutReverseLandscapeAsset, Paths.RotationShortcutReverseLandscape)
                            Files.copyFileToWin(this, Paths.RotationShortcutAsset, Paths.RotationShortcut)
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
                            Files.createFolder(Paths.toolbox)
                            Files.createFolder(Paths.frameworks)
                            Download.downloadFrameworks(this)
                        }) {
                            Files.copyFileToWin(
                                this,
                                Paths.installAsset,
                                "Toolbox/Frameworks/install.bat"
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
                            createFolder(Paths.toolbox)
                            Download.downloadDefenderRemover(this)
                        }, {
                            Files.copyFileToWin(this, Paths.edgeremover, "Toolbox/RemoveEdge.bat")

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
                        Info.pleaseWait(this, R.string.done, R.drawable.edge) {
                            Commands.bootInWindows(this)
                        }
                    },
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }
    }

    private suspend fun downloadAtlasOS() = coroutineScope {
        createFolder(Paths.toolbox)
        val download1 = async(Dispatchers.IO) {
            val path = Download.download(
                this@ToolboxActivity,
                "https://github.com/n00b69/modified-playbooks/releases/download/AtlasOS/AtlasPlaybook.apbx",
                "AtlasPlaybook.apbx"
            )
                ?: return@async
            runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(this@ToolboxActivity, path, "Toolbox/AtlasPlaybook.apbx")
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
                Files.moveFileToWin(this@ToolboxActivity, path, "Toolbox/AMEWizardBeta.zip")
            }
        }

        awaitAll(download1, download2)
    }

    private suspend fun downloadReviOS() = coroutineScope {
        createFolder(Paths.toolbox)

        val download1 = async(Dispatchers.IO) {
            val path = Download.download(
                this@ToolboxActivity,
                "https://github.com/n00b69/modified-playbooks/releases/download/ReviOS/ReviPlaybook.apbx",
                "ReviPlaybook.apbx"
            )
                ?: return@async
            runOnUiThread {
                UniversalDialog.increaseProgress(1)
                Files.moveFileToWin(this@ToolboxActivity, path, "Toolbox/ReviPlaybook.apbx")
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
                Files.moveFileToWin(this@ToolboxActivity, path, "Toolbox/AMEWizardBeta.zip")
            }
        }

        awaitAll(download1, download2)
    }
}