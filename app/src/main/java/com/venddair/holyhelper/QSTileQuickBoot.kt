package com.venddair.holyhelper

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class QSTileQuickBoot : TileService() {
    override fun onTileAdded() {
        updateTileState()
    }

    override fun onStartListening() {
        updateTileState()
    }

    override fun onClick() {
        Commands.bootInWindows(this, true)
        updateTileState(true)
    }

    private fun updateTileState(clicked: Boolean = false) {
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE
        if (!Files.checkFile(Paths.uefiImg) && clicked) tile.label = "OPEN THE APP"
        else tile.label = "Quickboot"
        tile.updateTile()
    }
}