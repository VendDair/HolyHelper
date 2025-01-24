package com.venddair.holyhelper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import com.venddair.holyhelper.Files.createFolder
import com.venddair.holyhelper.Files.getMountDir

class ToolboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.toolbox)

        val staButton = findViewById<LinearLayout>(R.id.staButton)
        val scriptButton = findViewById<LinearLayout>(R.id.scriptButton)
        //val dumpModemButton = findViewById<LinearLayout>(R.id.dumpModemButton)
        val armButton = findViewById<LinearLayout>(R.id.armButton)
        val atlasosButton = findViewById<LinearLayout>(R.id.atlasosButton)
        val dbkpButton = findViewById<LinearLayout>(R.id.dbkpButton)

        when (Commands.getDevice()) {
            "cepheus", "guacamole", "guacamolet", "pipa", "OnePlus7Pro", "OnePlus7Pro4G", "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "nabu" -> {
                dbkpButton.visibility = View.VISIBLE
            }
            else -> dbkpButton.visibility = View.GONE
        }

        staButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.sta_question),
                image = R.drawable.adrod,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        if (!Commands.isWindowsMounted(this))
                            Info.winNotMounted(this) { mounted ->
                                if (!mounted) return@winNotMounted
                                Files.copyStaFiles()
                            }
                        else Files.copyStaFiles()
                    },
                    Pair(getString(R.string.no)) {}
                )
            )

        }
        scriptButton.setOnClickListener { startActivity(Intent(this, ScriptToolboxActivity::class.java)) }

        /*dumpModemButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Dump modem",
                text = "Dump modem to Windows for LTE on SIM1.\nDump modem1st and modem2st to Windows partition?\nRequired before every Windows boot",
                image = R.drawable.ic_modem,
                buttons = listOf(
                    Pair("YES") {
                        if (!Commands.isWindowsMounted(this)) Info.winNotMounted(this) { mounted ->
                            if (!mounted) return@winNotMounted
                            Commands.dumpModem()
                        }
                        else Commands.dumpModem()
                    },
                    Pair("NO") {}
                )
            )

        }*/

        armButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.software_question),
                image = R.drawable.ic_sensor,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        if (!Commands.isWindowsMounted(this)) Info.winNotMounted(this) { mounted ->
                            if (!mounted) return@winNotMounted
                            Files.copyArmSoftwareLinks()
                        }
                        else Files.copyArmSoftwareLinks()
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
                        Commands.askUserToMountIfNotMounted(this) {
                            downloadAtlasOS()
                        }
                    },
                    Pair("revios") {
                        Commands.askUserToMountIfNotMounted(this) {
                            downloadReviOS()
                        }
                    },
                    Pair(getString(R.string.dismiss)) {},
                )
            )

        }

        dbkpButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.dbkp_question, Commands.getDbkpSupportedDevice()),
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

        Download.download(this,"https://github.com/n00b69/modified-playbooks/releases/download/AtlasOS/AtlasPlaybook.apbx", "AtlasPlaybook.apbx") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/AtlasPlaybook.apbx", false)
        }

        Download.download(this, "https://download.ameliorated.io/AME%20Wizard%20Beta.zip", "AMEWizardBeta.zip") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/AMEWizardBeta.zip", false)
        }
    }

    private fun downloadReviOS() {
        createFolder(getMountDir() + "/Toolbox")
        Download.download(this,"https://github.com/n00b69/modified-playbooks/releases/download/ReviOS/ReviPlaybook.apbx", "ReviPlaybook.apbx") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/ReviPlaybook.apbx", false)
        }

        Download.download(this, "https://download.ameliorated.io/AME%20Wizard%20Beta.zip", "AMEWizardBeta.zip") { path, _ ->
            Files.copyFileToWin(this, path, "Toolbox/AMEWizardBeta.zip", false)
        }
    }
}