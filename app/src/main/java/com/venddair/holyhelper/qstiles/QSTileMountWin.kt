package com.venddair.holyhelper.qstiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.ViewModel
import com.venddair.holyhelper.utils.failed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        if (failed) {
            val tile = qsTile

            failed = false
            tile.label = "FAILED"
            tile.updateTile()

            CoroutineScope(Dispatchers.Main).launch {
                delay(400) // Non-blocking delay
                tile.label = "Mount win"
                tile.updateTile()
            }
            return
        }

    }

    private fun updateTileState() {
        val tile = qsTile
        tile.state = if (ViewModel.isWindowsMounted.value) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE

        tile.updateTile()
    }
}