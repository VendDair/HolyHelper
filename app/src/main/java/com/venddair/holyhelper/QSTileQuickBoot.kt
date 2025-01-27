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
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE
        tile.updateTile()
    }
}