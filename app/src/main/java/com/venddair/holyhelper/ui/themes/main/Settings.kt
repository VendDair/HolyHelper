package com.venddair.holyhelper.ui.themes.main

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Preferences

data class ButtonConfig(
    val text: String,
    var isActive: Boolean,
    val onClick: () -> Unit
)

@Composable
fun Settings() {
    val context = LocalContext.current

    Column {
        TopBar(context.getString(R.string.preferences))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between items
        ) {
            var mountToMntChecked by remember {
                mutableStateOf(
                    Preferences.getBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.MOUNTTOMNT,
                        false
                    )
                )
            }
            var disableUpdatedChecked by remember {
                mutableStateOf(
                    Preferences.getBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.DISABLEUPDATES,
                        false
                    )
                )
            }

            var autoMountChecked by remember {
                mutableStateOf(
                    Preferences.getBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.AUTOMOUNT,
                        false
                    )
                )
            }

            var backupBootChecked by remember {
                mutableStateOf(
                    Preferences.getBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.BACKUPBOOT,
                        true
                    )
                )
            }

            var backupBootAndroid by remember {
                mutableStateOf(
                    Preferences.getBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.BACKUPBOOTANDROID,
                        true
                    )
                )
            }

            var backupBootWindows by remember {
                mutableStateOf(
                    Preferences.getBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.BACKUPBOOTWINDOWS,
                        true
                    )
                )
            }

            SettingsItem(
                text = context.getString(R.string.preference3),
                checked = backupBootChecked,
                onCheckedChange = {
                    backupBootChecked = it
                    Preferences.putBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.BACKUPBOOT,
                        it
                    )
                },
                buttons = listOf(
                    ButtonConfig(
                        text = "Android",
                        isActive = backupBootAndroid,
                        onClick = {
                            backupBootAndroid = !backupBootAndroid
                            Preferences.putBoolean(
                                Preferences.Preference.SETTINGS,
                                Preferences.Key.BACKUPBOOTANDROID,
                                backupBootAndroid
                            )
                        }
                    ),
                    ButtonConfig(
                        text = "Windows",
                        isActive = backupBootWindows,
                        onClick = {
                            backupBootWindows = !backupBootWindows
                            Preferences.putBoolean(
                                Preferences.Preference.SETTINGS,
                                Preferences.Key.BACKUPBOOTWINDOWS,
                                backupBootWindows
                            )
                        }
                    )
                )
            )

            SettingsItem(
                text = context.getString(R.string.preference10),
                checked = mountToMntChecked,
                onCheckedChange = {
                    mountToMntChecked = it
                    Preferences.putBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.MOUNTTOMNT,
                        it
                    )
                }
            )
            SettingsItem(
                text = context.getString(R.string.preference11),
                checked = disableUpdatedChecked,
                onCheckedChange = {
                    disableUpdatedChecked = it
                    Preferences.putBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.DISABLEUPDATES,
                        it
                    )
                }
            )
            SettingsItem(
                text = context.getString(R.string.preference7),
                checked = autoMountChecked,
                onCheckedChange = {
                    autoMountChecked = it
                    Preferences.putBoolean(
                        Preferences.Preference.SETTINGS,
                        Preferences.Key.AUTOMOUNT,
                        it
                    )
                }
            )
        }
    }
}

@Composable
fun SettingsItem(text: String, checked: Boolean = false, buttons: List<ButtonConfig>? = null, onCheckedChange: (Boolean) -> Unit = {}) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF404040))
            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                ) {
                    Text(
                        text = text,
                        color = colorResource(R.color.white),
                        fontSize = dimensionResource(com.intuit.ssp.R.dimen._12ssp).value.sp,
                    )
                }
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                )
            }

            AnimatedVisibility(
                visible = checked && buttons != null,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    buttons?.forEachIndexed { index, config ->
                        val backgroundColor by animateColorAsState(
                            targetValue = if (config.isActive) Color(0xFF43ff0b) else Color(0xFFFF420B),
                            animationSpec = tween(durationMillis = 250)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .height(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
                                .clickable { config.onClick() }
                                .background(backgroundColor),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = config.text,
                                textAlign = TextAlign.Center
                            )
                        }

                        if (index != buttons.lastIndex) {
                            Spacer(
                                modifier = Modifier
                                    .weight(0.05f)
                            )
                        }
                    }
                    /*Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .height(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
                            .clickable { }
                            .background(if (backupBootAndroid) Color(0xFF43ff0b) else Color(0xFFFF420B)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Android",
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .weight(0.05f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .height(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
                            .clickable {
                                backupBootWindows = !backupBootWindows
                                Preferences.putBoolean(
                                    Preferences.Preference.SETTINGS,
                                    Preferences.Key.BACKUPBOOTWINDOWS,
                                    backupBootWindows
                                )
                            }
                            .background(if (backupBootWindows) Color(0xFF43ff0b) else Color(0xFFFF420B)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Windows",
                            textAlign = TextAlign.Center
                        )
                    }*/
                }
            }
        }
    }
}
