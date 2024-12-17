package com.venddair.holyhelper

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)

        ToastUtil.init(this)

        Files.createFolder(Files.paths["uefiFolder"]!!)

        val quickbootButton = findViewById<LinearLayout>(R.id.quickbootButton)
        val backupButton = findViewById<LinearLayout>(R.id.backupButton)

        quickbootButton.setOnClickListener {
            ToastUtil.showToast(Commands.execute("pwd"))
        }
        backupButton.setOnClickListener {
            Commands.backupBootImage()
        }
    }
}