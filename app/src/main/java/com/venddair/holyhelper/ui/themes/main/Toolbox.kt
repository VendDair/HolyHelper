package com.venddair.holyhelper.ui.themes.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.venddair.holyhelper.R
import com.venddair.holyhelper.activities.sdp
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.MainActivityFunctions
import com.venddair.holyhelper.utils.State
import com.intuit.sdp.R.dimen.*
import com.intuit.ssp.R.dimen.*

@SuppressLint("StringFormatInvalid")
@Composable
fun Toolbox() {

    val context = LocalContext.current as ComponentActivity

    Box(
        modifier = Modifier
            .background(State.Colors.background)
            .fillMaxSize()
            .blur(State.blurAmount)
    ) {
        Column {
            TopBar(context.getString(R.string.toolbox_title))

            val configuration = LocalConfiguration.current

            val modifier = Modifier
                .height(sdp(_90sdp))

            val isUefiPresent by State.viewModel.isUefiFilePresent.collectAsState()

            //val buttonConfigs = buildList {
            val buttonConfigs = remember(isUefiPresent) {
                buildList {
                    add(Configs.sta(modifier))
                    add(ButtonConfig(
                        image = R.drawable.ic_sensor,
                        tintImage = true,
                        modifier = modifier,
                        title = context.getString(R.string.software_title),
                        subtitle = context.getString(R.string.software_subtitle),
                        onClick = { MainActivityFunctions.arm_software(context) }
                    ))
                    add(ButtonConfig(
                        image = R.drawable.ic_uefi,
                        modifier = modifier,
                        disabled = !isUefiPresent,
                        title = if (isUefiPresent) context.getString(R.string.flash_uefi_title) else context.getString(
                            R.string.uefi_not_found
                        ),
                        subtitle = if (isUefiPresent) context.getString(R.string.flash_uefi_subtitle) else context.getString(
                            R.string.uefi_not_found_subtitle,
                            Device.get()
                        ),
                        onClick = { if (isUefiPresent) MainActivityFunctions.flash_uefi(context) }
                    ))
                    if (State.deviceConfig.isDumpModem) {
                        add(ButtonConfig(
                            image = R.drawable.ic_modem,
                            modifier = modifier,
                            title = context.getString(R.string.dump_modem_title),
                            subtitle = context.getString(R.string.dump_modem_subtitle),
                            onClick = { MainActivityFunctions.dump_modem(context) }
                        ))
                    }
                    add(ButtonConfig(
                        image = R.drawable.atlasos,
                        modifier = modifier,
                        title = context.getString(R.string.atlasos_title),
                        subtitle = context.getString(R.string.atlasos_subtitle),
                        onClick = { MainActivityFunctions.atlasos(context) }
                    ))
                    if (State.deviceConfig.isDbkp) {
                        add(ButtonConfig(
                            image = R.drawable.ic_uefi,
                            modifier = modifier,
                            title = context.getString(R.string.dbkp_title),
                            subtitle = context.getString(R.string.dbkp_subtitle),
                            onClick = { MainActivityFunctions.dbkp(context) }
                        ))
                    }
                    add(Configs.usbHost(modifier))
                    add(ButtonConfig(
                        image = R.drawable.cd,
                        modifier = modifier,
                        title = context.getString(R.string.rotation_title),
                        subtitle = context.getString(R.string.rotation_subtitle),
                        onClick = { MainActivityFunctions.rotation(context) }
                    ))
                    add(ButtonConfig(
                        image = R.drawable.ic_sensor,
                        modifier = modifier,
                        title = context.getString(R.string.tablet_title),
                        subtitle = context.getString(R.string.tablet_subtitle),
                        onClick = { MainActivityFunctions.optimizedTaskbar(context) }
                    ))
                    add(ButtonConfig(
                        image = R.drawable.folder,
                        modifier = modifier,
                        imageScale = 0.8f,
                        title = context.getString(R.string.setup_title),
                        subtitle = context.getString(R.string.setup_subtitle),
                        onClick = { MainActivityFunctions.frameworks(context) }
                    ))
                    add(ButtonConfig(
                        image = R.drawable.edge,
                        modifier = modifier,
                        title = context.getString(R.string.defender_title),
                        subtitle = context.getString(R.string.defender_subtitle),
                        onClick = { MainActivityFunctions.edge(context) }
                    ))
                }
            }

            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    ToolboxButtons(buttonConfigs)
                }

                Configuration.ORIENTATION_LANDSCAPE -> {
                    ToolboxButtonsLandscape(buttonConfigs)
                }
            }
        }
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun ToolboxButtonsLandscape(buttonConfigs: List<ButtonConfig>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(sdp(_8sdp)),
        verticalArrangement = Arrangement.spacedBy(sdp(_10sdp)),
        horizontalArrangement = Arrangement.spacedBy(sdp(_10sdp))
    ) {
        buttonConfigs.forEach { config ->
            item {
                Button(
                    config = ButtonConfig(
                        modifier = config.modifier,
                        image = config.image,
                        tintImage = config.tintImage,
                        imageScale = config.imageScale,
                        disabled = config.disabled,
                        title = config.title,
                        subtitle = config.subtitle,
                        onClick = config.onClick,
                    )
                )
            }
        }
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun ToolboxButtons(buttonConfigs: List<ButtonConfig>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(sdp(_8sdp))
    ) {
        buttonConfigs.forEach { config ->
            Button(
                config = ButtonConfig(
                    modifier = config.modifier.padding(buttonPadding),
                    image = config.image,
                    tintImage = config.tintImage,
                    imageScale = config.imageScale,
                    disabled = config.disabled,
                    title = config.title,
                    subtitle = config.subtitle,
                    onClick = config.onClick,
                )
            )
        }
    }
}