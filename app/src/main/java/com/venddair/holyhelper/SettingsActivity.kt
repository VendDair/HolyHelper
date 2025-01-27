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
        val disableUpdates = findViewById<Switch>(R.id.DisableUpdates)

        /*val settings = Preferences.get(Preferences.Preference.SETTINGS)
        val settingsEditor = settings.edit()*/

        /*switchMountToMnt.isChecked = settings.getBoolean("mountToMnt", false)
        disableUpdates.isChecked = settings.getBoolean("disableUpdates", false)*/
        switchMountToMnt.isChecked = Preferences.getBoolean(Preferences.Preference.SETTINGS, Preferences.Key.MOUNTTOMNT, false)
        disableUpdates.isChecked = Preferences.getBoolean(Preferences.Preference.SETTINGS, Preferences.Key.DISABLEUPDATES, false)

        switchMountToMnt.setOnCheckedChangeListener { _, isChecked ->
            Preferences.putBoolean(Preferences.Preference.SETTINGS, Preferences.Key.MOUNTTOMNT, isChecked)
            /*settingsEditor.putBoolean("mountToMnt", isChecked)
            settingsEditor.apply()*/
        }

        disableUpdates.setOnCheckedChangeListener { _, isChecked ->
            Preferences.putBoolean(Preferences.Preference.SETTINGS, Preferences.Key.DISABLEUPDATES, isChecked)
            /*settingsEditor.putBoolean("disableUpdates", isChecked)
            settingsEditor.apply()*/
        }
    }
}
