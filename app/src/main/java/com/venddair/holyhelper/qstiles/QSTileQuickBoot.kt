package com.venddair.holyhelper.qstiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Commands

class QSTileQuickBoot : TileService() {
    override fun onStartListening() {
        updateTileState()
        val tile = qsTile
        if (!Files.checkFile(Strings.uefiImg)) tile.state = 0
        else tile.state = 1
        tile.updateTile()
    }

    override fun onClick() {
        Commands.bootInWindows(this, true)
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile

        tile.state = Tile.STATE_INACTIVE
        tile.label = "Quickboot"
        tile.updateTile()
    }
}