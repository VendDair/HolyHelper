package com.venddair.holyhelper.ui.themes.wincross

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.intuit.sdp.R.dimen._100sdp
import com.intuit.sdp.R.dimen._150sdp
import com.intuit.sdp.R.dimen._15sdp
import com.intuit.sdp.R.dimen._16sdp
import com.intuit.sdp.R.dimen._1sdp
import com.intuit.sdp.R.dimen._20sdp
import com.intuit.sdp.R.dimen._4sdp
import com.intuit.sdp.R.dimen._5sdp
import com.intuit.ssp.R.dimen._10ssp
import com.intuit.ssp.R.dimen._11ssp
import com.venddair.holyhelper.R
import com.venddair.holyhelper.activities.openUrl
import com.venddair.holyhelper.activities.sdp
import com.venddair.holyhelper.activities.ssp
import com.venddair.holyhelper.ui.themes.ButtonConfig
import com.venddair.holyhelper.ui.themes.Configs
import com.venddair.holyhelper.ui.themes.SettingsButtonConfig
import com.venddair.holyhelper.ui.themes.SettingsMiniButtonConfig
import com.venddair.holyhelper.ui.themes.Theme
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.ui.themes.ogwoaheler2_0.OGWoaHelper2_0Theme
import com.venddair.holyhelper.ui.themes.ogwoaheler2_0.RowInnerElementContainer
import com.venddair.holyhelper.utils.AppTheme
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.ViewModel
import com.venddair.holyhelper.utils.appColors
import com.venddair.holyhelper.utils.context
import com.venddair.holyhelper.utils.deviceConfig

private val shape: RoundedCornerShape
    @Composable
    get() = RoundedCornerShape(sdp(_16sdp))


object WINCrossTheme : Theme {
    override val statusBarColor: Int
        get() = appColors.surface.toArgb()
    override val navigationBarColor: Int
        get() = appColors.background.toArgb()

    @Composable
    override fun MainMenu() {
        val modifier = Modifier.height(sdp(_100sdp))

        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar(null)

            AppTheme.ScreenContainer(Modifier) {
                AppTheme.Panel()
                AppTheme.ElementsContainer(false) {

                    SpecialContainer(Modifier.height(sdp(_150sdp)),"CONFIGURATIONS") {
                        Column(modifier = Modifier.padding(horizontal = sdp(_5sdp)))
                        {
                            AppTheme.SettingsItem(Configs.autoMount())
                            AppTheme.SettingsItem(Configs.requireConfirmationForQSQuickboot())
                            AppTheme.SettingsItem(Configs.requireUnlockedForQSQuickboot())
                        }
                    }

                    RowInnerElementContainer(modifier) {
                        AppTheme.Button(Configs.backupBoot(Modifier.weight(1f), 1.8f))
                        AppTheme.Button(Configs.toolbox(Modifier.weight(1f), 2f))
                    }
                    RowInnerElementContainer(modifier) {
                        AppTheme.Button(Configs.mount(Modifier.weight(1f), 1.5f))
                        AppTheme.Button(Configs.quickboot(Modifier.weight(1f), 3.5f))
                    }
                }
            }
        }

        
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
        val deviceName by ViewModel.deviceName.collectAsState()
        val panel by ViewModel.panelType.collectAsState()
        val ram by ViewModel.totalRam.collectAsState()
        val lastBackup by ViewModel.lastBackupDate.collectAsState()
        val slot by ViewModel.slot.collectAsState()

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
                                AppTheme.PanelItem(it)
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
                    AppTheme.MiniButton(
                        text = context.getString(R.string.guide),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        context.openUrl(deviceConfig.guideLink)
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
                .background(appColors.surface)
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
                    tint = if (config.tintImage) appColors.text else Color.Unspecified,
                    contentDescription = "button image"
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = sdp(_20sdp))
                        .fillMaxWidth(),
                    text = config.title,
                    color = appColors.text,
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
            color = appColors.text
        )
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
                color = appColors.text,
                fontWeight = FontWeight.Bold,
                fontSize = ssp(_10ssp)
            )
        }
        Box(
            modifier = modifier
                .fillMaxHeight()
                .background(appColors.surface, shape = shape)
        ) {
            content()
        }
    }
}