package com.venddair.holyhelper.ui.themes.main

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.venddair.holyhelper.activities.sdp
import com.intuit.sdp.R.dimen.*
import com.intuit.ssp.R.dimen._10ssp
import com.intuit.ssp.R.dimen._11ssp
import com.intuit.ssp.R.dimen._12ssp
import com.intuit.ssp.R.dimen._13ssp
import com.intuit.ssp.R.dimen._15ssp
import com.intuit.ssp.R.dimen._16ssp
import com.intuit.ssp.R.dimen._9ssp
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.activities.openUrl
import com.venddair.holyhelper.activities.ssp
import com.venddair.holyhelper.ui.theme.BaseColors
import com.venddair.holyhelper.ui.themes.ButtonConfig
import com.venddair.holyhelper.ui.themes.Configs
import com.venddair.holyhelper.ui.themes.SettingsButtonConfig
import com.venddair.holyhelper.ui.themes.SettingsMiniButtonConfig
import com.venddair.holyhelper.ui.themes.Theme
import com.venddair.holyhelper.ui.themes.ThemeType
import com.venddair.holyhelper.utils.AppTheme
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.NavController
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.ViewModel
import kotlinx.coroutines.flow.update
import com.venddair.holyhelper.utils.appColors
import com.venddair.holyhelper.utils.context
import com.venddair.holyhelper.utils.deviceConfig
import com.venddair.holyhelper.utils.restartApp

object MainTheme : Theme {
    override val statusBarColor: Int
        get() = appColors.surface.toArgb()
    override val navigationBarColor: Int
        get() = appColors.background.toArgb()

