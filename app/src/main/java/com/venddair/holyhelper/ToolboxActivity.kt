package com.venddair.holyhelper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

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
                        if (!Commands.isWindowsMounted()) {
                            Info.winNotMounted(this) { mounted ->
                                if (!mounted) return@winNotMounted

                                Files.createFolder(Paths.sta)
                                Files.copyFile(Paths.staAsset, Paths.staBin)
                                Files.copyFile(Paths.staLinkAsset, Paths.staLink)
                            }
                        }
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
                            Commands.execute("su -c dd bs=8M if=/dev/block/by-name/modemst1 of=$(find ${Files.getWinPartition()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs1 bs=4M")
                            Commands.execute("su -c dd bs=8M if=/dev/block/by-name/modemst2 of=$(find ${Files.getWinPartition()}/Windows/System32/DriverStore/FileRepository -name qcremotefs8150.inf_arm64_*)/bootmodem_fs2 bs=4M")
                        }
                    },
                    Pair("NO") {}
                )
            )

        }

        armButton.setOnClickListener {  }

        atlasosButton.setOnClickListener {  }

        dbkpButton.setOnClickListener {  }


    }
}