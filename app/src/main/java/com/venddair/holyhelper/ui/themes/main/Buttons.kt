package com.venddair.holyhelper.ui.themes.main

import android.annotation.SuppressLint
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.MainActivityFunctions
import com.venddair.holyhelper.utils.State


@Composable
fun backupBootButton(modifier: Modifier = Modifier): ButtonConfig {

    val context = LocalContext.current as ComponentActivity

    return ButtonConfig(
            modifier = modifier
                .padding(buttonPadding),
            image = R.drawable.cd,
            title = context.getString(R.string.backup_boot_title),
            subtitle = context.getString(R.string.backup_boot_subtitle),
            onClick = { MainActivityFunctions.backupBoot(context) }
        )
}

@Composable
fun mountButton(modifier: Modifier = Modifier): ButtonConfig {

    val viewModel: MainViewModel = viewModel()

    val mountText by viewModel.mountText.observeAsState("")

    val context = LocalContext.current as ComponentActivity

    return ButtonConfig(
            modifier = modifier
                .padding(buttonPadding),
            image = R.drawable.folder,
            imageScale = 0.8f,
            title = mountText,
            subtitle = context.getString(R.string.mnt_subtitle),
            onClick = { MainActivityFunctions.mountWindows(context) }
        )
}

@Composable
fun toolboxButton(modifier: Modifier = Modifier, navController: NavController): ButtonConfig {

    val context = LocalContext.current as ComponentActivity

    return ButtonConfig(
            modifier = modifier
                .padding(buttonPadding),
            image = R.drawable.toolbox,
            title = context.getString(R.string.toolbox_title),
            subtitle = context.getString(R.string.toolbox_subtitle),
            onClick = { navController.navigate("toolbox") }
        )
}

@SuppressLint("StringFormatInvalid")
@Composable
fun quickbootButton(modifier: Modifier = Modifier): ButtonConfig {

    val isUefiPresent by State.viewModel.isUefiFilePresent.observeAsState(false)

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

fun staButtonConfig(modifier: Modifier = Modifier): ButtonConfig {

    return ButtonConfig(
            image = R.drawable.adrod,
            modifier = modifier,
            title = State.context.get()!!.getString(R.string.sta_title),
            subtitle = State.context.get()!!.getString(R.string.sta_subtitle),
            onClick = { MainActivityFunctions.sta_creator(State.context.get()!!) }
        )
}

fun usbHostButtonConfig(modifier: Modifier = Modifier): ButtonConfig {

    return ButtonConfig(
            image = R.drawable.folder,
            modifier = modifier,
            imageScale = 0.8f,
            title = State.context.get()!!.getString(R.string.usbhost_title),
            subtitle = State.context.get()!!.getString(R.string.usbhost_subtitle),
            onClick = { MainActivityFunctions.usb_host_mode(State.context.get()!!) }
        )
}