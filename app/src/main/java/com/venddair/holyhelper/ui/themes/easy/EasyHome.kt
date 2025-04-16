package com.venddair.holyhelper.ui.themes.easy

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.venddair.holyhelper.ui.themes.ButtonConfig
import com.venddair.holyhelper.ui.themes.Configs
import com.venddair.holyhelper.ui.themes.SettingsButtonConfig
import com.venddair.holyhelper.ui.themes.SettingsMiniButtonConfig
import com.venddair.holyhelper.ui.themes.Theme
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.utils.AppTheme
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.appColors


object EasyTheme : Theme {
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
                    AppTheme.Button(Configs.sta(Modifier.weight(1f)))
                    AppTheme.Button(Configs.usbHost(Modifier.weight(1f)))
                    AppTheme.Button(Configs.quickboot(Modifier.weight(1f)))
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
        MainTheme.TopBar(text)
    }

    @Composable
    override fun Panel() {
        MainTheme.Panel()
    }

    @Composable
    override fun InfoBox(modifier: Modifier) {
        MainTheme.InfoBox(modifier)
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
        MainTheme.Button(config)
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
        MainTheme.Loading()
    }

}