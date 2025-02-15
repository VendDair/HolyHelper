package com.venddair.holyhelper.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences

class SettingsActivity : ComponentActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        // Set up the back pressed callback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })


        val switchMountToMnt = findViewById<Switch>(R.id.MountToMnt)
        val disableUpdates = findViewById<Switch>(R.id.DisableUpdates)
        val autoMount = findViewById<Switch>(R.id.AutoMount)
        val selectUefi = findViewById<LinearLayout>(R.id.selectUefiImage)

        switchMountToMnt.isChecked = Preferences.getBoolean(
            Preferences.Preference.SETTINGS,
            Preferences.Key.MOUNTTOMNT,
            false
        )
        disableUpdates.isChecked = Preferences.getBoolean(
            Preferences.Preference.SETTINGS,
            Preferences.Key.DISABLEUPDATES,
            false
        )
        autoMount.isChecked = Preferences.getBoolean(
            Preferences.Preference.SETTINGS,
            Preferences.Key.AUTOMOUNT,
            false
        )

        switchMountToMnt.setOnCheckedChangeListener { _, isChecked ->
            Preferences.putBoolean(
                Preferences.Preference.SETTINGS,
                Preferences.Key.MOUNTTOMNT,
                isChecked
            )
        }

        disableUpdates.setOnCheckedChangeListener { _, isChecked ->
            Preferences.putBoolean(
                Preferences.Preference.SETTINGS,
                Preferences.Key.DISABLEUPDATES,
                isChecked
            )
        }

        autoMount.setOnCheckedChangeListener { _, isChecked ->
            Preferences.putBoolean(
                Preferences.Preference.SETTINGS,
                Preferences.Key.AUTOMOUNT,
                isChecked
            )
        }

        selectUefi.setOnClickListener {
            Files.selectUefiImage()
        }
    }
}
