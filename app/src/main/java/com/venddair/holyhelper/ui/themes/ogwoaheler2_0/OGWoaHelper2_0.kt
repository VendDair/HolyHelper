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
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State

object OGWoaHelper2_0Theme : Theme {
    @Composable
    override fun MainMenu() {
        val modifier = Modifier.height(sdp(_140sdp))
        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar(null)

            State.Theme.ScreenContainer(Modifier) {
                State.Theme.Panel()
                State.Theme.ElementsContainer(false) {
                    RowInnerElementContainer(modifier) {
                        State.Theme.Button(Configs.backupBoot(Modifier.weight(1f), 1.2f))
                        State.Theme.Button(Configs.mount(Modifier.weight(1f), 1.1f))
                    }
                    RowInnerElementContainer(modifier) {
                        State.Theme.Button(Configs.toolbox(Modifier.weight(1f), 1.5f))
                        State.Theme.Button(Configs.quickboot(Modifier.weight(1f), 2.5f))
                    }
                }
            }
        }

        State.Theme.Loading()
    }

    @Composable
    override fun ToolboxMenu() {
        //val modifier = Modifier.height(sdp(_90sdp))
        val heightModifier = Modifier.height(sdp(_140sdp))
        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar(State.context.getString(R.string.toolbox_title))

            State.Theme.ScreenContainer(Modifier.padding(vertical = 10.dp)) {
                State.Theme.ElementsContainer(true) {
                    val modifier = Modifier.weight(1f)

                    RowInnerElementContainer(heightModifier) {
                        State.Theme.Button(Configs.sta(modifier, 2f))
                        State.Theme.Button(Configs.armSoftware(modifier, 1.5f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        State.Theme.Button(Configs.flashUefi(modifier, 1.5f))
                        State.Theme.Button(Configs.atlasos(modifier, 1.5f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        State.Theme.Button(Configs.usbHost(modifier, 1.2f))
                        State.Theme.Button(Configs.rotation(modifier, 1.2f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        State.Theme.Button(Configs.tabletMode(modifier, 1.5f))
                        State.Theme.Button(Configs.frameworks(modifier, 1.2f))
                    }
                    RowInnerElementContainer(heightModifier) {
                        State.Theme.Button(Configs.edgeRemover(modifier, 1.5f))
                        if (State.deviceConfig.isDumpModem)
                            State.Theme.Button(Configs.dumpModem(modifier, 2f))
                    }
                    if (State.deviceConfig.isDbkp)
                        State.Theme.Button(Configs.dbkp(modifier))
                }
            }
        }

        State.Theme.Loading()
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
                .background(State.Colors.background)
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
                            color = State.Colors.text,
                            fontSize = ssp(_15ssp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = Strings.version + if (Strings.test) " (DEV)" else "",
                            color = State.Colors.text,
                            fontSize = ssp(_12ssp)
                        )
                    }
                else
                    Text(
                        text = text,
                        fontSize = ssp(_16ssp),
                        color = State.Colors.text,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = sdp(_2sdp))
                    )
            }

            if (text == null)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(sdp(_10sdp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (Strings.test)
                        MainTheme.RefreshIcon(Modifier)
                    MainTheme.SettingsIcon(Modifier)
                }
        }
    }

    @Composable
    override fun Panel() {
        val isPortrait = State.isPortrait

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
                DeviceImage(modifier = Modifier.weight(1f))
                State.Theme.InfoBox(modifier = Modifier.weight(1.4f))
            }
        } else {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(sdp(_5sdp))
            ) {
                DeviceImage(modifier = Modifier.weight(1f))
                State.Theme.InfoBox(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    override fun InfoBox(modifier: Modifier) {
        val deviceName by State.viewModel.deviceName.collectAsState()
        val panel by State.viewModel.panelType.collectAsState()
        val ram by State.viewModel.totalRam.collectAsState()
        val lastBackup by State.viewModel.lastBackupDate.collectAsState()
        val slot by State.viewModel.slot.collectAsState()

        State.lastBackup = lastBackup

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
                .background(State.Colors.surface, shape = RoundedCornerShape(sdp(_10sdp)))
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
                        color = State.Colors.text,
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
                                    State.Theme.PanelItem(it)
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
                .background(State.Colors.surface),
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
                        color = State.Colors.text,
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
                        tint = if (config.tintImage) State.Colors.text else Color.Unspecified,
                        contentDescription = "button image"
                    )
                }
                HorizontalDivider()

                Text(
                    modifier = Modifier
                        .padding(start = sdp(_10sdp), top = sdp(_5sdp)),
                    text = config.subtitle,
                    color = State.Colors.text,
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
                    tint = if (config.tintImage) State.Colors.text else Color.Unspecified,
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
                        color = State.Colors.text,
                        fontWeight = FontWeight.Bold,
                        fontSize = ssp(_11ssp),
                    )
                    Text(
                        text = config.subtitle,
                        color = State.Colors.text,
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
    ) {
        MainTheme.ColorChanger(topBarText, initialColor, colorKey)
    }

    @Composable
    override fun Loading() {
        val isLoading by State.viewModel.isLoading.collectAsState()
        val hadLoaded by State.viewModel.hadLoaded.collectAsState()
        State.context.window.statusBarColor = State.Colors.background.toArgb()
        State.context.window.navigationBarColor = State.Colors.background.toArgb()

        if (isLoading && !hadLoaded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(State.Colors.background),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = "Loading...",
                    textAlign = TextAlign.Center,
                    color = State.Colors.text,
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