package com.venddair.holyhelper.ui.themes.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.intuit.sdp.R.dimen.*
import com.intuit.ssp.R.dimen.*
import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.activities.sdp
import com.venddair.holyhelper.activities.ssp
import com.venddair.holyhelper.ui.theme.generateAppColors
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import com.venddair.holyhelper.utils.Update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val buttonPadding: PaddingValues
    @Composable
    get() = PaddingValues(
        start = 0.dp,
        top = 0.dp,
        end = 0.dp,
        bottom = sdp(_5sdp)
    )

val menuPadding: PaddingValues
    @Composable
    get() = PaddingValues(sdp(_8sdp))

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

@Composable
fun MainTheme() {

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
        composable("home") { MainMenu(navController) }
        composable("toolbox") { Toolbox() }
        composable("settings") { Settings(navController) }
        composable("color_options") { ColorOptions(navController) }
        composable("theme_options") { ThemeSelector() }
        composable("main_color") { MainColorChanger() }
        composable("text_color") { TextColorChanger() }
    }
}

@Composable
fun TopBar(text: String) {
    Row(
        modifier = Modifier
            .background(State.Colors.surface)
            .fillMaxWidth()
            .padding(sdp(_16sdp), 0.dp, sdp(_16sdp), sdp(_5sdp))
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "image",
            modifier = Modifier
                .scale(2f)
                .size(sdp(_30sdp))
        )

        Box(
            modifier = Modifier
                .height(sdp(_30sdp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = ssp(_16ssp),
                //color = colorResource(R.color.white),
                color = State.Colors.text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(sdp(_10sdp), 0.dp, 0.dp, 0.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SwitchIntDef")
@Composable
fun MainMenu(navController: NavController) {
    val viewModel: MainViewModel = viewModel()

    State.viewModel = viewModel

    val context = LocalContext.current as ComponentActivity

    val configuration = LocalConfiguration.current

    val root by viewModel.rootPrivilege.collectAsState()

    if (!State.launch) {
        State.launch = true

        State.Colors = generateAppColors()

        if (!Preferences.EASYTHEME.get())
            CoroutineScope(Dispatchers.Main).launch {
                Update.checkUpdate(context)
            }
        //State.winPartition = Files.getWinPartition(context)

        /*CoroutineScope(Dispatchers.Main).launch {
            State.bootPartition = Files.getBootPartition()
        }*/
        //State.isWindowsMounted = isWindowsMounted()
    }

    LaunchedEffect(Unit) {
        viewModel.loadData(context)

        if (Shell.isAppGrantedRoot() != true)
            Info.noRootDetected(context)

        if (Device.isRestricted())
            Info.appRestricted(context)
    }

    Box(
        modifier = Modifier
            .background(State.Colors.background)
            .blur(State.blurAmount)
    ) {
        Column {
            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    Topbar(navController)

                    DeviceImageAndPanel()

                    Buttons(navController)
                }

                Configuration.ORIENTATION_LANDSCAPE -> {
                    Topbar(navController)
                    Row {
                        DeviceImageAndPanelLandscape()
                        Buttons(navController)
                    }
                }
            }


        }

        context.window.statusBarColor = State.Colors.surface.toArgb()
        context.window.navigationBarColor = State.Colors.background.toArgb()

        val isLoading by viewModel.isLoading.observeAsState(true)
        val hadLoaded by viewModel.hadLoaded.observeAsState(false)

        if (isLoading == true && hadLoaded == false) {
            context.window.navigationBarColor = State.Colors.surface.toArgb()
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
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun Buttons(navController: NavController) {

    val viewModel: MainViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .then(
                if (Preferences.EASYTHEME.get())
                    Modifier.verticalScroll(rememberScrollState())
                else
                    Modifier
            )
            .padding(menuPadding)
    ) {
        val mountText by viewModel.mountText.collectAsState("")
        State.mountText = mountText

        if (Preferences.DEFAULTTHEME.get()) {
            Button(Configs.backupBoot(Modifier.weight(1f)))
            Button(Configs.mount(Modifier.weight(1f)))
            Button(Configs.toolbox(Modifier.weight(1f)))
            Button(Configs.quickboot(Modifier.weight(1f)))
        }
        else if (Preferences.EASYTHEME.get()) {
            Button(Configs.backupBoot(Modifier.height(sdp(_90sdp))))
            Button(Configs.mount(Modifier.height(sdp(_90sdp))))
            Button(Configs.sta(Modifier
                .height(sdp(_90sdp))
                .padding(buttonPadding)))
            Button(Configs.usbHost(Modifier
                .height(sdp(_90sdp))
                .padding(buttonPadding)))
            Button(Configs.quickboot(Modifier.height(sdp(_90sdp))))
        }
    }
}

@Composable
fun DeviceImageAndPanelLandscape() {
    Column (
        modifier = Modifier
            .width(sdp(_250sdp))
            .padding(
                sdp(_25sdp),
                sdp(_10sdp),
                sdp(_25sdp),
                0.dp
            )
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        DeviceImage(
            modifier = Modifier
                .weight(0.8f)
        )
        Spacer(
            modifier = Modifier
                .weight(0.05f)
        )
        Panel(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun DeviceImageAndPanel() {
    Row(
        modifier = Modifier
            .height(sdp(_160sdp))
            .padding(
                sdp(_25sdp),
                sdp(_10sdp),
                sdp(_25sdp),
                0.dp
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DeviceImage(
            modifier = Modifier
                .weight(0.8f)
        )
        Spacer(
            modifier = Modifier
                .weight(0.05f)
        )
        Panel(
            modifier = Modifier
                .weight(1f)
        )
    }
}


@Composable
fun Button(config: ButtonConfig) {
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

/*@Composable
fun Button(
    config: ButtonConfig
) {
    val scale = remember { Animatable(1f) }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    scale.animateTo(0.9f, animationSpec = tween(100))
                }
                is PressInteraction.Release -> {
                    scale.animateTo(1f, animationSpec = tween(100))
                }
                is PressInteraction.Cancel -> {
                    scale.animateTo(1f, animationSpec = tween(100))
                }
            }
        }
    }
    val clickModifier = if (!config.disabled)
        Modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = config.onClick
        )
    else
        Modifier
    Box(
        modifier = config.modifier
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clip(RoundedCornerShape(sdp(_8sdp)))
            .background(State.Colors.surface)
            .then(clickModifier)
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
}*/

@Composable
fun Panel(modifier: Modifier = Modifier) {

    val viewModel: MainViewModel = viewModel()

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(State.Colors.surface, shape = RoundedCornerShape(sdp(_10sdp)))
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Windows on ARM",
                    color = State.Colors.text,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            0.dp,
                            sdp(_15sdp),
                            0.dp,
                            0.dp
                        ),
                    fontSize = ssp(_13ssp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            sdp(_5sdp), sdp(_5sdp), 0.dp, 0.dp
                        )
                ) {

                    val deviceName by viewModel.deviceName.collectAsState()
                    val panel by viewModel.panelType.collectAsState()
                    val ram by viewModel.totalRam.collectAsState()
                    val lastBackup by viewModel.lastBackupDate.collectAsState()
                    val slot by viewModel.slot.collectAsState()

                    State.lastBackup = lastBackup

                    deviceName?.let {
                        PanelInfo(it)
                    }
                    panel?.let {
                        PanelInfo(it)
                    }
                    ram?.let {
                        PanelInfo(it)
                    }
                    if (lastBackup == "")
                        PanelInfo(lastBackup)
                    if (!slot.contains("null"))
                        PanelInfo(slot)
                }

                Row(
                    modifier = Modifier
                        .padding(sdp(_6sdp), 0.dp, sdp(_6sdp), sdp(_8sdp))
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    MiniButton(context.getString(R.string.group)) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(State.deviceConfig.groupLink)
                            )
                        )
                    }
                    MiniButton(context.getString(R.string.guide)) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(State.deviceConfig.guideLink)
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    //val bg = if (Preferences.MATERIALYOU.get() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) colorResource(android.R.color.system_accent3_400) else changeHue(adjustBrightness(State.Colors.surface, .7f), -160f)
    Box(
        modifier = modifier
            //.background(changeHue(adjustBrightness(State.Colors.surface, 1.3f), -10f), shape = RoundedCornerShape(sdp(_16sdp)))
            .background(State.Colors.primary, shape = RoundedCornerShape(sdp(_16sdp)))
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = ssp(_10ssp),
            color = State.Colors.text,
            modifier = Modifier
                .padding(sdp(_7sdp), sdp(_3sdp))
                .clickable { onClick() }
        )
    }
}

@Composable
fun PanelInfo(text: String) {
    Text(
        text = text,
        fontSize = ssp(_9ssp),
        color = State.Colors.text
    )
}

@Composable
fun DeviceImage(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()

    val drawable by viewModel.drawable.collectAsState()
    val easterEgg1 by viewModel.easterEgg1.collectAsState()

    Image(
        painter = painterResource(if (!easterEgg1) drawable else R.drawable.redfin),
        contentDescription = "Device Image",
        alignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        Preferences.EASTEREGG1.set(!easterEgg1)
                        viewModel.easterEgg1.update { !easterEgg1 }

                    }
                )
            }
    )
}

@Composable
fun Topbar(navController: NavController) {
    Row(
        modifier = Modifier
            .height(sdp(_40sdp))
            .background(State.Colors.surface)
            .padding(
                sdp(_20sdp),
                0.dp,
                sdp(_20sdp),
                0.dp
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "logo",
                alignment = Alignment.Center,
                modifier = Modifier
                    .scale(2f)
                    .fillMaxHeight()
                    .padding(0.dp, 0.dp, sdp(_10sdp), 0.dp)
                    .size(sdp(_30sdp))
            )
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
                    modifier = Modifier
                        .alpha(.5f),
                    text = Strings.version + if (Strings.test) " (DEV)" else "",
                    color = State.Colors.text,
                    fontSize = ssp(_12ssp),
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(sdp(_10sdp))
        ) {
            if (Strings.test)
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = "refresh",
                    tint = State.Colors.text,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                        .scale(1.2f)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = { State.viewModel.onResume() }
                        )
                        .width(sdp(_30sdp))
                        .height(sdp(_30sdp)),

                    )
            Icon(
                painter = painterResource(R.drawable.ic_gear),
                contentDescription = "settings",
                tint = State.Colors.text,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
                    .scale(1.2f)
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = { navController.navigate("settings") }
                    )
                    .width(sdp(_30sdp))
                    .height(sdp(_30sdp)),

                )
        }
        /*Image(
            //painter = painterResource(R.drawable.settings),
            painter = painterResource(R.drawable.ic_gear),
            contentDescription = "settings",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .scale(1.2f)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { navController.navigate("settings") }
                )
                .width(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
                .height(dimensionResource(com.intuit.sdp.R.dimen._30sdp)),

            )*/
    }
}