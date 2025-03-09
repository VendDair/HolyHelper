package com.venddair.holyhelper.ui.themes.main

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.MainActivityFunctions
import com.venddair.holyhelper.utils.State

object Configs {

    @Composable
    fun backupBoot(modifier: Modifier = Modifier): ButtonConfig {
        return ButtonConfig(
            modifier = modifier
                .padding(buttonPadding),
            image = R.drawable.cd,
            title = State.context.getString(R.string.backup_boot_title),
            subtitle = State.context.getString(R.string.backup_boot_subtitle),
            onClick = { MainActivityFunctions.backupBoot(State.context) }
        )
    }

    @Composable
    fun mount(modifier: Modifier = Modifier): ButtonConfig {
        val mountText by State.viewModel.mountText.collectAsState()

        return ButtonConfig(
            modifier = modifier
                .padding(buttonPadding),
            image = R.drawable.folder,
            imageScale = 0.8f,
            title = mountText,
            subtitle = State.context.getString(R.string.mnt_subtitle),
            onClick = { MainActivityFunctions.mountWindows(State.context) }
        )
    }

    @Composable
    fun toolbox(modifier: Modifier = Modifier): ButtonConfig {
        return ButtonConfig(
            modifier = modifier
                .padding(buttonPadding),
            image = R.drawable.toolbox,
            title = State.context.getString(R.string.toolbox_title),
            subtitle = State.context.getString(R.string.toolbox_subtitle),
            onClick = { State.navController.navigate("toolbox") }
        )
    }

    @SuppressLint("StringFormatInvalid")
    @Composable
    fun quickboot(modifier: Modifier = Modifier): ButtonConfig {
        val isUefiPresent by State.viewModel.isUefiFilePresent.collectAsState()

        val context = LocalContext.current as ComponentActivity

        return ButtonConfig(
            modifier = modifier,
            image = R.drawable.ic_launcher_foreground,
            imageScale = 2f,
            disabled = !isUefiPresent,
            title = if (isUefiPresent) context.getString(R.string.quickboot_title) else context.getString(R.string.uefi_not_found),
            subtitle = if (isUefiPresent) context.getString(R.string.quickboot_subtitle) else context.getString(R.string.uefi_not_found_subtitle, Device.get()),
            onClick = { MainActivityFunctions.quickboot(context) }
        )
    }

    fun sta(modifier: Modifier = Modifier): ButtonConfig {

        return ButtonConfig(
            image = R.drawable.adrod,
            modifier = modifier,
            title = State.context.getString(R.string.sta_title),
            subtitle = State.context.getString(R.string.sta_subtitle),
            onClick = { MainActivityFunctions.sta_creator(State.context) }
        )
    }

    fun usbHost(modifier: Modifier = Modifier): ButtonConfig {

        return ButtonConfig(
            image = R.drawable.folder,
            modifier = modifier,
            imageScale = 0.8f,
            title = State.context.getString(R.string.usbhost_title),
            subtitle = State.context.getString(R.string.usbhost_subtitle),
            onClick = { MainActivityFunctions.usb_host_mode(State.context) }
        )
    }
}
