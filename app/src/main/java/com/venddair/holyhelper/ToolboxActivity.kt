package com.venddair.holyhelper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import com.venddair.holyhelper.Files.createFolder

class ToolboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.toolbox)

        val staButton = findViewById<LinearLayout>(R.id.staButton)
        val scriptButton = findViewById<LinearLayout>(R.id.scriptButton)
        val armButton = findViewById<LinearLayout>(R.id.armButton)
        val atlasosButton = findViewById<LinearLayout>(R.id.atlasosButton)
        val dbkpButton = findViewById<LinearLayout>(R.id.dbkpButton)


        if (!Device.isDbkpSupported()) dbkpButton.visibility = View.GONE

        staButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.sta_question),
                image = R.drawable.adrod,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                            if (Commands.mountWindows(this, false)) {
                            Files.copyStaFiles()
                        }
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }
        scriptButton.setOnClickListener { startActivity(Intent(this, ScriptToolboxActivity::class.java)) }

        armButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.software_question),
                image = R.drawable.ic_sensor,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        if (Commands.mountWindows(this, false)) {
                            Files.copyArmSoftwareLinks()
                        }
                    },
                    Pair(getString(R.string.no)) {}
                )
            )
        }

        atlasosButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.atlasos_question),
                image = R.drawable.atlasos,
                buttons = listOf(
                    Pair("atlasos") {
                        if (Commands.mountWindows(this, false)) {
                            downloadAtlasOS()
                        }
                    },
                    Pair("revios") {
                        if (Commands.mountWindows(this, false)) {
                            downloadReviOS()
                        }
                    },
                    Pair(getString(R.string.dismiss)) {},
                )
            )

        }

        dbkpButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.dbkp_question, Device.getDbkpDeviceName()),
                //text = "Patches and flashes your current kernel\nDON'T CLICK UNLESS YOUR DEVICE IS ${Commands.getDbkpSupportedDevice()}",
                image = R.drawable.ic_uefi,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Commands.dbkp(this)
                    },
                    Pair(getString(R.string.no)) {}
                )
                )
        }


    }

    private fun downloadAtlasOS() {
        createFolder(Paths.toolbox)
        Download.download(this,"https://github.com/n00b69/modified-playbooks/releases/download/AtlasOS/AtlasPlaybook.apbx", "AtlasPlaybook.apbx") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/AtlasPlaybook.apbx")
        }

        Download.download(this, "https://download.ameliorated.io/AME%20Wizard%20Beta.zip", "AMEWizardBeta.zip") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/AMEWizardBeta.zip")
        }
    }

    private fun downloadReviOS() {
        createFolder(Paths.toolbox)
        Download.download(this,"https://github.com/n00b69/modified-playbooks/releases/download/ReviOS/ReviPlaybook.apbx", "ReviPlaybook.apbx") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/ReviPlaybook.apbx")
        }

        Download.download(this, "https://download.ameliorated.io/AME%20Wizard%20Beta.zip", "AMEWizardBeta.zip") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/AMEWizardBeta.zip")
        }
    }
}