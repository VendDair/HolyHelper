package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val switchMountToMnt = findViewById<Switch>(R.id.MountToMnt)

        val settings = Preferences.get("settings")
        val settingsEditor = settings.edit()

        switchMountToMnt.isChecked = settings.getBoolean("mountToMnt", false)

        switchMountToMnt.setOnCheckedChangeListener { _, isChecked ->
            settingsEditor.putBoolean("mountToMnt", isChecked)
            settingsEditor.apply()
        }
    }
}
