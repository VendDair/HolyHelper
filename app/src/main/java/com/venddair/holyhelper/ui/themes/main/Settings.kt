package com.venddair.holyhelper.ui.themes.main

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.venddair.holyhelper.R
import com.venddair.holyhelper.activities.sdp
import com.venddair.holyhelper.activities.ssp
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import com.intuit.sdp.R.dimen.*
import com.intuit.ssp.R.dimen.*

data class SettingsButtonConfig(
    val text: String,
    var isActive: Boolean,
    val onClick: () -> Unit
)

val itemModifier: Modifier
    @Composable
    get() = Modifier
        .clip(RoundedCornerShape(sdp(_8sdp)))
        .background(State.Colors.surface)
        .padding(horizontal = sdp(_8sdp))

@Composable
fun Settings(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(State.Colors.background)
    ) {
        TopBar(context.getString(R.string.preferences))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .padding(menuPadding),
            verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
        ) {
            var mountToMntChecked by remember {
                mutableStateOf(Preferences.MOUNTTOMNT.get())
            }
            var disableUpdatedChecked by remember {
                mutableStateOf(Preferences.DISABLEUPDATES.get())
            }

            var autoMountChecked by remember {
                mutableStateOf(Preferences.AUTOMOUNT.get())
            }

            var backupBootChecked by remember {
                mutableStateOf(Preferences.BACKUPBOOT.get())
            }

            var backupBootAndroid by remember {
                mutableStateOf(Preferences.BACKUPBOOTANDROID.get())
            }

            var backupBootWindows by remember {
                mutableStateOf(Preferences.BACKUPBOOTWINDOWS.get())
            }

            var qsQuickbootChecked by remember {
                mutableStateOf(Preferences.QSCONFIRMATION.get())
            }

            var requireUnlockedChecked by remember {
                mutableStateOf(Preferences.REQUIREUNLOCKED.get())
            }

            SettingsItem(
                text = context.getString(R.string.preference3),
                checked = backupBootChecked,
                onCheckedChange = {
                    backupBootChecked = it
                    Preferences.BACKUPBOOT.set(it)
                },
                buttons = listOf(
                    SettingsButtonConfig(
                        text = "Android",
                        isActive = backupBootAndroid,
                        onClick = {
                            backupBootAndroid = !backupBootAndroid
                            Preferences.BACKUPBOOTANDROID.set(backupBootAndroid)
                        }
                    ),
                    SettingsButtonConfig(
                        text = "Windows",
                        isActive = backupBootWindows,
                        onClick = {
                            backupBootWindows = !backupBootWindows
                            Preferences.BACKUPBOOTWINDOWS.set(backupBootWindows)
                        }
                    )
                )
            )

            SettingsItem(
                text = context.getString(R.string.preference10),
                checked = mountToMntChecked,
                onCheckedChange = {
                    mountToMntChecked = it
                    Preferences.MOUNTTOMNT.set(it)
                }
            )

            if (!Preferences.EASYTHEME.get())
                SettingsItem(
                    text = context.getString(R.string.preference11),
                    checked = disableUpdatedChecked,
                    onCheckedChange = {
                        disableUpdatedChecked = it
                        Preferences.DISABLEUPDATES.set(it)
                    }
                )

            SettingsItem(
                text = context.getString(R.string.preference7),
                checked = autoMountChecked,
                onCheckedChange = {
                    autoMountChecked = it
                    Preferences.AUTOMOUNT.set(it)
                }
            )

            SettingsItem(
                text = context.getString(R.string.preference5),
                checked = qsQuickbootChecked,
                onCheckedChange = {
                    qsQuickbootChecked = it
                    Preferences.QSCONFIRMATION.set(it)
                }
            )

            SettingsItem(
                text = context.getString(R.string.preference9),
                checked = requireUnlockedChecked,
                onCheckedChange = {
                    requireUnlockedChecked = it
                    Preferences.REQUIREUNLOCKED.set(it)
                }
            )


            SettingsButton(
                text = "Color Options",
                onClick = { navController.navigate("color_options") }
            )

            SettingsButton(
                text = "Theme Options",
                onClick = { navController.navigate("theme_options") }
            )

        }
    }
}

@Composable
fun ThemeSelector() {

    var defaultThemeChecked by remember {
        mutableStateOf(Preferences.DEFAULTTHEME.get())
    }

    var easyThemeChecked by remember {
        mutableStateOf(Preferences.EASYTHEME.get())
    }

    Column(
        modifier = Modifier
            .background(State.Colors.background)
            .fillMaxHeight()
    ) {
        TopBar("Theme Options")

        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(menuPadding),
            verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
        ) {

            SettingsItem(
                text = "Default Theme",
                checked = defaultThemeChecked,
                onCheckedChange = {
                    defaultThemeChecked = it
                    Preferences.DEFAULTTHEME.set(it)
                    easyThemeChecked = !it
                    Preferences.EASYTHEME.set(!it)
                }
            )

            SettingsItem(
                text = "Easy Theme",
                checked = easyThemeChecked,
                onCheckedChange = {
                    easyThemeChecked = it
                    Preferences.EASYTHEME.set(it)
                    defaultThemeChecked = !it
                    Preferences.DEFAULTTHEME.set(!it)
                }
            )


            //ApplySettingsButton()
        }
    }
}

