package com.venddair.holyhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import java.lang.ref.WeakReference

class ScriptToolboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.scripttoolbox)

        // Set up the back pressed callback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })

        val usbhostmodeButton = findViewById<Button>(R.id.usbhostmode)
        val rotationButton = findViewById<Button>(R.id.rotation)
        val frameworkInstallersButton = findViewById<Button>(R.id.frameworkInstallers)
        val edgeRemover = findViewById<Button>(R.id.edgeremover)

        usbhostmodeButton.setOnClickListener {
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
                    Pair(getString(R.string.no)) {}
                )
            )

        }

        rotationButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.rotation_question),
                image = R.drawable.cd,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWait(this, R.string.done, R.drawable.cd) {
                            Files.createFolder(Paths.toolbox)
                            Files.copyFileToWin(this, Paths.displayAsset, "Toolbox/display.exe")
                            Files.copyFileToWin(
                                this,
                                Paths.RotationShortcutAsset,
                                "Toolbox/RotationShortcut.lnk"
                            )
                        }
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }

        frameworkInstallersButton.setOnClickListener {
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
                    Pair(getString(R.string.no)) {}
                )
            )

        }

        edgeRemover.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.defender_question),
                image = R.drawable.edge,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Info.pleaseWaitProgress(this, R.string.done, R.drawable.edge, 1, {
                            Files.createFolder(Paths.toolbox)
                            Download.downloadDefenderRemover(this)
                        }, {
                            Files.copyFileToWin(this, Paths.edgeremover, "Toolbox/RemoveEdge.bat")

                        })
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }
    }
}