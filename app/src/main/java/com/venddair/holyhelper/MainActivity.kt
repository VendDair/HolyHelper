package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.main)

        ToastUtil.init(this)
        Files.init(this)
        UniversalDialog.init(this)
        Preferences.init(this)

        Files.createFolder(Files.paths["uefiFolder"]!!)

        val quickbootButton = findViewById<LinearLayout>(R.id.quickbootButton)
        val backupButton = findViewById<LinearLayout>(R.id.backupButton)
        val deviceImageView = findViewById<ImageView>(R.id.device)
        val mountButton = findViewById<LinearLayout>(R.id.mountButton)
        val codeNameText = findViewById<TextView>(R.id.codeName)
        val settingsButton = findViewById<ImageView>(R.id.settingsButton)

        deviceImageView.setImageDrawable(Files.getResourceFromDevice())
        codeNameText.text = "Device: ${Commands.getDevice()}"

        settingsButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }

        quickbootButton.setOnClickListener {
            UniversalDialog.showDialog(
                title = "Boot in Windows?",
                text = "It flashes uefi.img to boot partition",
                buttons = listOf(
                    Pair("YES") {
                        Commands.bootInWindows()
                    },
                    Pair("NO") {}
                )
            )
        }
        backupButton.setOnClickListener {
            UniversalDialog.showDialog(
                title = "Backup current boot partition?",
                text = "Backups current boot in /sdcard/boot.img or in win folder when its mounted",
                image = R.drawable.cd,
                buttons = listOf(
                    Pair("YES") {
                        Commands.backupBootImage()
                    },
                    Pair("NO") {}
                )
            )
        }
        mountButton.setOnClickListener {
            UniversalDialog.showDialog(
                title = "Mount Windows?",
                text = "Mounts Windows in /sdcard/Windows",
                image = R.drawable.folder,
                buttons = listOf(
                    Pair("YES") {
                        Commands.mountWindows()
                    },
                    Pair("NO") {}
                )
            )
        }
    }
}