package com.venddair.holyhelper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.venddair.holyhelper.Files.createFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ToolboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.toolbox)

        // Set up the back pressed callback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })

        val staButton = findViewById<Button>(R.id.staButton)
        val scriptButton = findViewById<Button>(R.id.scriptButton)
        val armButton = findViewById<Button>(R.id.armButton)
        val atlasosButton = findViewById<Button>(R.id.atlasosButton)
        val dbkpButton = findViewById<Button>(R.id.dbkpButton)


        if (!Device.isDbkpSupported()) {
            dbkpButton.visibility = View.GONE
            findViewById<Space>(R.id.space).visibility = View.GONE
            if (Device.isLandscape(this)) {
                findViewById<LinearLayout>(R.id.asd).visibility = View.GONE
            }
        }

        staButton.setOnClickListener {
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
        scriptButton.setOnClickListener {
            startActivity(Intent(this, ScriptToolboxActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        armButton.setOnClickListener {
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

        atlasosButton.setOnClickListener {
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