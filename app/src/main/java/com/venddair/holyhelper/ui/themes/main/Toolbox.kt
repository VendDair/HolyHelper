package com.venddair.holyhelper.ui.themes.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.MainActivityFunctions
import com.venddair.holyhelper.utils.State

@SuppressLint("StringFormatInvalid")
@Composable
fun Toolbox() {

    val context = LocalContext.current as ComponentActivity

    Box(
        modifier = Modifier
            .background(Color(0xFF202020))
            .fillMaxSize()
            .blur(State.blurAmount)
    ) {
        Column {
            TopBar(context.getString(R.string.toolbox_title))

            val configuration = LocalConfiguration.current

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    ToolboxButtons()
                }

                Configuration.ORIENTATION_LANDSCAPE -> {
                    ToolboxButtonsLandscape()
                }
            }


        }
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun ToolboxButtonsLandscape() {
    val context = LocalContext.current as ComponentActivity


    val isUefiPresent by State.viewModel.isUefiFilePresent.observeAsState(false)
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(
                10.dp,
                dimensionResource(com.intuit.sdp.R.dimen._10sdp),
                10.dp,
                10.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val modifier = Modifier
            //.padding(buttonPadding)
            .weight(1f)
            .height(dimensionResource(com.intuit.sdp.R.dimen._90sdp))
        val rowArrangement = Arrangement.spacedBy(10.dp)
        Row(
            horizontalArrangement = rowArrangement
        ) {
            Button(
                image = R.drawable.adrod,
                modifier = modifier,
                title = context.getString(R.string.sta_title),
                subtitle = context.getString(R.string.sta_subtitle),
                onClick = { MainActivityFunctions.sta_creator(context) }
            )
            Button(
                image = R.drawable.ic_sensor,
                modifier = modifier,
                title = context.getString(R.string.software_title),
                subtitle = context.getString(R.string.software_subtitle),
                onClick = { MainActivityFunctions.arm_software(context) }
            )
        }
        Row(
            horizontalArrangement = rowArrangement
        ) {

            Button(
                image = R.drawable.ic_uefi,
                modifier = modifier,
                disabled = !isUefiPresent,
                title = if (isUefiPresent) context.getString(R.string.flash_uefi_title) else context.getString(
                    R.string.uefi_not_found),
                subtitle = if (isUefiPresent) context.getString(R.string.flash_uefi_subtitle) else context.getString(
                    R.string.uefi_not_found_subtitle, Device.get()),
                onClick = { MainActivityFunctions.flash_uefi(context) }
            )
            Button(
                image = R.drawable.atlasos,
                modifier = modifier,
                title = context.getString(R.string.atlasos_title),
                subtitle = context.getString(R.string.atlasos_subtitle),
                onClick = { MainActivityFunctions.atlasos(context) }
            )
        }
        Row(
            horizontalArrangement = rowArrangement
        ) {

            Button(
                image = R.drawable.folder,
                modifier = modifier,
                imageScale = .8f,
                title = context.getString(R.string.usbhost_title),
                subtitle = context.getString(R.string.usbhost_subtitle),
                onClick = { MainActivityFunctions.usb_host_mode(context) }
            )
            Button(
                image = R.drawable.cd,
                modifier = modifier,
                title = context.getString(R.string.rotation_title),
                subtitle = context.getString(R.string.rotation_subtitle),
                onClick = { MainActivityFunctions.rotation(context) }
            )
        }
        Row(
            horizontalArrangement = rowArrangement
        ) {

            Button(
                image = R.drawable.folder,
                modifier = modifier,
                imageScale = .8f,
                title = context.getString(R.string.setup_title),
                subtitle = context.getString(R.string.setup_subtitle),
                onClick = { MainActivityFunctions.frameworks(context) }
            )
            Button(
                image = R.drawable.edge,
                modifier = modifier,
                title = context.getString(R.string.defender_title),
                subtitle = context.getString(R.string.defender_subtitle),
                onClick = { MainActivityFunctions.edge(context) }
            )
        }
        Row(
            horizontalArrangement = rowArrangement
        ) {

            if (State.deviceConfig.isDumpModem)
                Button(
                    image = R.drawable.ic_modem,
                    modifier = modifier,
                    title = context.getString(R.string.dump_modem_title),
                    subtitle = context.getString(R.string.dump_modem_subtitle),
                    onClick = { MainActivityFunctions.dump_modem(context) }
                )
            if (State.deviceConfig.isDbkp)
                Button(
                    image = R.drawable.ic_uefi,
                    modifier = modifier,
                    title = context.getString(R.string.dbkp_title),
                    subtitle = context.getString(R.string.dbkp_subtitle),
                    onClick = { MainActivityFunctions.dbkp(context) }
                )
        }
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun ToolboxButtons() {
    val context = LocalContext.current as ComponentActivity

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(
                10.dp,
                dimensionResource(com.intuit.sdp.R.dimen._10sdp),
                10.dp,
                10.dp
            )
    ) {
        val modifier = Modifier
            .padding(buttonPadding)
            .height(dimensionResource(com.intuit.sdp.R.dimen._90sdp))

        val isUefiPresent by State.viewModel.isUefiFilePresent.observeAsState(false)

        Button(
            image = R.drawable.adrod,
            modifier = modifier,
            title = context.getString(R.string.sta_title),
            subtitle = context.getString(R.string.sta_subtitle),
            onClick = { MainActivityFunctions.sta_creator(context) }
        )
        Button(
            image = R.drawable.ic_sensor,
            modifier = modifier,
            title = context.getString(R.string.software_title),
            subtitle = context.getString(R.string.software_subtitle),
            onClick = { MainActivityFunctions.arm_software(context) }
        )
        Button(
            image = R.drawable.ic_uefi,
            modifier = modifier,
            disabled = !isUefiPresent,
            title = if (isUefiPresent) context.getString(R.string.flash_uefi_title) else context.getString(
                R.string.uefi_not_found),
            subtitle = if (isUefiPresent) context.getString(R.string.flash_uefi_subtitle) else context.getString(
                R.string.uefi_not_found_subtitle, Device.get()),
            onClick = { MainActivityFunctions.flash_uefi(context) }
        )
        if (State.deviceConfig.isDumpModem)
            Button(
                image = R.drawable.ic_modem,
                modifier = modifier,
                title = context.getString(R.string.dump_modem_title),
                subtitle = context.getString(R.string.dump_modem_subtitle),
                onClick = { MainActivityFunctions.dump_modem(context) }
            )
        Button(
            image = R.drawable.atlasos,
            modifier = modifier,
            title = context.getString(R.string.atlasos_title),
            subtitle = context.getString(R.string.atlasos_subtitle),
            onClick = { MainActivityFunctions.atlasos(context) }
        )
        if (State.deviceConfig.isDbkp)
            Button(
                image = R.drawable.ic_uefi,
                modifier = modifier,
                title = context.getString(R.string.dbkp_title),
                subtitle = context.getString(R.string.dbkp_subtitle),
                onClick = { MainActivityFunctions.dbkp(context) }
            )
        Button(
            image = R.drawable.folder,
            modifier = modifier,
            imageScale = .8f,
            title = context.getString(R.string.usbhost_title),
            subtitle = context.getString(R.string.usbhost_subtitle),
            onClick = { MainActivityFunctions.usb_host_mode(context) }
        )
        Button(
            image = R.drawable.cd,
            modifier = modifier,
            title = context.getString(R.string.rotation_title),
            subtitle = context.getString(R.string.rotation_subtitle),
            onClick = { MainActivityFunctions.rotation(context) }
        )
        Button(
            image = R.drawable.folder,
            modifier = modifier,
            imageScale = .8f,
            title = context.getString(R.string.setup_title),
            subtitle = context.getString(R.string.setup_subtitle),
            onClick = { MainActivityFunctions.frameworks(context) }
        )
        Button(
            image = R.drawable.edge,
            modifier = modifier,
            title = context.getString(R.string.defender_title),
            subtitle = context.getString(R.string.defender_subtitle),
            onClick = { MainActivityFunctions.edge(context) }
        )
    }
}