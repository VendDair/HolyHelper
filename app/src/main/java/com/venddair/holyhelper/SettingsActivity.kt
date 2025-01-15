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
        val checkUpdates = findViewById<Switch>(R.id.CheckUpdates)

        val settings = Preferences.get("settings")
        val settingsEditor = settings.edit()

        switchMountToMnt.isChecked = settings.getBoolean("mountToMnt", false)
        checkUpdates.isChecked = settings.getBoolean("checkUpdates", true)

        switchMountToMnt.setOnCheckedChangeListener { _, isChecked ->
            settingsEditor.putBoolean("mountToMnt", isChecked)
            settingsEditor.apply()
        }

        checkUpdates.setOnCheckedChangeListener { _, isChecked ->
            settingsEditor.putBoolean("checkUpdates", isChecked)
            settingsEditor.apply()
        }
    }
}
