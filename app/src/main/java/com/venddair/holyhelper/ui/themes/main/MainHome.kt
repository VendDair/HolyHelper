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
import androidx.compose.foundation.layout.wrapContentSize
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
import com.venddair.holyhelper.ui.themes.ButtonConfig
import com.venddair.holyhelper.ui.themes.Configs
import com.venddair.holyhelper.ui.themes.SettingsButtonConfig
import com.venddair.holyhelper.ui.themes.SettingsMiniButtonConfig
import com.venddair.holyhelper.ui.themes.Theme
import com.venddair.holyhelper.ui.themes.ThemeType
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import kotlinx.coroutines.flow.update

object MainTheme : Theme {

    @Composable
    override fun MainMenu() {
        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar(null)

            State.Theme.ScreenContainer(Modifier) {
                State.Theme.Panel()
                State.Theme.ElementsContainer(false) {
                    State.Theme.Button(Configs.backupBoot(Modifier.weight(1f)))
                    State.Theme.Button(Configs.mount(Modifier.weight(1f)))
                    State.Theme.Button(Configs.toolbox(Modifier.weight(1f)))
                    State.Theme.Button(Configs.quickboot(Modifier.weight(1f)))
                }
            }
        }

        State.Theme.Loading()
    }

    @Composable
    override fun ToolboxMenu() {
        val modifier = Modifier.height(sdp(_90sdp))
        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar(State.context.getString(R.string.toolbox_title))

            State.Theme.ScreenContainer(Modifier.padding(vertical = 10.dp)) {
                State.Theme.ElementsContainer(true) {
                    State.Theme.Button(Configs.sta(modifier))
                    State.Theme.Button(Configs.armSoftware(modifier))
                    State.Theme.Button(Configs.flashUefi(modifier))
                    if (State.deviceConfig.isDumpModem)
                        State.Theme.Button(Configs.dumpModem(modifier))
                    State.Theme.Button(Configs.atlasos(modifier))
                    if (State.deviceConfig.isDbkp)
                        State.Theme.Button(Configs.dbkp(modifier))
                    State.Theme.Button(Configs.usbHost(modifier))
                    State.Theme.Button(Configs.rotation(modifier))
                    State.Theme.Button(Configs.tabletMode(modifier))
                    State.Theme.Button(Configs.frameworks(modifier))
                    State.Theme.Button(Configs.edgeRemover(modifier))
                }
            }
        }

        State.Theme.Loading()
    }

    @Composable
    override fun SettingsMenu() {
        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar(State.context.getString(R.string.preferences))

            State.Theme.ScreenContainer(Modifier) {
                State.Theme.ElementsContainer(true) {
                    State.Theme.SettingsItem(Configs.backupBootSetting())
                    State.Theme.SettingsItem(Configs.mountToMnt())
                    State.Theme.SettingsItem(Configs.disableUpdates())
                    State.Theme.SettingsItem(Configs.autoMount())
                    State.Theme.SettingsItem(Configs.requireConfirmationForQSQuickboot())
                    State.Theme.SettingsItem(Configs.requireUnlockedForQSQuickboot())
                    State.Theme.SettingsButton(Configs.colorOptions())
                    State.Theme.SettingsButton(Configs.themeOptions())
                }
            }
        }

        State.Theme.Loading()
    }

    @Composable
    override fun ColorsMenu() {
        var materialYouChecked by remember { mutableStateOf(Preferences.MATERIALYOU.get()) }
        var colorsBasedChecked by remember { mutableStateOf(Preferences.COLORSBASEDONDEFAULT.get()) }

        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar("Color Options")

            State.Theme.ScreenContainer(Modifier) {
                State.Theme.ElementsContainer(true) {
                    AnimatedVisibility(!colorsBasedChecked) {
                        State.Theme.SettingsItem(Configs.useMaterialYou(materialYouChecked) {
                            materialYouChecked = it
                            colorsBasedChecked = false
                        })
                    }
                    AnimatedVisibility(!materialYouChecked) {
                        State.Theme.SettingsItem(Configs.colorsBasedOnDefault(colorsBasedChecked) {
                            colorsBasedChecked = it
                            materialYouChecked = false
                        })
                    }

                    AnimatedVisibility(colorsBasedChecked) {
                        State.Theme.ElementsContainer(false) {
                            State.Theme.SettingsButton(Configs.mainColor())
                            State.Theme.SettingsButton(Configs.textColor())
                        }
                    }

                    State.Theme.SettingsButton(Configs.reset())
                    State.Theme.SettingsButton(Configs.apply())
                }
            }
        }
        State.Theme.Loading()
    }

    @Composable
    override fun ThemesMenu() {
        var theme by remember { mutableStateOf(Preferences.THEME.get()) }

        State.Theme.OuterColumn(Modifier) {
            State.Theme.TopBar("Theme Options")

            State.Theme.ScreenContainer(Modifier) {
                State.Theme.ElementsContainer(true) {
                    State.Theme.SettingsItem(Configs.defaultTheme(theme == ThemeType.MAIN.type) {
                        theme = ThemeType.MAIN.type
                    })

                    State.Theme.SettingsItem(Configs.easyTheme(theme == ThemeType.EASY.type) {
                        theme = ThemeType.EASY.type
                    })

                    State.Theme.SettingsItem(Configs.ogwoahelper2_0Theme(theme == ThemeType.OGWOAHELPER2_0.type) {
                        theme = ThemeType.OGWOAHELPER2_0.type
                    })
                    State.Theme.SettingsItem(Configs.winCrossTheme(theme == ThemeType.WINCROSS.type) {
                        theme = ThemeType.WINCROSS.type
                    })
                    State.Theme.SettingsButton(Configs.apply())
                }
            }
        }

        State.Theme.Loading()
    }

    @Composable
    override fun OuterColumn(modifier: Modifier, content: @Composable () -> Unit) {
        Box(
            modifier = modifier
                .background(State.Colors.background)
                .blur(State.blurAmount)
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
        if (State.isPortrait)
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
                .background(State.Colors.surface)
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
                        RefreshIcon(Modifier)
                    SettingsIcon(Modifier)
                }
        }
    }

    @Composable
    override fun Panel() {
        val isPortrait = State.isPortrait

        val padding = PaddingValues(horizontal = if (isPortrait) sdp(_10sdp) else sdp(_5sdp))
        val modifier = if (isPortrait) {
            Modifier.height(sdp(_160sdp)).fillMaxWidth().padding(padding)
        } else {
            Modifier.width(sdp(_250sdp)).fillMaxHeight().padding(padding)
        }

        if (isPortrait) {
            Row(
                modifier = modifier,
                //horizontalArrangement = Arrangement.spacedBy(sdp(_5sdp))
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DeviceImage(modifier = Modifier.weight(1f))
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
                        State.Theme.MiniButton(
                            text = State.context.getString(R.string.group),
                            modifier = Modifier
                        ) {
                            State.context.openUrl(State.deviceConfig.groupLink)
                        }

                        State.Theme.MiniButton(
                            text = State.context.getString(R.string.guide),
                            modifier = Modifier
                        ) {
                            State.context.openUrl(State.deviceConfig.guideLink)
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
            tint = State.Colors.text,
            modifier = modifier
                .scale(1.5f)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { State.viewModel.onResume() }
                )
                .size(sdp(_20sdp))
        )
    }

    @Composable
    override fun SettingsIcon(modifier: Modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_gear),
            contentDescription = "settings",
            tint = State.Colors.text,
            modifier = modifier
                .scale(1.5f)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { State.navController.navigate("settings") }
                )
                .size(sdp(_20sdp))
        )
    }

    @Composable
    override fun DeviceImage(modifier: Modifier) {
        val drawable by State.viewModel.drawable.collectAsState()
        val easterEgg1 by State.viewModel.easterEgg1.collectAsState()

        Image(
            painter = painterResource(if (!easterEgg1) drawable else R.drawable.redfin),
            contentDescription = "Device Image",
            alignment = if (State.isPortrait) Alignment.CenterStart else Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            Preferences.EASTEREGG1.set(!easterEgg1)
                            State.viewModel.easterEgg1.update { !easterEgg1 }

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
            color = State.Colors.text
        )
    }

    @Composable
    override fun ColorChanger(topBarText: String, initialColor: Color, colorKey: Preferences.Preference<String>) {
        val controller = rememberColorPickerController()

        var color by remember { mutableStateOf("FFFFFFFF") }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(State.Colors.background)
        ) {
            TopBar(topBarText)
            if (Device.isLandscape(State.context))
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
                                        controller.selectedColor.value,
                                        shape = RoundedCornerShape(sdp(_5sdp))
                                    )
                            )
                            State.Theme.MiniButton(
                                text = "Apply",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .scale(1.4f),
                                onClick = {
                                    colorKey.set("#$color")
                                    State.restartApp()
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
                                        controller.selectedColor.value,
                                        shape = RoundedCornerShape(sdp(_5sdp))
                                    )
                            )
                            State.Theme.MiniButton(
                                text = "Apply",
                                modifier = Modifier
                                    .scale(1.4f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = sdp(_13sdp)),
                                onClick = {
                                    colorKey.set("#$color")
                                    State.restartApp()
                                }
                            )
                        }
                    }
                }

        }
    }

    @Composable
    override fun Loading() {
        val isLoading by State.viewModel.isLoading.collectAsState()
        val hadLoaded by State.viewModel.hadLoaded.collectAsState()

        if (isLoading && !hadLoaded) {
            State.context.window.statusBarColor = State.Colors.surface.toArgb()
            State.context.window.navigationBarColor = State.Colors.surface.toArgb()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(State.Colors.surface),
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
        else {
            State.context.window.statusBarColor = State.Colors.surface.toArgb()
            State.context.window.navigationBarColor = State.Colors.background.toArgb()
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
                .background(State.Colors.surface)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
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
            }
        }
    }

    @Composable
    override fun MiniButton(text: String, modifier: Modifier, onClick: () -> Unit) {
        Box(
            modifier = modifier
                .clickable { onClick() }
                .background(State.Colors.primary, shape = RoundedCornerShape(sdp(_16sdp))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = ssp(_10ssp),
                color = State.Colors.text,
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
                color = State.Colors.text,
                fontSize = ssp(_12ssp)
            )
        }
    }
}

val settingItemModifier: Modifier
    @Composable
    get() = Modifier
        .clip(RoundedCornerShape(sdp(_8sdp)))
        .background(State.Colors.surface)
        .padding(horizontal = sdp(_8sdp))