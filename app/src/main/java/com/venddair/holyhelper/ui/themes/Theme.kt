package com.venddair.holyhelper.ui.themes

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.venddair.holyhelper.ui.themes.easy.EasyTheme
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.ui.themes.ogwoaheler2_0.OGWoaHelper2_0Theme
import com.venddair.holyhelper.ui.themes.wincross.WINCrossTheme
import com.venddair.holyhelper.utils.Preferences

enum class ThemeType(val type: String) {
    MAIN("main"),
    EASY("easy"),
    OGWOAHELPER2_0("ogwoahelper2_0"),
    WINCROSS("WINCross"),
}

interface Theme {

    @Composable
    fun MainMenu()

    @Composable
    fun ToolboxMenu()

    @Composable
    fun SettingsMenu()

    @Composable
    fun ColorsMenu()

    @Composable
    fun ThemesMenu()

    @Composable
    fun OuterColumn(modifier: Modifier, content: @Composable () -> Unit)

    @Composable
    fun ScreenContainer(modifier: Modifier, content: @Composable () -> Unit)

    @Composable
    fun ElementsContainer(scrollable: Boolean, content: @Composable ColumnScope.() -> Unit)

    @Composable
    fun TopBar(text: String?)

    @Composable
    fun Panel()

    @Composable
    fun InfoBox(modifier: Modifier)

    @Composable
    fun RefreshIcon(modifier: Modifier)

    @Composable
    fun SettingsIcon(modifier: Modifier)

    @Composable
    fun DeviceImage(modifier: Modifier)

    @Composable
    fun Button(config: ButtonConfig)

    @Composable
    fun MiniButton(text: String, modifier: Modifier, onClick: () -> Unit)

    @Composable
    fun SettingsItem(config: SettingsButtonConfig)

    @Composable
    fun SettingsButton(config: SettingsMiniButtonConfig)

    @Composable
    fun PanelItem(text: String)

    @Composable
    fun ColorChanger(topBarText: String, initialColor: Color, colorKey: Preferences.Preference<String>)

    @Composable
    fun Loading()
}

fun getAppTheme(): Theme {
    return when (Preferences.THEME.get()) {
        ThemeType.MAIN.type -> MainTheme
        ThemeType.EASY.type -> EasyTheme
        ThemeType.OGWOAHELPER2_0.type -> OGWoaHelper2_0Theme
        ThemeType.WINCROSS.type -> WINCrossTheme
        else -> MainTheme
    }
}
