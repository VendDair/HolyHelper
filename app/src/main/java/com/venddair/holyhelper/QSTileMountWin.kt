package com.venddair.holyhelper

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService


class QSTileMountWin : TileService() {
    override fun onTileAdded() {
        updateTileState()
    }

    override fun onStartListening() {
        updateTileState()
    }

    override fun onClick() {
        Commands.mountWindows(this)
        updateTileState()
    }

    private fun updateTileState() {
        val tile = qsTile
        tile.state = if (Commands.isWindowsMounted(this)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }
}