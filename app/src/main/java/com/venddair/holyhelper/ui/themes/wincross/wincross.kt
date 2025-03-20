package com.venddair.holyhelper.ui.themes.wincross

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.venddair.holyhelper.activities.sdp
import com.venddair.holyhelper.ui.themes.ButtonConfig
import com.venddair.holyhelper.ui.themes.Configs
import com.venddair.holyhelper.ui.themes.SettingsButtonConfig
import com.venddair.holyhelper.ui.themes.SettingsMiniButtonConfig
import com.venddair.holyhelper.ui.themes.Theme
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.ui.themes.ogwoaheler2_0.RowInnerElementContainer
import com.venddair.holyhelper.utils.State
import com.intuit.sdp.R.dimen.*
import com.intuit.ssp.R.dimen.*
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.activities.openUrl
import com.venddair.holyhelper.activities.ssp
import com.venddair.holyhelper.ui.themes.main.settingItemModifier
import com.venddair.holyhelper.ui.themes.ogwoaheler2_0.OGWoaHelper2_0Theme
import com.venddair.holyhelper.utils.Preferences

private val shape: RoundedCornerShape
    @Composable
    get() = RoundedCornerShape(sdp(_16sdp))


object WINCrossTheme : Theme {
    @Composable
    override fun MainMenu() {
        val modifier = Modifier.height(sdp(_100sdp))

        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar(null)

            State.Theme.ScreenContainer(Modifier) {
                State.Theme.Panel()
                State.Theme.ElementsContainer(false) {

                    SpecialContainer(Modifier.height(sdp(_150sdp)),"CONFIGURATIONS") {
                        Column(modifier = Modifier.padding(horizontal = sdp(_5sdp)))
                        {
                            State.Theme.SettingsItem(Configs.autoMount())
                            State.Theme.SettingsItem(Configs.requireConfirmationForQSQuickboot())
                            State.Theme.SettingsItem(Configs.requireUnlockedForQSQuickboot())
                        }
                    }

                    RowInnerElementContainer(modifier) {
                        State.Theme.Button(Configs.backupBoot(Modifier.weight(1f), 1.8f))
                        State.Theme.Button(Configs.toolbox(Modifier.weight(1f), 2f))
                    }
                    RowInnerElementContainer(modifier) {
                        State.Theme.Button(Configs.mount(Modifier.weight(1f), 1.5f))
                        State.Theme.Button(Configs.quickboot(Modifier.weight(1f), 3.5f))
                    }
                }
            }
        }

        State.Theme.Loading()
    }

    @Composable
    override fun ToolboxMenu() {
        MainTheme.ToolboxMenu()
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
        OGWoaHelper2_0Theme.TopBar(text)
    }

    @Composable
    override fun Panel() {
        OGWoaHelper2_0Theme.Panel()
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
            start = sdp(_4sdp),
            end = sdp(_4sdp),
            top = 0.dp,
            bottom = sdp(_4sdp)
        )

        SpecialContainer(modifier, "Windows on ARM") {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = sdp(_15sdp))
                    .fillMaxHeight()
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

                Row(
                    modifier = Modifier
                        .padding(buttonPadding)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    State.Theme.MiniButton(
                        text = State.context.getString(R.string.guide),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        State.context.openUrl(State.deviceConfig.guideLink)
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
                .clip(shape)
                .background(State.Colors.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(sdp(_4sdp), Alignment.CenterVertically)
            ) {
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
                Text(
                    modifier = Modifier
                        .padding(horizontal = sdp(_20sdp))
                        .fillMaxWidth(),
                    text = config.title,
                    color = State.Colors.text,
                    fontWeight = FontWeight.Bold,
                    fontSize = ssp(_11ssp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    override fun MiniButton(text: String, modifier: Modifier, onClick: () -> Unit) {
        MainTheme.MiniButton(text, modifier) { onClick() }
    }

    @Composable
    override fun SettingsItem(config: SettingsButtonConfig) {
        MainTheme.SettingsItem(config)
        /*Box(modifier = Modifier
            .clip(shape)
            .clickable {
                config.checked = !config.checked
                config.onCheckedChange(config.checked)
            }
        ) {
            Column(modifier = Modifier.animateContentSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = sdp(_6sdp)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = config.text,
                        color = State.Colors.text,
                        fontSize = ssp(_12ssp),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Switch(
                        checked = config.checked,
                        onCheckedChange = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                AnimatedVisibility(visible = config.checked && !config.buttons.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier.padding(bottom = sdp(_6sdp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        config.buttons!!.forEachIndexed { index, buttonConfig ->
                            val backgroundColor by animateColorAsState(
                                targetValue = if (buttonConfig.isActive)
                                    lerp(Color(0xFF24ad1b), State.Colors.surface, 0.2f)
                                else
                                    lerp(Color(0xFFc33436), State.Colors.surface, 0.2f),
                                animationSpec = tween(durationMillis = 250)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .height(sdp(_30sdp))
                                    .clip(RoundedCornerShape(sdp(_16sdp)))
                                    .clickable { buttonConfig.onClick() }
                                    .background(backgroundColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = buttonConfig.text,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = State.Colors.text,
                                    fontSize = ssp(_13ssp)
                                )
                            }
                            if (index != config.buttons.lastIndex) {
                                Spacer(modifier = Modifier.weight(0.05f))
                            }
                        }
                    }
                }
            }
        }*/
    }

    @Composable
    override fun SettingsButton(config: SettingsMiniButtonConfig) {
        MainTheme.SettingsButton(config)
    }

    @Composable
    override fun PanelItem(text: String) {
        Text(
            text = text,
            fontSize = ssp(_10ssp),
            color = State.Colors.text
        )
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
        OGWoaHelper2_0Theme.Loading()
    }

}

@Composable
private fun SpecialContainer(modifier: Modifier = Modifier, text: String = "", content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .background(Color("#FF2d9ce3".toColorInt()), shape = shape)
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = sdp(_1sdp))
                .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ) {
            Text(
                text = text,
                color = State.Colors.text,
                fontWeight = FontWeight.Bold,
                fontSize = ssp(_10ssp)
            )
        }
        Box(
            modifier = modifier
                .fillMaxHeight()
                .background(State.Colors.surface, shape = shape)
        ) {
            content()
        }
    }
}