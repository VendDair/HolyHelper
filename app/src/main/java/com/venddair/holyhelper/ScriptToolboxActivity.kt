package com.venddair.holyhelper

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class ScriptToolboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.scripttoolbox)

        val usbhostmodeButton = findViewById<LinearLayout>(R.id.usbhostmode)
        val rotationButton = findViewById<LinearLayout>(R.id.rotation)
        val frameworkInstallersButton = findViewById<LinearLayout>(R.id.frameworkInstallers)
        val edgeRemover = findViewById<LinearLayout>(R.id.edgeremover)

        usbhostmodeButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.usbhost_question),
                image = R.drawable.folder,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Files.createFolder(Paths.toolbox)
                        Files.copyFileToWin(this, Paths.USBHostModeAsset, "Toolbox/usbhostmode.exe")
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }

        rotationButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.rotation_question),
                image = R.drawable.cd,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Files.createFolder(Paths.toolbox)
                        Files.copyFileToWin(this, Paths.displayAsset, "Toolbox/display.exe")
                        Files.copyFileToWin(this, Paths.RotationShortcutAsset, "Toolbox/RotationShortcut.lnk")
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }

        frameworkInstallersButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.setup_question),
                image = R.drawable.folder,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Files.createFolder(Paths.toolbox)
                        Files.createFolder(Paths.frameworks)
                        Download.downloadFrameworks(this)
                        Files.copyFileToWin(this, Paths.installAsset, "Toolbox/Frameworks/install.bat")
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }

        edgeRemover.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.defender_question),
                image = R.drawable.edge,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Files.createFolder(Paths.toolbox)
                        Files.copyFileToWin(this, Paths.edgeremover, "Toolbox/RemoveEdge.bat")
                        Download.downloadDefenderRemover(this)
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }
    }
}