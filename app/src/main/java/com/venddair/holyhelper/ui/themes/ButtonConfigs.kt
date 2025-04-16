package com.venddair.holyhelper.ui.themes

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.MainActivityFunctions
import com.venddair.holyhelper.utils.NavController
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.ViewModel
import com.venddair.holyhelper.utils.context
import com.venddair.holyhelper.ui.theme.BaseColors
import com.venddair.holyhelper.utils.restartApp
import kotlinx.coroutines.flow.update

data class ButtonConfig(
    var modifier: Modifier = Modifier,
    val image: Int,
    val tintImage: Boolean = false,
    val title: String = "",
    val subtitle: String = "",
    val imageScale: Float = 1f,
    val disabled: Boolean = false,
    val onClick: () -> Unit = {},
)

data class SettingsButtonConfig(
    val text: String,
    var checked: Boolean = false,
    val buttons: List<SettingsMiniButtonConfig>? = null,
    val onCheckedChange: (Boolean) -> Unit = {}
)

data class SettingsMiniButtonConfig(
    val text: String,
    var isActive: Boolean = true,
    val onClick: () -> Unit
)

object Configs {

    @Composable
    fun colorOptions(): SettingsMiniButtonConfig {
        return SettingsMiniButtonConfig(
            text = "Color Options",
            onClick = { NavController.navigate("color_options") }
        )
    }
    @Composable
    fun themeOptions(): SettingsMiniButtonConfig {
        return SettingsMiniButtonConfig(
            text = "Theme Options",
            onClick = { NavController.navigate("theme_options") }
        )
    }
    @Composable
    fun mainColor(): SettingsMiniButtonConfig {
        return SettingsMiniButtonConfig(
            text = "Main color",
            onClick = { NavController.navigate("main_color") }
        )
    }
    @Composable
    fun textColor(): SettingsMiniButtonConfig {
        return SettingsMiniButtonConfig(
            text = "Text color",
            onClick = { NavController.navigate("text_color") }
        )
    }
    @Composable
    fun reset(): SettingsMiniButtonConfig {
        return SettingsMiniButtonConfig(
            text = "Reset",
            onClick = {
                Preferences.COLOR.set(BaseColors.color)
                Preferences.TEXTCOLOR.set(BaseColors.textColor)
                Preferences.GUIDEGROUPCOLOR.set(BaseColors.guideGroupColor)
                Preferences.MATERIALYOU.set(true)
                Preferences.COLORSBASEDONDEFAULT.set(false)
                restartApp()
            }
        )
    }
    @Composable
    fun apply(): SettingsMiniButtonConfig {
        return SettingsMiniButtonConfig(
            text = "Apply",
            onClick = { restartApp() }
        )
    }

