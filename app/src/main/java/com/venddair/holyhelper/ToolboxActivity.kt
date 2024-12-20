package com.venddair.holyhelper

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import kotlin.io.path.Path

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
            if (!Commands.isWindowsMounted()) return@setOnClickListener

            Files.createFolder(Paths.sta)
            Files.copyFile(Paths.staAsset, Paths.staBin)
            Files.copyFile(Paths.staLinkAsset, Paths.staLink)
        }


    }
}