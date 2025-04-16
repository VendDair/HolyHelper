package com.venddair.holyhelper.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.ui.theme.generateAppColors
import com.venddair.holyhelper.ui.themes.getAppTheme
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.ToastUtil
import androidx.core.net.toUri
import com.venddair.holyhelper.utils.AppTheme
import com.venddair.holyhelper.utils.NavController
import com.venddair.holyhelper.utils.ViewModel
import com.venddair.holyhelper.utils.context
import com.venddair.holyhelper.utils.appColors
import com.venddair.holyhelper.utils.checkAppRestriction
import com.venddair.holyhelper.utils.checkRoot

class MainActivity : ComponentActivity() {

    private var isFirstResume = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ToastUtil.init(this@MainActivity)
        Preferences.init(this@MainActivity)
        Files.init(this@MainActivity)

        context = this

        if (savedInstanceState == null) {
            appColors = generateAppColors()

            val viewModel: MainViewModel by viewModels()

            ViewModel = viewModel

            AppTheme = getAppTheme()

            ViewModel.loadData()
        }

        context.window.statusBarColor = AppTheme.statusBarColor
        context.window.navigationBarColor = AppTheme.navigationBarColor

        setContent {
            checkRoot()
            checkAppRestriction()
            /*if (Shell.isAppGrantedRoot() != true)
                Info.noRootDetected(context)

            if (Device.isRestricted())
                Info.appRestricted(context)*/

            val navController = rememberNavController()
            NavController = navController
            NavHost(
                navController = navController,
                startDestination = "home",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -300 },
                        animationSpec = tween(300)
                    ) + fadeIn()
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -300 },
                        animationSpec = tween(300)
                    ) + fadeOut()
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 300 },
                        animationSpec = tween(300)
                    ) + fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { 300 },
                        animationSpec = tween(300)
                    ) + fadeOut()
                }
            ) {
                composable("home") { AppTheme.MainMenu() }
                composable("toolbox") { AppTheme.ToolboxMenu() }
                composable("settings") { AppTheme.SettingsMenu() }
                composable("color_options") { AppTheme.ColorsMenu() }
                composable("theme_options") { AppTheme.ThemesMenu()}
                composable("main_color") { AppTheme.ColorChanger(
                    topBarText = "Main color preference",
                    initialColor = Color(Preferences.COLOR.get().toColorInt()),
                    colorKey = Preferences.COLOR,
                    .1f,
                ) }
                composable("text_color") { AppTheme.ColorChanger(
                    topBarText = "Text color preference",
                    initialColor = Color(Preferences.TEXTCOLOR.get().toColorInt()),
                    colorKey = Preferences.TEXTCOLOR,
                    .35f,
                ) }
            }

        }
    }
    override fun onResume() {
        super.onResume()
        if (!isFirstResume) ViewModel.reloadEssentials()
        else isFirstResume = false
    }
}


fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

@Composable
fun sdp(sdp: Int): Dp {
    return dimensionResource(sdp)
}

@Composable
fun ssp(ssp: Int): TextUnit {
    return dimensionResource(ssp).value.sp
}
