package com.venddair.holyhelper

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QSTileQuickBoot : TileService() {
    override fun onStartListening() {
        updateTileState()
        val tile = qsTile
        if (!Files.checkFile(Paths.uefiImg)) tile.state = 0
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