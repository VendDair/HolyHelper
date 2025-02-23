package com.venddair.holyhelper.qstiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.LockStateDetector
import com.venddair.holyhelper.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QSTileQuickBoot : TileService() {
    var clicks = 0

    override fun onStartListening() {
        updateTileState()
        val tile = qsTile
        if (!Files.checkFile(Strings.uefiImg)) tile.state = 0
        else tile.state = 1
        tile.updateTile()
    }

    override fun onClick() {
        val lockedScreenRequired = Preferences.getBoolean(Preferences.Preference.SETTINGS, Preferences.Key.REQUIREUNLOCKED, false)
        val confirmationRequired = Preferences.getBoolean(Preferences.Preference.SETTINGS, Preferences.Key.QSCONFIRMATION, false)

        if (LockStateDetector.isDeviceLocked(this) && lockedScreenRequired) {
            updateLabel("UNLOCK")
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                updateLabel("Quickboot")
            }
        }
        else {
            if (clicks != 2) clicks++
            if (confirmationRequired && clicks != 2) {
                updateLabel("SURE?")
                CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    if (clicks != 2) {
                        updateLabel("Quickboot")
                        clicks = 0
                    }
                }
            }
            else if (clicks == 2) {
                Commands.bootInWindows(this, true)
                updateLabel("Quickboot")
                clicks = 0
            }
        }
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile
        tile.state = if (Files.checkFile(Strings.uefiImg)) Tile.STATE_INACTIVE else Tile.STATE_UNAVAILABLE

        tile.updateTile()
    }

    private fun updateLabel(newLabel: String) {
        val tile = qsTile
        tile.label = newLabel
        tile.updateTile()
    }
}