@Composable
fun ColorOptions(navController: NavController) {

    val context = LocalContext.current

    var materialyouChecked by remember {
        mutableStateOf(Preferences.MATERIALYOU.get())
    }

    var colorsBasedOnDefault by remember {
        mutableStateOf(Preferences.COLORSBASEDONDEFAULT.get())
    }

    Column(
        modifier = Modifier
            .background(State.Colors.background)
            .fillMaxHeight()
    ) {
        TopBar("Color Options")

        Column(
            modifier = Modifier
                .padding(menuPadding),
            verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
        ) {

            AnimatedVisibility(!colorsBasedOnDefault) {
                SettingsItem(
                    text = "Use Material you",
                    checked = materialyouChecked,
                    onCheckedChange = {
                        materialyouChecked = it
                        Preferences.MATERIALYOU.set(it)
                        colorsBasedOnDefault = false
                        Preferences.COLORSBASEDONDEFAULT.set(false)
                    }
                )
            }

            AnimatedVisibility(!materialyouChecked) {
                SettingsItem(
                    text = context.getString(R.string.preference12),
                    checked = colorsBasedOnDefault,
                    onCheckedChange = {
                        colorsBasedOnDefault = it
                        Preferences.COLORSBASEDONDEFAULT.set(it)
                        materialyouChecked = false
                        Preferences.MATERIALYOU.set(false)
                    }
                )
            }

            AnimatedVisibility(colorsBasedOnDefault) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(sdp(_8sdp))
                ) {

                    SettingsButton(
                        text = "Main color",
                        onClick = { navController.navigate("main_color") }
                    )

                    SettingsButton(
                        text = "Text color",
                        onClick = { navController.navigate("text_color") }
                    )
                }
            }

            SettingsButton(
                text = "Reset",
                onClick = {
                    Preferences.COLOR.set(State.BaseColors.color)
                    Preferences.TEXTCOLOR.set(State.BaseColors.textColor)
                    Preferences.GUIDEGROUPCOLOR.set(State.BaseColors.guideGroupColor)
                    Preferences.MATERIALYOU.set(true)
                    Preferences.COLORSBASEDONDEFAULT.set(false)
                    State.restartApp(context)
                }
            )
            ApplySettingsButton()
        }
    }
}

@Composable
fun ApplySettingsButton() {

    val context = LocalContext.current

    SettingsButton(
        text = "Apply",
        onClick = { State.restartApp(context) }
    )
}

@Composable
fun MainColorChanger() {
    ColorChanger(
        topBarText = "Main color preference",
        initialColor = Color(Preferences.COLOR.get().toColorInt()),
        colorKey = Preferences.COLOR
    )
}

@Composable
fun TextColorChanger() {
    ColorChanger(
        topBarText = "Text color preference",
        initialColor = Color(Preferences.TEXTCOLOR.get().toColorInt()),
        colorKey = Preferences.TEXTCOLOR
    )
}

@Composable
fun ColorChanger(topBarText: String, initialColor: Color, colorKey: Preferences.Preference<String>) {
    val context = LocalContext.current

    val controller = rememberColorPickerController()

    var color by remember { mutableStateOf("FFFFFFFF") }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(State.Colors.background)
    ) {
        TopBar(topBarText)
        if (Device.isLandscape(context))
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
                        MiniButton(
                            text = "Apply",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .scale(1.4f),
                            onClick = {
                                colorKey.set("#$color")
                                State.restartApp(context)
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
                        MiniButton(
                            text = "Apply",
                            modifier = Modifier
                                .scale(1.4f)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = sdp(_13sdp)),
                            onClick = {
                                colorKey.set("#$color")
                                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)

                                Runtime.getRuntime().exit(0)
                            }
                        )
                    }
                }
            }

    }
}

@Composable
fun SettingsButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = itemModifier
            .clickable { onClick() }
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = sdp(_13sdp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = State.Colors.text,
            fontSize = ssp(_12ssp)
        )
    }
}

@Composable
fun SettingsItem(text: String, checked: Boolean = false, buttons: List<SettingsButtonConfig>? = null, onCheckedChange: (Boolean) -> Unit = {}) {

    var _checked = checked

    Box(
        modifier = itemModifier
            .clickable {
                _checked = !_checked
                onCheckedChange(_checked)
            }
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = sdp(_6sdp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                ) {
                    Text(
                        text = text,
                        color = State.Colors.text,
                        fontSize = ssp(_12ssp),
                    )
                }
                Switch(
                    checked = checked,
                    onCheckedChange = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            AnimatedVisibility(checked && buttons != null,) {
                Row(
                    modifier = Modifier
                        .padding(bottom = sdp(_6sdp)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    buttons?.forEachIndexed { index, config ->
                        val backgroundColor by animateColorAsState(
                            targetValue = if (config.isActive) lerp(Color(0xFF24ad1b), State.Colors.surface, 0.2f) else lerp(Color(0xFFc33436), State.Colors.surface, 0.2f),
                            animationSpec = tween(durationMillis = 250)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(sdp(_16sdp)))
                                .height(sdp(_30sdp))
                                .clickable { config.onClick() }
                                .background(backgroundColor),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = config.text,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = State.Colors.text,
                                fontSize = ssp(_13ssp),
                            )
                        }

                        if (index != buttons.lastIndex) {
                            Spacer(
                                modifier = Modifier
                                    .weight(0.05f)
                            )
                        }
                    }
                }
            }
        }
    }
}
