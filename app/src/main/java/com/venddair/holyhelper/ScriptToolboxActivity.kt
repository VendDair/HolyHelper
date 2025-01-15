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
                title = "Usb Host Mode Toggle",
                text = "Scripts that toggles Usb Host Mode$question",
                image = R.drawable.folder,
                buttons = listOf(
                    Pair("YES") {
                        Files.copyFileToWin(this, Paths.USBHostModeAsset, "Toolbox/usbhostmode.exe")
                    },
                    Pair("NO") {}
                )
            )

        }

        rotationButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Rotation Toggle",
                text = "Scripts that rotates the device orientation$question",
                image = R.drawable.cd,
                buttons = listOf(
                    Pair("YES") {
                        Files.copyFileToWin(this, Paths.displayAsset, "Toolbox/display.exe")
                        Files.copyFileToWin(this, Paths.RotationShortcutAsset, "Toolbox/RotationShortcut.lnk")
                    },
                    Pair("NO") {}
                )
            )

        }

        frameworkInstallersButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Framework Installers",
                text = "Add installers for several frameworks\n(C++ Redistributables, DirectX, OpenGL, OpenAL, PhysX and XNA framework)$question",
                image = R.drawable.folder,
                buttons = listOf(
                    Pair("YES") {
                        Download.downloadFrameworks(this)
                        Files.copyFileToWin(this, Paths.installAsset, "Toolbox/Frameworks/install.bat")
                    },
                    Pair("NO") {}
                )
            )

        }

        edgeRemover.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Edge / Defender Remover",
                text = "Scripts that removes Edge or Defender$question",
                image = R.drawable.edge,
                buttons = listOf(
                    Pair("YES") {

                    },
                    Pair("NO") {}
                )
            )

        }
    }
}