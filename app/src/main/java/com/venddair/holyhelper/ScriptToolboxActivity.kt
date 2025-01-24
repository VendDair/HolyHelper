package com.venddair.holyhelper

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.venddair.holyhelper.ui.theme.HolyHelperTheme

class ScriptToolboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.scripttoolbox)

        val usbhostmodeButton = findViewById<LinearLayout>(R.id.usbhostmode)
        val rotationButton = findViewById<LinearLayout>(R.id.rotation)
        val frameworkInstallersButton = findViewById<LinearLayout>(R.id.frameworkInstallers)
        val edgeRemover = findViewById<LinearLayout>(R.id.edgeremover)

        val question = "\nDo you want to copy it to windows?"

        usbhostmodeButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.usbhost_question),
                image = R.drawable.folder,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
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
                        Files.copyFileToWin(this, Paths.edgeremover, "Toolbox/RemoveEdge.bat")
                        Download.downloadDefenderRemover(this)
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }
    }
}