    @Composable
    override fun MainMenu() {
        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar(null)

            AppTheme.ScreenContainer(Modifier) {
                AppTheme.Panel()
                AppTheme.ElementsContainer(false) {
                    AppTheme.Button(Configs.backupBoot(Modifier.weight(1f)))
                    AppTheme.Button(Configs.mount(Modifier.weight(1f)))
                    AppTheme.Button(Configs.toolbox(Modifier.weight(1f)))
                    AppTheme.Button(Configs.quickboot(Modifier.weight(1f)))
                }
            }
        }
    }

    @Composable
    override fun ToolboxMenu() {
        val modifier = Modifier.height(sdp(_90sdp))
        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar(context.getString(R.string.toolbox_title))

            AppTheme.ScreenContainer(Modifier.padding(vertical = 10.dp)) {
                AppTheme.ElementsContainer(true) {
                    AppTheme.Button(Configs.sta(modifier))
                    AppTheme.Button(Configs.armSoftware(modifier))
                    AppTheme.Button(Configs.flashUefi(modifier))
                    if (deviceConfig.isDumpModem)
                        AppTheme.Button(Configs.dumpModem(modifier))
                    if (deviceConfig.isDevCfg)
                        AppTheme.Button(Configs.devcfg(modifier))
                    AppTheme.Button(Configs.atlasos(modifier))
                    if (deviceConfig.isDbkp)
                        AppTheme.Button(Configs.dbkp(modifier))
                    AppTheme.Button(Configs.usbHost(modifier))
                    AppTheme.Button(Configs.rotation(modifier))
                    AppTheme.Button(Configs.tabletMode(modifier))
                    AppTheme.Button(Configs.frameworks(modifier))
                    AppTheme.Button(Configs.edgeRemover(modifier))
                }
            }
        }
    }

    @Composable
    override fun SettingsMenu() {
        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar(context.getString(R.string.preferences))

            AppTheme.ScreenContainer(Modifier) {
                AppTheme.ElementsContainer(true) {
                    AppTheme.SettingsItem(Configs.backupBootSetting())
                    AppTheme.SettingsItem(Configs.mountToMnt())
                    AppTheme.SettingsItem(Configs.disableUpdates())
                    AppTheme.SettingsItem(Configs.autoMount())
                    AppTheme.SettingsItem(Configs.requireConfirmationForQSQuickboot())
                    AppTheme.SettingsItem(Configs.requireUnlockedForQSQuickboot())
                    AppTheme.SettingsButton(Configs.colorOptions())

                    AppTheme.SettingsButton(Configs.themeOptions())
                }
            }
        }
    }

    @Composable
    override fun ColorsMenu() {
        var materialYouChecked by remember { mutableStateOf(Preferences.MATERIALYOU.get()) }
        var colorsBasedChecked by remember { mutableStateOf(Preferences.COLORSBASEDONDEFAULT.get()) }

        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar("Color Options")

            AppTheme.ScreenContainer(Modifier) {
                AppTheme.ElementsContainer(true) {
                    AnimatedVisibility(!colorsBasedChecked) {
                        AppTheme.SettingsItem(Configs.useMaterialYou(materialYouChecked) {
                            materialYouChecked = it
                            colorsBasedChecked = false
                        })
                    }
                    AnimatedVisibility(!materialYouChecked) {
                        AppTheme.SettingsItem(Configs.colorsBasedOnDefault(colorsBasedChecked) {
                            colorsBasedChecked = it
                            materialYouChecked = false
                        })
                    }

                    AnimatedVisibility(colorsBasedChecked) {
                        AppTheme.ElementsContainer(false) {
                            AppTheme.SettingsButton(Configs.mainColor())
                            AppTheme.SettingsButton(Configs.textColor())
                        }
                    }

                    AppTheme.SettingsButton(Configs.reset())
                    AppTheme.SettingsButton(Configs.apply())
                }
            }
        }

    }

    @Composable
    override fun ThemesMenu() {
        var theme by remember { mutableStateOf(Preferences.THEME.get()) }

        AppTheme.OuterColumn(Modifier) {
            AppTheme.TopBar("Theme Options")

            AppTheme.ScreenContainer(Modifier) {
                AppTheme.ElementsContainer(true) {
                    AppTheme.SettingsItem(Configs.defaultTheme(theme == ThemeType.MAIN.type) {
                        theme = ThemeType.MAIN.type
                    })

                    AppTheme.SettingsItem(Configs.easyTheme(theme == ThemeType.EASY.type) {
                        theme = ThemeType.EASY.type
                    })

                    if (Strings.dev) {
                        AppTheme.SettingsItem(Configs.ogwoahelper2_0Theme(theme == ThemeType.OGWOAHELPER2_0.type) {
                            theme = ThemeType.OGWOAHELPER2_0.type
                        })
                        AppTheme.SettingsItem(Configs.winCrossTheme(theme == ThemeType.WINCROSS.type) {
                            theme = ThemeType.WINCROSS.type
                        })
                    }
                    AppTheme.SettingsButton(Configs.apply())
                }
            }
        }

        
    }

    @Composable
    override fun OuterColumn(modifier: Modifier, content: @Composable () -> Unit) {
        val blurAmount by ViewModel.blurAmount.collectAsState()
        Box(
            modifier = modifier
                .background(appColors.background)
                .blur(blurAmount)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
            ) {
                content()
            }
        }

    }

    @Composable
    override fun ScreenContainer(modifier: Modifier, content: @Composable () -> Unit) {
        val padding = sdp(_8sdp)
        if (Device.isPortrait)
            Column(
                modifier = modifier
                    .padding(
                        top = 0.dp,
                        //start = if (State.isPortrait) padding else 0.dp,
                        start = padding,
                        end = padding,
                        bottom = padding
                    ),
                verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
            ) {
                content()
            }
        else
            Row(
                modifier = modifier
                    .padding(
                        top = 0.dp,
                        //start = if (State.isPortrait) padding else 0.dp,
                        start = padding,
                        end = padding,
                        bottom = padding
                    ),
                horizontalArrangement = Arrangement.spacedBy(sdp(_5sdp))
            ) {
                content()
            }
    }

    @Composable
    override fun ElementsContainer(scrollable: Boolean, content: @Composable (ColumnScope.() -> Unit)) {
        val padding = sdp(_8sdp)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (scrollable)
                        it.verticalScroll(rememberScrollState())
                    else
                        it
                },
            verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
        ) {
            content()
        }
    }

    @Composable
    override fun TopBar(text: String?) {
        Row(
            modifier = Modifier
                .background(appColors.surface)
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
                        RefreshIcon(Modifier)
                    SettingsIcon(Modifier)
                }
        }
    }

    @Composable
    override fun Panel() {
        val isPortrait = Device.isPortrait

        val padding = PaddingValues(horizontal = if (isPortrait) sdp(_10sdp) else sdp(_5sdp))
        val modifier = if (isPortrait) {
            Modifier.height(sdp(_160sdp)).fillMaxWidth().padding(padding)
        } else {
            Modifier.width(sdp(_250sdp)).fillMaxHeight().padding(padding)
        }

        if (isPortrait) {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(sdp(_5sdp))
                //horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DeviceImage(modifier = Modifier.weight(.8f))
                InfoBox(modifier = Modifier.weight(1f))
            }
        } else {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(sdp(_5sdp))
            ) {
                DeviceImage(modifier = Modifier.weight(1f))
                InfoBox(modifier = Modifier.weight(1f))
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
                                    PanelItem(it)
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
                            text = context.getString(R.string.group),
                            modifier = Modifier
                        ) {
                            context.openUrl(deviceConfig.groupLink)
                        }

                        AppTheme.MiniButton(
                            text = context.getString(R.string.guide),
                            modifier = Modifier
                        ) {
                            context.openUrl(deviceConfig.guideLink)
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun RefreshIcon(modifier: Modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_refresh),
            contentDescription = "refresh",
            tint = appColors.text,
            modifier = modifier
                .scale(1.5f)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { ViewModel.reloadEssentials() }
                )
                .size(sdp(_20sdp))
        )
    }

    @Composable
    override fun SettingsIcon(modifier: Modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_gear),
            contentDescription = "settings",
            tint = appColors.text,
            modifier = modifier
                .scale(1.5f)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { NavController.navigate("settings") }
                )
                .size(sdp(_20sdp))
        )
    }

    @Composable
    override fun DeviceImage(modifier: Modifier) {
        val drawable by ViewModel.drawable.collectAsState()
        val easterEgg1 by ViewModel.easterEgg1.collectAsState()

        Image(
            painter = painterResource(if (!easterEgg1) drawable else R.drawable.redfin),
            contentDescription = "Device Image",
            alignment = if (Device.isPortrait) Alignment.CenterStart else Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            Preferences.EASTEREGG1.set(!easterEgg1)
                            ViewModel.easterEgg1.update { !easterEgg1 }

                        }
                    )
                }
        )
    }

    @Composable
    override fun PanelItem(text: String) {
        Text(
            text = text,
            fontSize = ssp(_9ssp),
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
        val controller = rememberColorPickerController()

        var color by remember { mutableStateOf("FFFFFFFF") }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(appColors.background)
        ) {
            AppTheme.TopBar(topBarText)
            if (Device.isLandscape)
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            horizontal = sdp(_8sdp),
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HsvColorPicker(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .height(sdp(_200sdp)),
                        controller = controller,
                        initialColor = initialColor,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            color = colorEnvelope.hexCode
                        }
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .fillMaxHeight()
                            .weight(1f),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Box(
                            modifier = Modifier.weight(.8f),
                            contentAlignment = Alignment.Center
                        ) {
                            BrightnessSlider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(sdp(_25sdp)),
                                controller = controller,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(sdp(_30sdp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(sdp(_40sdp))
                                    .background(
                                        //controller.selectedColor.value,
                                        lerp(controller.selectedColor.value, Color(BaseColors.color.toColorInt()),1f - previewColorFactor),
                                        shape = RoundedCornerShape(sdp(_5sdp))
                                    )
                            )
                            AppTheme.MiniButton(
                                text = "Apply",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .scale(1.4f),
                                onClick = {
                                    colorKey.set("#$color")
                                    restartApp()
                                }
                            )
                        }
                    }
                }
            else
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            horizontal = sdp(_8sdp),
                            vertical = sdp(_70sdp)
                        ),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(sdp(_300sdp)),
                        controller = controller,
                        initialColor = initialColor,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            color = colorEnvelope.hexCode
                        }
                    )

                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(sdp(_25sdp)),
                        controller = controller,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(sdp(_40sdp))
                                    .align(Alignment.CenterHorizontally)
                                    .background(
                                        lerp(controller.selectedColor.value, Color(BaseColors.color.toColorInt()),1f - previewColorFactor),
                                        shape = RoundedCornerShape(sdp(_5sdp))
                                    )
                            )
                            AppTheme.MiniButton(
                                text = "Apply",
                                modifier = Modifier
                                    .scale(1.4f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = sdp(_13sdp)),
                                onClick = {
                                    colorKey.set("#$color")
                                    restartApp()
                                }
                            )
                        }
                    }
                }

        }
    }

    @Composable
    override fun Loading() {
        val isLoading by ViewModel.isLoading.collectAsState()
        val hadLoaded by ViewModel.hadLoaded.collectAsState()

        if (isLoading && !hadLoaded) {
            context.window.statusBarColor = appColors.surface.toArgb()
            context.window.navigationBarColor = appColors.surface.toArgb()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appColors.surface),
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
        else {
            context.window.statusBarColor = appColors.surface.toArgb()
            context.window.navigationBarColor = appColors.background.toArgb()
        }
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
                .background(appColors.surface)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
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
            }
        }
    }

    @Composable
    override fun MiniButton(text: String, modifier: Modifier, onClick: () -> Unit) {
        Box(
            modifier = modifier
                .clickable { onClick() }
                .background(appColors.primary, shape = RoundedCornerShape(sdp(_16sdp))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = ssp(_10ssp),
                color = appColors.text,
                modifier = Modifier
                    .padding(sdp(_7sdp), sdp(_3sdp))
            )
        }
    }

    @Composable
    override fun SettingsItem(config: SettingsButtonConfig) {
        Box(modifier = settingItemModifier.clickable {
            config.checked = !config.checked
            config.onCheckedChange(config.checked)
        }) {
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
                        color = appColors.text,
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
                                    lerp(Color(0xFF24ad1b), appColors.surface, 0.2f)
                                else
                                    lerp(Color(0xFFc33436), appColors.surface, 0.2f),
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
                                    color = appColors.text,
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
        }
    }

    @Composable
    override fun SettingsButton(config: SettingsMiniButtonConfig) {
        Box(
            modifier = settingItemModifier
                .clickable { config.onClick() }
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = sdp(_13sdp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = config.text,
                color = appColors.text,
                fontSize = ssp(_12ssp)
            )
        }
    }
}

val settingItemModifier: Modifier
    @Composable
    get() = Modifier
        .clip(RoundedCornerShape(sdp(_8sdp)))
        .background(appColors.surface)
        .padding(horizontal = sdp(_8sdp))