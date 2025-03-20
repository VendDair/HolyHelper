package com.venddair.holyhelper.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.venddair.holyhelper.R
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.ui.theme.generateAppColors
import com.venddair.holyhelper.ui.themes.ThemeType
import com.venddair.holyhelper.ui.themes.getAppTheme
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.ToastUtil
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {

    private var isFirstResume = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        State.coroutineInit()
        ToastUtil.init(this@MainActivity)
        Preferences.init(this@MainActivity)
        Files.init(this@MainActivity)

        State.context = this

        if (savedInstanceState == null) {
            val viewModel: MainViewModel by viewModels()

            State.viewModel = viewModel

            State.Theme = getAppTheme()
        }

        setContent {

            LaunchedEffect(Unit) {
                State.viewModel.loadData(State.context)
            }

            if (!State.launch) {
                State.launch = true

                State.Colors = generateAppColors()

                /*if (Shell.isAppGrantedRoot() != true)
                    Info.noRootDetected(State.context)

                if (Device.isRestricted())
                    Info.appRestricted(State.context)*/
            }

            val navController = rememberNavController()
            State.navController = navController
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
                composable("home") { State.Theme.MainMenu() }
                composable("toolbox") { State.Theme.ToolboxMenu() }
                composable("settings") { State.Theme.SettingsMenu() }
                composable("color_options") { State.Theme.ColorsMenu() }
                composable("theme_options") { State.Theme.ThemesMenu()}
                composable("main_color") { State.Theme.ColorChanger(
                    topBarText = "Main color preference",
                    initialColor = Color(Preferences.COLOR.get().toColorInt()),
                    colorKey = Preferences.COLOR
                ) }
                composable("text_color") { State.Theme.ColorChanger(
                    topBarText = "Text color preference",
                    initialColor = Color(Preferences.TEXTCOLOR.get().toColorInt()),
                    colorKey = Preferences.TEXTCOLOR
                ) }
            }

        }
    }

    companion object {
        fun updateMountText(context: Context) {
            State.viewModel.mountText.update { if (State.isWindowsMounted) context.getString(
                R.string.mnt_title,
                context.getString(R.string.unmountt)
            ) else context.getString(R.string.mnt_title, context.getString(R.string.mountt)) }
        }

        fun updateLastBackupDate(context: Context) {
            val formatter = SimpleDateFormat("dd-MM HH:mm", Locale.US)
            val date = context.getString(R.string.last, formatter.format(Date()))
            Preferences.LASTBACKUPDATE.set(date)
            State.viewModel.lastBackupDate.update { date }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstResume) State.viewModel.onResume()
        else isFirstResume = false
    }
}


fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

@Composable
fun sdp(sdp: Int): Dp {
    return dimensionResource(sdp)
}

@Composable
fun ssp(ssp: Int): TextUnit {
    return dimensionResource(ssp).value.sp
}
