package com.venddair.holyhelper.ui.themes.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.R
import com.venddair.holyhelper.Strings
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.MainActivityFunctions
import com.venddair.holyhelper.utils.State

val buttonPadding = PaddingValues(
    start = 0.dp,
    top = 0.dp,
    end = 0.dp,
    bottom = 5.dp
)

@Composable
fun MainTheme() {
    val navController = rememberNavController()
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
        composable("settings") { Settings() }
    }
}

@Composable
fun TopBar(text: String) {
    Row(
        modifier = Modifier
            .background(Color(0xFF404040))
            .fillMaxWidth()
            .padding(20.dp, 0.dp, 20.dp, 5.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "image",
            modifier = Modifier
                .scale(2f)
                .size(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
        )

        Box(
            modifier = Modifier
                .height(dimensionResource(com.intuit.sdp.R.dimen._30sdp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = dimensionResource(com.intuit.ssp.R.dimen._16ssp).value.sp,
                color = colorResource(R.color.white),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
            )
        }

    }
}

@SuppressLint("SwitchIntDef")
@Composable
fun MainMenu(navController: NavController) {
    val viewModel: MainViewModel = viewModel()

    State.viewModel = viewModel

    val context = LocalContext.current

    val configuration = LocalConfiguration.current


    LaunchedEffect(Unit) {
        viewModel.loadData(context)
    }

    Box(
        modifier = Modifier
            .background(Color(0xFF202020))
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

        val isLoading by viewModel.isLoading.observeAsState(true)
        val hadLoaded by viewModel.hadLoaded.observeAsState(false)

        if (isLoading == true && hadLoaded == false)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF404040)),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = "Loading...",
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.white),
                    fontSize = dimensionResource(com.intuit.ssp.R.dimen._15ssp).value.sp,
                    fontWeight = FontWeight.Bold
                )
            }
    }
}

@SuppressLint("StringFormatInvalid")
@Composable
fun Buttons(navController: NavController) {

    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                10.dp,
                dimensionResource(com.intuit.sdp.R.dimen._10sdp),
                10.dp,
                10.dp
            )
    ) {
        val mountText = viewModel.mountText.observeAsState()
        mountText.value?.let {
            State.mountText = it
        }

        val isUefiPresent by viewModel.isUefiFilePresent.observeAsState(false)

        Button(
            modifier = Modifier
                .padding(buttonPadding)
                .weight(1f),
            image = R.drawable.cd,
            title = context.getString(R.string.backup_boot_title),
            subtitle = context.getString(R.string.backup_boot_subtitle),
            onClick = { MainActivityFunctions.backupBoot(context) }
        )

        mountText.value?.let {
            Button(
                modifier = Modifier
                    .padding(buttonPadding)
                    .weight(1f),
                image = R.drawable.folder,
                imageScale = .8f,
                //title = State.mountText,
                title = it,
                subtitle = context.getString(R.string.mnt_subtitle),
                onClick = { MainActivityFunctions.mountWindows(context) }
            )
        }
        Button(
            modifier = Modifier
                .padding(buttonPadding)
                .weight(1f),
            image = R.drawable.toolbox,
            title = context.getString(R.string.toolbox_title),
            subtitle = context.getString(R.string.toolbox_subtitle),
            onClick = { navController.navigate("toolbox") }
        )

        Button(
            modifier = Modifier
                .weight(1f),
            image = R.drawable.ic_launcher_foreground,
            imageScale = 2f,
            disabled = !isUefiPresent,
            title = if (isUefiPresent) context.getString(R.string.quickboot_title) else context.getString(R.string.uefi_not_found),
            subtitle = if (isUefiPresent) context.getString(R.string.quickboot_subtitle) else context.getString(R.string.uefi_not_found_subtitle, Device.get()),
            onClick = { MainActivityFunctions.quickboot(context) }
        )

    }
}

