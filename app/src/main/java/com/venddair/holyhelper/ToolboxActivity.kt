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
        val dumpModemButton = findViewById<LinearLayout>(R.id.dumpModemButton)
        val armButton = findViewById<LinearLayout>(R.id.armButton)
        val atlasosButton = findViewById<LinearLayout>(R.id.atlasosButton)
        val dbkpButton = findViewById<LinearLayout>(R.id.dbkpButton)

        when (Commands.getDevice()) {
            "cepheus", "guacamole", "OnePlus7Pro", "OnePlus7Pro4G", "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "nabu" -> {
                dbkpButton.visibility = View.VISIBLE
            }
            else -> dbkpButton.visibility = View.GONE
        }

        staButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Sta creator",
                text = "Copies sta files into win partition",
                image = R.drawable.adrod,
                buttons = listOf(
                    Pair("YES") {
                        if (!Commands.isWindowsMounted())
                            Info.winNotMounted(this) { mounted ->
                                if (!mounted) return@winNotMounted
                                Files.copyStaFiles()
                            }
                        else Files.copyStaFiles()
                    },
                    Pair("NO") {}
                )
            )

        }
        scriptButton.setOnClickListener { startActivity(Intent(this, ScriptToolboxActivity::class.java)) }

        dumpModemButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Dump modem",
                text = "Dump modem to Windows for LTE on SIM1.\nDump modem1st and modem2st to Windows partition?\nRequired before every Windows boot",
                image = R.drawable.ic_modem,
                buttons = listOf(
                    Pair("YES") {
                        if (!Commands.isWindowsMounted()) Info.winNotMounted(this) { mounted ->
                            if (!mounted) return@winNotMounted
                            Commands.dumpModem()
                        }
                        else Commands.dumpModem()
                    },
                    Pair("NO") {}
                )
            )

        }

        armButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Arm Software",
                text = "Copy browser shortcuts to C:\\Toolbox?",
                image = R.drawable.ic_modem,
                buttons = listOf(
                    Pair("YES") {
                        if (!Commands.isWindowsMounted()) Info.winNotMounted(this) { mounted ->
                            if (!mounted) return@winNotMounted
                            Files.copyArmSoftwareLinks()
                        }
                        else Files.copyArmSoftwareLinks()
                    },
                    Pair("NO") {}
                )
            )
        }

        atlasosButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Copy AtlasOS/ReviOS files?",
                text = "Copies AtlasOS/ReviOS files in C:\\Toolbox",
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
                    Pair("no") {},
                )
            )

        }

        dbkpButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Dualboot kernel Patcher",
                text = "Patches and flashes your current kernel",
                image = R.drawable.ic_uefi,
                buttons = listOf(
                    Pair("yes") {
                        Commands.dbkp(this)
                    },
                    Pair("no") {}
                )
                )
        }


    }

    private fun downloadAtlasOS() {
        createFolder(getMountDir() + "/Toolbox")
        Download.download(this,"https://github.com/n00b69/modified-playbooks/releases/download/AtlasOS/AtlasPlaybook.apbx", "AtlasPlaybook.apbx") { name ->
            Files.moveFile("${Paths.downloads}/$name", Paths.internalStorage+"/AtlasPlaybook.apbx")
            Files.copyFileToWin(this, Paths.internalStorage+"/AtlasPlaybook.apbx", "Toolbox/AtlasPlaybook.apbx", false)
        }

        Download.download(this, "https://download.ameliorated.io/AME%20Wizard%20Beta.zip", "AMEWizardBeta.zip") { name ->
            Files.moveFile("${Paths.downloads}/$name", Paths.internalStorage+"/AMEWizardBeta.zip")
            Files.copyFileToWin(this, Paths.internalStorage+"/AMEWizardBeta.zip", "Toolbox/AMEWizardBeta.zip", false)
        }
    }

    private fun downloadReviOS() {
        createFolder(getMountDir() + "/Toolbox")
        Download.download(this,"https://github.com/n00b69/modified-playbooks/releases/download/ReviOS/ReviPlaybook.apbx", "ReviPlaybook.apbx") { name ->
            Files.moveFile("${Paths.downloads}/$name", Paths.internalStorage+"/ReviPlaybook.apbx")
            Files.copyFileToWin(this, Paths.internalStorage+"/ReviPlaybook.apbx", "Toolbox/ReviPlaybook.apbx", false)
        }

        Download.download(this, "https://download.ameliorated.io/AME%20Wizard%20Beta.zip", "AMEWizardBeta.zip") { name ->
            Files.moveFile("${Paths.downloads}/$name", Paths.internalStorage+"/AMEWizardBeta.zip")
            Files.copyFileToWin(this, Paths.internalStorage+"/AMEWizardBeta.zip", "Toolbox/AMEWizardBeta.zip", false)
        }
    }
}