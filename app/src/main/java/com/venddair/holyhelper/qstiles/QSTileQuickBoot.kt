package com.venddair.holyhelper.qstiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QSTileQuickBoot : TileService() {
    var clicks = 0

    override fun onStartListening() {
        updateTileState()
    }

    override fun onClick() {
        val confirmationRequired = Preferences.QSCONFIRMATION.get()

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
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile
        tile.state = if (Files.checkFile(Strings.uefiImg)) Tile.STATE_INACTIVE else Tile.STATE_UNAVAILABLE

        val lockedScreenRequired = Preferences.REQUIREUNLOCKED.get()

        if (Device.isLocked) {
            if (lockedScreenRequired) {
                tile.state = Tile.STATE_UNAVAILABLE
                updateLabel("UNLOCK")
            }
            else {
                tile.state = Tile.STATE_INACTIVE
                updateLabel("Quickboot")
            }
        }

        tile.updateTile()
    }

    private fun updateLabel(newLabel: String) {
        val tile = qsTile
        tile.label = newLabel
        tile.updateTile()
    }
}