@Composable
fun DeviceImageAndPanelLandscape() {
    Column (
        modifier = Modifier
            .width(dimensionResource(com.intuit.sdp.R.dimen._250sdp))
            .padding(
                dimensionResource(com.intuit.sdp.R.dimen._25sdp),
                dimensionResource(com.intuit.sdp.R.dimen._10sdp),
                dimensionResource(com.intuit.sdp.R.dimen._25sdp),
                0.dp
            )
            //.wrapContentHeight()
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
            .height(dimensionResource(com.intuit.sdp.R.dimen._160sdp))
            .padding(
                dimensionResource(com.intuit.sdp.R.dimen._25sdp),
                dimensionResource(com.intuit.sdp.R.dimen._10sdp),
                dimensionResource(com.intuit.sdp.R.dimen._25sdp),
                0.dp
            )
            //.wrapContentHeight()
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
fun Button(
    modifier: Modifier = Modifier,
   image: Int,
   title: String = "",
   subtitle: String = "",
   imageScale: Float = 1f,
    disabled: Boolean = false,
   onClick: () -> Unit = {}
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
    val clickModifier = if (!disabled)
        Modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
    else
        Modifier
    Box(
        modifier = modifier
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF404040))
            .then(clickModifier)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .scale(imageScale)
                    .width(dimensionResource(id = com.intuit.sdp.R.dimen._40sdp))
                    .fillMaxHeight(),
                painter = painterResource(id = image),
                contentDescription = "button image"
            )
            Column(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._11ssp).value.sp,
                )
                Text(
                    text = subtitle,
                    color = colorResource(id = R.color.white),
                    fontStyle = FontStyle.Italic,
                    fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._9ssp).value.sp
                )
            }
        }
    }
}

@Composable
fun Panel(modifier: Modifier = Modifier) {

    val viewModel: MainViewModel = viewModel()

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(Color(0xFF404040), shape = RoundedCornerShape(16.dp))
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Windows on ARM",
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            0.dp,
                            dimensionResource(com.intuit.sdp.R.dimen._15sdp),
                            0.dp,
                            0.dp
                        ),
                    fontSize = dimensionResource(com.intuit.ssp.R.dimen._13ssp).value.sp
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
                            dimensionResource(com.intuit.sdp.R.dimen._5sdp), dimensionResource(
                                com.intuit.sdp.R.dimen._5sdp
                            ), 0.dp, 0.dp
                        )
                ) {

                    val deviceName = viewModel.deviceName.observeAsState()
                    val panel = viewModel.panelType.observeAsState()
                    val ram = viewModel.totalRam.observeAsState()
                    val lastBackup = viewModel.lastBackupDate.observeAsState()
                    val slot = viewModel.slot.observeAsState()

                    State.lastBackup = lastBackup.value

                    deviceName.value?.let {
                        PanelInfo(it)
                    }
                    panel.value?.let {
                        PanelInfo(it)
                    }
                    ram.value?.let {
                        PanelInfo(it)
                    }
                    State.lastBackup?.let {
                        PanelInfo(it)
                    }
                    slot.value?.let {
                        PanelInfo(it)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(10.dp, 0.dp, 10.dp, 10.dp)
                        .fillMaxWidth()
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
fun MiniButton(text: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFF0F0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(com.intuit.ssp.R.dimen._10ssp).value.sp,
            modifier = Modifier
                .padding(dimensionResource(com.intuit.sdp.R.dimen._7sdp), dimensionResource(com.intuit.sdp.R.dimen._3sdp))
                .clickable { onClick() }
        )
    }
}

@Composable
fun PanelInfo(text: String) {
    Text(
        text = text,
        fontSize = dimensionResource(com.intuit.ssp.R.dimen._9ssp).value.sp,
        color = colorResource(R.color.white)
    )
}

@Composable
fun DeviceImage(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()

    val drawable = viewModel.drawable.observeAsState()

    drawable.value?.let {
        Image(
            painter = painterResource(it),
            contentDescription = "Device Image",
            alignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun Topbar(navController: NavController) {
    Row(
        modifier = Modifier
            .height(dimensionResource(com.intuit.sdp.R.dimen._40sdp))
            .background(Color(0xFF404040))
            .padding(
                dimensionResource(com.intuit.sdp.R.dimen._20sdp),
                0.dp,
                dimensionResource(com.intuit.sdp.R.dimen._20sdp),
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
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
                    .size(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
            )
            Column {
                Text(
                    text = "Holy Helper",
                    color = colorResource(R.color.white),
                    fontSize = dimensionResource(com.intuit.ssp.R.dimen._15ssp).value.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = Strings.version,
                    color = colorResource(R.color.gray),
                    fontSize = dimensionResource(com.intuit.ssp.R.dimen._12ssp).value.sp,
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.settings),
            contentDescription = "settings",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .scale(2f)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { navController.navigate("settings") }
                )
                .width(dimensionResource(com.intuit.sdp.R.dimen._30sdp))
                .height(dimensionResource(com.intuit.sdp.R.dimen._30sdp)),

            )
    }
}