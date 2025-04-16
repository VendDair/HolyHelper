package com.venddair.holyhelper.ui.themes.ogwoaheler2_0

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.intuit.sdp.R.dimen.*
import com.intuit.ssp.R.dimen.*
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.activities.sdp
import com.venddair.holyhelper.activities.ssp
import com.venddair.holyhelper.ui.themes.ButtonConfig
import com.venddair.holyhelper.ui.themes.Configs
import com.venddair.holyhelper.ui.themes.SettingsButtonConfig
import com.venddair.holyhelper.ui.themes.SettingsMiniButtonConfig
import com.venddair.holyhelper.ui.themes.Theme
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.utils.AppTheme
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.ViewModel
import com.venddair.holyhelper.utils.appColors
import com.venddair.holyhelper.utils.context
import com.venddair.holyhelper.utils.deviceConfig

object OGWoaHelper2_0Theme : Theme {
    override val statusBarColor: Int
        get() = appColors.surface.toArgb()
    override val navigationBarColor: Int
        get() = appColors.background.toArgb()

    @Composable
    override fun MainMenu() {
        val modifier = Modifier.height(sdp(_140sdp))
        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar(null)

            AppTheme.ScreenContainer(Modifier) {
                AppTheme.Panel()
                AppTheme.ElementsContainer(false) {
                    RowInnerElementContainer(modifier) {
                        AppTheme.Button(Configs.backupBoot(Modifier.weight(1f), 1.2f))
                        AppTheme.Button(Configs.mount(Modifier.weight(1f), 1.1f))
                    }
                    RowInnerElementContainer(modifier) {
                        AppTheme.Button(Configs.toolbox(Modifier.weight(1f), 1.5f))
                        AppTheme.Button(Configs.quickboot(Modifier.weight(1f), 2.5f))
                    }
                }
            }
        }

        
    }

    @Composable
    override fun ToolboxMenu() {
        //val modifier = Modifier.height(sdp(_90sdp))
        val heightModifier = Modifier.height(sdp(_140sdp))
        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar(context.getString(R.string.toolbox_title))

            AppTheme.ScreenContainer(Modifier.padding(vertical = 10.dp)) {
                AppTheme.ElementsContainer(true) {
                    val modifier = Modifier.weight(1f)

                    RowInnerElementContainer(heightModifier) {
                        AppTheme.Button(Configs.sta(modifier, 2f))
                        AppTheme.Button(Configs.armSoftware(modifier, 1.5f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        AppTheme.Button(Configs.flashUefi(modifier, 1.5f))
                        AppTheme.Button(Configs.atlasos(modifier, 1.5f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        AppTheme.Button(Configs.usbHost(modifier, 1.2f))
                        AppTheme.Button(Configs.rotation(modifier, 1.2f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        AppTheme.Button(Configs.tabletMode(modifier, 1.5f))
                        AppTheme.Button(Configs.frameworks(modifier, 1.2f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        AppTheme.Button(Configs.edgeRemover(modifier, 1.5f))
                        if (deviceConfig.isDumpModem)
                            AppTheme.Button(Configs.dumpModem(modifier, 2f))
                    }
                    if (deviceConfig.isDbkp)
                        AppTheme.Button(Configs.dbkp(modifier))
                }
            }
        }

        
    }

    @Composable
    override fun SettingsMenu() {
        MainTheme.SettingsMenu()
    }

    @Composable
    override fun ColorsMenu() {
        MainTheme.ColorsMenu()
    }

    @Composable
    override fun ThemesMenu() {
        MainTheme.ThemesMenu()
    }

    @Composable
    override fun OuterColumn(modifier: Modifier, content: @Composable () -> Unit) {
        MainTheme.OuterColumn(modifier) { content() }
    }

    @Composable
    override fun ScreenContainer(modifier: Modifier, content: @Composable () -> Unit) {
        MainTheme.ScreenContainer(modifier) { content() }
    }

    @Composable
    override fun ElementsContainer(
        scrollable: Boolean,
        content: @Composable() (ColumnScope.() -> Unit),
    ) {
        MainTheme.ElementsContainer(scrollable) { content() }
    }

    @Composable
    override fun TopBar(text: String?) {
        Row(
            modifier = Modifier
                .background(appColors.background)
                .fillMaxWidth()
                .padding(
                    horizontal = sdp(_18sdp),
                    vertical = sdp(_5sdp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "logo",
                    modifier = Modifier
                        .scale(2f)
                        .padding(end = sdp(_10sdp))
                        .size(sdp(_30sdp)),
                )
                if (text == null)
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Holy Helper",
                            color = appColors.text,
                            fontSize = ssp(_15ssp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = Strings.version + if (Strings.dev) " (DEV)" else "",
                            color = appColors.text,
                            fontSize = ssp(_12ssp)
                        )
                    }
                else
                    Text(
                        text = text,
                        fontSize = ssp(_16ssp),
                        color = appColors.text,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = sdp(_2sdp))
                    )
            }

            if (text == null)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sdp(_10sdp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (Strings.dev)
                        MainTheme.RefreshIcon(Modifier)
                    MainTheme.SettingsIcon(Modifier)
                }
        }
    }

    @Composable
    override fun Panel() {
        val isPortrait = Device.isPortrait

        val modifier = if (isPortrait) {
            Modifier.height(sdp(_160sdp)).fillMaxWidth()
        } else {
            Modifier.width(sdp(_250sdp)).fillMaxHeight()
        }

        if (isPortrait) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(sdp(_5sdp))
                //horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppTheme.DeviceImage(modifier = Modifier.weight(1f))
                AppTheme.InfoBox(modifier = Modifier.weight(1.4f))
            }
        } else {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(sdp(_5sdp))
            ) {
                AppTheme.DeviceImage(modifier = Modifier.weight(1f))
                AppTheme.InfoBox(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    override fun InfoBox(modifier: Modifier) {
        val deviceName by ViewModel.deviceName.collectAsState()
        val panel by ViewModel.panelType.collectAsState()
        val ram by ViewModel.totalRam.collectAsState()
        val lastBackup by ViewModel.lastBackupDate.collectAsState()
        val slot by ViewModel.slot.collectAsState()

        val horizontalPadding = sdp(_5sdp)
        val buttonPadding = PaddingValues(
            start = sdp(_6sdp),
            end = sdp(_6sdp),
            top = 0.dp,
            bottom = sdp(_8sdp)
        )

        Box(
            modifier = modifier
                .fillMaxHeight()
                .background(appColors.surface, shape = RoundedCornerShape(sdp(_10sdp)))
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .height(sdp(_30sdp))
                        .padding(top = sdp(_15sdp))
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center

                ) {
                    Text(
                        text = "Windows on ARM",
                        color = appColors.text,
                        fontWeight = FontWeight.Bold,
                        fontSize = ssp(_12ssp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight().padding(top = sdp(_5sdp))
                ) {
                    Column {
                        Column(
                            modifier = Modifier.padding(start = horizontalPadding)
                        ) {
                            listOf(deviceName, panel, ram, lastBackup, slot).forEach { info ->
                                info?.takeIf { it.isNotEmpty() && !it.contains("null") }?.let {
                                    AppTheme.PanelItem(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun RefreshIcon(modifier: Modifier) {
        MainTheme.RefreshIcon(modifier)
    }

    @Composable
    override fun SettingsIcon(modifier: Modifier) {
        MainTheme.SettingsIcon(modifier)
    }

    @Composable
    override fun DeviceImage(modifier: Modifier) {
        MainTheme.DeviceImage(modifier)
    }

    @Composable
    override fun Button(config: ButtonConfig) {
        var isHeld by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (isHeld) 0.9f else 1f,
            animationSpec = tween(70)
        )

        Box(
            modifier = config.modifier
                .fillMaxHeight()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .pointerInput(config.disabled) {
                    detectTapGestures(
                        onTap = { if (!config.disabled) config.onClick() },
                        onPress = {
                            if (config.disabled) return@detectTapGestures
                            isHeld = true
                            try {
                                awaitRelease()
                            } finally {
                                isHeld = false
                            }
                        }
                    )
                }
                .clip(RoundedCornerShape(sdp(_8sdp)))
                .background(appColors.surface),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .height(sdp(_60sdp)),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = config.title,
                        color = appColors.text,
                        fontWeight = FontWeight.Bold,
                        fontSize = ssp(_10ssp),
                        textAlign = TextAlign.Center
                    )

                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = sdp(_5sdp))
                            .scale(config.imageScale)
                            .width(sdp(_20sdp)),
                        painter = painterResource(id = config.image),
                        tint = if (config.tintImage) appColors.text else Color.Unspecified,
                        contentDescription = "button image"
                    )
                }
                HorizontalDivider()

                Text(
                    modifier = Modifier
                        .padding(start = sdp(_10sdp), top = sdp(_5sdp)),
                    text = config.subtitle,
                    color = appColors.text,
                    fontStyle = FontStyle.Italic,
                    fontSize = ssp(_8ssp)
                )
            }
            /*Row(modifier = Modifier.fillMaxSize()) {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = sdp(_5sdp))
                        .scale(config.imageScale)
                        .width(sdp(_40sdp))
                        .fillMaxHeight(),
                    painter = painterResource(id = config.image),
                    tint = if (config.tintImage) appColors.text else Color.Unspecified,
                    contentDescription = "button image"
                )
                Column(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, sdp(_10sdp), 0.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = config.title,
                        color = appColors.text,
                        fontWeight = FontWeight.Bold,
                        fontSize = ssp(_11ssp),
                    )
                    Text(
                        text = config.subtitle,
                        color = appColors.text,
                        fontStyle = FontStyle.Italic,
                        fontSize = ssp(_9ssp)
                    )
                }
            }*/
        }
    }

    @Composable
    override fun MiniButton(text: String, modifier: Modifier, onClick: () -> Unit) {
        MainTheme.MiniButton(text, modifier) { onClick() }
    }

    @Composable
    override fun SettingsItem(config: SettingsButtonConfig) {
        MainTheme.SettingsItem(config)
    }

    @Composable
    override fun SettingsButton(config: SettingsMiniButtonConfig) {
        MainTheme.SettingsButton(config)
    }

    @Composable
    override fun PanelItem(text: String) {
        MainTheme.PanelItem(text)
    }

    @Composable
    override fun ColorChanger(
        topBarText: String,
        initialColor: Color,
        colorKey: Preferences.Preference<String>,
        previewColorFactor: Float
    ) {
        MainTheme.ColorChanger(topBarText, initialColor, colorKey, previewColorFactor)
    }

    @Composable
    override fun Loading() {
        val isLoading by ViewModel.isLoading.collectAsState()
        val hadLoaded by ViewModel.hadLoaded.collectAsState()
        context.window.statusBarColor = appColors.background.toArgb()
        context.window.navigationBarColor = appColors.background.toArgb()

        if (isLoading && !hadLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appColors.background),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = "Loading...",
                    textAlign = TextAlign.Center,
                    color = appColors.text,
                    fontSize = ssp(_15ssp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RowInnerElementContainer(modifier: Modifier, content: @Composable () -> Unit) {
    val space = sdp(_8sdp)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space),
    ) { content() }
}