    @Composable
    fun defaultTheme(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SettingsButtonConfig {
        return SettingsButtonConfig(
            text = "Default Theme",
            checked = checked,
            onCheckedChange = {
                Preferences.THEME.set(ThemeType.MAIN.type)
                onCheckedChange(it)
            }
        )
    }
    @Composable
    fun easyTheme(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SettingsButtonConfig {
        return SettingsButtonConfig(
            text = "Easy Theme",
            checked = checked,
            onCheckedChange = {
                Preferences.THEME.set(ThemeType.EASY.type)
                onCheckedChange(it)
            }
        )
    }
    @Composable
    fun ogwoahelper2_0Theme(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SettingsButtonConfig {
        return SettingsButtonConfig(
            text = "OG Woa Helper 2.0 Theme",
            checked = checked,
            onCheckedChange = {
                Preferences.THEME.set(ThemeType.OGWOAHELPER2_0.type)
                onCheckedChange(it)
            }
        )
    }
    @Composable
    fun winCrossTheme(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SettingsButtonConfig {
        return SettingsButtonConfig(
            text = "WINCross Theme",
            checked = checked,
            onCheckedChange = {
                Preferences.THEME.set(ThemeType.WINCROSS.type)
                onCheckedChange(it)
            }
        )
    }
    @Composable
    fun useMaterialYou(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SettingsButtonConfig {
        return SettingsButtonConfig(
            text = "Use Material you",
            checked = checked,
            onCheckedChange = {
                Preferences.MATERIALYOU.set(it)
                Preferences.COLORSBASEDONDEFAULT.set(false)
                onCheckedChange(it)
            }
        )
    }
    @Composable
    fun colorsBasedOnDefault(checked: Boolean, onCheckedChange: (Boolean) -> Unit): SettingsButtonConfig {
        return SettingsButtonConfig(
            text = context.getString(R.string.preference12),
            checked = checked,
            onCheckedChange = {
                Preferences.COLORSBASEDONDEFAULT.set(it)
                Preferences.MATERIALYOU.set(false)
                onCheckedChange(it)
            }
        )
    }
    @Composable
    fun requireUnlockedForQSQuickboot(): SettingsButtonConfig {
        var requireUnlockedChecked by remember { mutableStateOf(Preferences.REQUIREUNLOCKED.get()) }

        return SettingsButtonConfig(
            text = context.getString(R.string.preference9),
            checked = requireUnlockedChecked,
            onCheckedChange = {
                requireUnlockedChecked = it
                Preferences.REQUIREUNLOCKED.set(it)
            }
        )
    }
    @Composable
    fun requireConfirmationForQSQuickboot(): SettingsButtonConfig {
        var qsQuickbootChecked by remember { mutableStateOf(Preferences.QSCONFIRMATION.get()) }

        return SettingsButtonConfig(
            text = context.getString(R.string.preference5),
            checked = qsQuickbootChecked,
            onCheckedChange = {
                qsQuickbootChecked = it
                Preferences.QSCONFIRMATION.set(it)
            }
        )
    }
    @Composable
    fun autoMount(): SettingsButtonConfig {
        var autoMountChecked by remember { mutableStateOf(Preferences.AUTOMOUNT.get()) }

        return SettingsButtonConfig(
            text = context.getString(R.string.preference7),
            checked = autoMountChecked,
            onCheckedChange = {
                autoMountChecked = it
                Preferences.AUTOMOUNT.set(it)
            }
        )

    }
    @Composable
    fun disableUpdates(): SettingsButtonConfig {
        var disableUpdatedChecked by remember { mutableStateOf(Preferences.DISABLEUPDATES.get()) }

        return SettingsButtonConfig(
            text = context.getString(R.string.preference11),
            checked = disableUpdatedChecked,
            onCheckedChange = {
                disableUpdatedChecked = it
                Preferences.DISABLEUPDATES.set(it)
            }
        )
    }
    @Composable
    fun backupBootSetting(): SettingsButtonConfig {
        var backupBootChecked by remember { mutableStateOf(Preferences.BACKUPBOOT.get()) }
        var backupBootAndroid by remember { mutableStateOf(Preferences.BACKUPBOOTANDROID.get()) }
        var backupBootWindows by remember { mutableStateOf(Preferences.BACKUPBOOTWINDOWS.get()) }

        return SettingsButtonConfig(
            text = context.getString(R.string.preference3),
            checked = backupBootChecked,
            onCheckedChange = {
                backupBootChecked = it
                Preferences.BACKUPBOOT.set(it)
            },
            buttons = listOf(
                SettingsMiniButtonConfig(
                    text = "Android",
                    isActive = backupBootAndroid,
                    onClick = {
                        backupBootAndroid = !backupBootAndroid
                        Preferences.BACKUPBOOTANDROID.set(backupBootAndroid)
                    }
                ),
                    SettingsMiniButtonConfig(
                    text = "Windows",
                    isActive = backupBootWindows,
                    onClick = {
                        backupBootWindows = !backupBootWindows
                        Preferences.BACKUPBOOTWINDOWS.set(backupBootWindows)
                    }
                )
            )
        )
    }
    @Composable
    fun mountToMnt(): SettingsButtonConfig {
        var mountToMntChecked by remember { mutableStateOf(Preferences.MOUNTTOMNT.get()) }

        return SettingsButtonConfig(
            text = context.getString(R.string.preference10),
            checked = mountToMntChecked,
            onCheckedChange = {
                mountToMntChecked = it
                Preferences.MOUNTTOMNT.set(it)
            }

        )
    }

    @Composable
    fun backupBoot(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            modifier = modifier,
            image = R.drawable.cd,
            imageScale = imageScale ?: 1f,
            title = context.getString(R.string.backup_boot_title),
            subtitle = context.getString(R.string.backup_boot_subtitle),
            onClick = { MainActivityFunctions.backupBoot() }
        )
    }

    @Composable
    fun mount(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        val mountText by ViewModel.mountText.collectAsState()

        return ButtonConfig(
            modifier = modifier,
            image = R.drawable.folder,
            imageScale = imageScale ?: 0.8f,
            title = mountText,
            subtitle = context.getString(R.string.mnt_subtitle),
            onClick = { MainActivityFunctions.mountWindows() }
        )
    }

    @Composable
    fun toolbox(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            modifier = modifier,
            image = R.drawable.toolbox,
            imageScale = imageScale ?: 1f,
            title = context.getString(R.string.toolbox_title),
            subtitle = context.getString(R.string.toolbox_subtitle),
            onClick = { NavController.navigate("toolbox") }
        )
    }

    @SuppressLint("StringFormatInvalid")
    @Composable
    fun quickboot(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        val isUefiPresent by ViewModel.isUefiFilePresent.collectAsState()

        val context = LocalContext.current as ComponentActivity

        return ButtonConfig(
            modifier = modifier,
            image = R.drawable.ic_launcher_foreground,
            imageScale = imageScale ?: 2f,
            disabled = !isUefiPresent,
            title = if (isUefiPresent) context.getString(R.string.quickboot_title) else context.getString(R.string.uefi_not_found),
            subtitle = if (isUefiPresent) context.getString(R.string.quickboot_subtitle) else context.getString(R.string.uefi_not_found_subtitle, Device.get()),
            onClick = { MainActivityFunctions.quickboot() }
        )
    }

    fun sta(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {

        return ButtonConfig(
            image = R.drawable.adrod,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            title = context.getString(R.string.sta_title),
            subtitle = context.getString(R.string.sta_subtitle),
            onClick = { MainActivityFunctions.sta_creator() }
        )
    }

    fun usbHost(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {

        return ButtonConfig(
            image = R.drawable.folder,
            imageScale = imageScale ?: 0.8f,
            modifier = modifier,
            title = context.getString(R.string.usbhost_title),
            subtitle = context.getString(R.string.usbhost_subtitle),
            onClick = { MainActivityFunctions.usb_host_mode() }
        )
    }

    fun armSoftware(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.ic_sensor,
            imageScale = imageScale ?: 1f,
            tintImage = true,
            modifier = modifier,
            title = context.getString(R.string.software_title),
            subtitle = context.getString(R.string.software_subtitle),
            onClick = { MainActivityFunctions.arm_software() }
        )
    }

    @Composable
    @SuppressLint("StringFormatInvalid")
    fun flashUefi(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        val isUefiPresent by ViewModel.isUefiFilePresent.collectAsState()
        return ButtonConfig(
            image = R.drawable.ic_uefi,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            disabled = !isUefiPresent,
            title = if (isUefiPresent) context.getString(R.string.flash_uefi_title) else context.getString(
                R.string.uefi_not_found
            ),
            subtitle = if (isUefiPresent) context.getString(R.string.flash_uefi_subtitle) else context.getString(
                R.string.uefi_not_found_subtitle,
                Device.get()
            ),
            onClick = { if (isUefiPresent) MainActivityFunctions.flash_uefi() }
        )
    }

    fun dumpModem(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.ic_modem,
            imageScale = imageScale ?: 1.1f,
            modifier = modifier,
            title = context.getString(R.string.dump_modem_title),
            subtitle = context.getString(R.string.dump_modem_subtitle),
            onClick = { MainActivityFunctions.dump_modem() }
        )
    }

    fun devcfg(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.ic_uefi,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            title = context.getString(R.string.devcfg_title),
            subtitle = context.getString(R.string.devcfg_subtitle),
            onClick = { MainActivityFunctions.devcfg() }
        )
    }

    fun atlasos(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.atlasos,
            imageScale = imageScale ?: .8f,
            modifier = modifier,
            title = context.getString(R.string.atlasos_title),
            subtitle = context.getString(R.string.atlasos_subtitle),
            onClick = { MainActivityFunctions.atlasos() }
        )
    }

    fun dbkp(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.ic_uefi,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            title = context.getString(R.string.dbkp_title),
            subtitle = context.getString(R.string.dbkp_subtitle),
            onClick = { MainActivityFunctions.dbkp() }
        )
    }

    fun rotation(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.cd,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            title = context.getString(R.string.rotation_title),
            subtitle = context.getString(R.string.rotation_subtitle),
            onClick = { MainActivityFunctions.rotation() }
        )
    }

    fun tabletMode(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.ic_sensor,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            title = context.getString(R.string.tablet_title),
            subtitle = context.getString(R.string.tablet_subtitle),
            onClick = { MainActivityFunctions.optimizedTaskbar() }
        )
    }

    fun frameworks(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.folder,
            imageScale = imageScale ?: .8f,
            modifier = modifier,
            title = context.getString(R.string.setup_title),
            subtitle = context.getString(R.string.setup_subtitle),
            onClick = { MainActivityFunctions.frameworks() }
        )
    }

    fun edgeRemover(modifier: Modifier = Modifier, imageScale: Float? = null): ButtonConfig {
        return ButtonConfig(
            image = R.drawable.edge,
            imageScale = imageScale ?: 1f,
            modifier = modifier,
            title = context.getString(R.string.defender_title),
            subtitle = context.getString(R.string.defender_subtitle),
            onClick = { MainActivityFunctions.edge() }
        )
    }
}
