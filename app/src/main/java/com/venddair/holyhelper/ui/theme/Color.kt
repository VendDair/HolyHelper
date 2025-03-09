package com.venddair.holyhelper.ui.theme

import android.R
import android.graphics.Color.parseColor
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.navigation.serialization.generateRouteWithArgs
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import android.graphics.Color as AndroidColor

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class AppColors(
    val primary: Color,      // Main color (your base color)
    val secondary: Color,    // Lighter shade for highlights
    val background: Color,   // Dark shade for backgrounds
    val surface: Color,      // Slightly darker shade for cards or surfaces
    val accent: Color,       // Very light shade for contrast
    val text: Color,
    val textSecondary: Color,
)

/*fun adjustBrightness(color: Color, factor: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)
    return Color(AndroidColor.HSVToColor(hsv))
}

fun changeHue(color: Color, hueShift: Float): Color {
    val hsv = FloatArray(3)

    AndroidColor.colorToHSV(color.toArgb(), hsv)

    hsv[0] = ((hsv[0] + hueShift) % 360 + 360) % 360

    return Color(AndroidColor.HSVToColor(hsv))
}*/

//@Composable
fun generateAppColors(): AppColors {

    val isMaterialYou = Preferences.MATERIALYOU.get() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isColorBasedOnDefault = Preferences.COLORSBASEDONDEFAULT.get()

    val defaultColor = Color(State.BaseColors.color.toColorInt())
    val defaultTextColor = Color(State.BaseColors.textColor.toColorInt())

    val factor = 0.15f

    val baseColor =
        if (isMaterialYou)
            Color(State.context.getColor(R.color.system_accent1_200))
        else if (isColorBasedOnDefault)
            Color(Preferences.COLOR.get().toColorInt())
        else
            Color(State.BaseColors.color.toColorInt())

    val textColor =
        if (isMaterialYou)
            Color(State.context.getColor(R.color.system_accent3_10))
        else if (isColorBasedOnDefault)
            Color(Preferences.TEXTCOLOR.get().toColorInt())
        else
            Color(State.BaseColors.textColor.toColorInt())


    val guideGroupColor =
        if (isMaterialYou)
            Color(State.context.getColor(R.color.system_accent1_800))
        else if (isColorBasedOnDefault)
            Color(Preferences.GUIDEGROUPCOLOR.get().toColorInt())
        else
            Color(State.BaseColors.guideGroupColor.toColorInt())

    if (isMaterialYou)
        return AppColors(
            primary = guideGroupColor,
            secondary = lerp(defaultColor, baseColor, factor),
            background = lerp(defaultColor, baseColor.adjustBrightness(.2f), .5f),
            surface = lerp(defaultColor, baseColor, .1f),
            accent = Color(State.context.getColor(R.color.system_accent2_500)),
            text = textColor,
            textSecondary = textColor.adjustBrightness(.5f)
        )

    if (isColorBasedOnDefault)
        return AppColors(
            primary = guideGroupColor,
            secondary = lerp(defaultColor, baseColor, factor),
            background = lerp(defaultColor, baseColor.adjustBrightness(.2f), .05f),
            surface = lerp(defaultColor, baseColor, .1f),
            accent = lerp(defaultColor, baseColor.changeHue(10f).adjustBrightness(1.5f), factor),
            text = lerp(defaultTextColor, textColor, factor + .2f),
            textSecondary = lerp(defaultTextColor, textColor.adjustBrightness(.4f), factor + .2f)
        )

    return AppColors(
        primary = guideGroupColor,
        secondary = baseColor,
        background = baseColor.adjustBrightness(.5f),
        surface = baseColor,
        accent = baseColor.changeHue(10f).adjustBrightness(1.5f),
        text = textColor,
        textSecondary = textColor.adjustBrightness(.4f)
    )

}

/*@Composable
fun generateAppColors(): AppColors {
    // Retrieve colors from preferences
    val baseColor = Color(Preferences.COLOR.get().toColorInt())
    val textColor = Color(Preferences.TEXTCOLOR.get().toColorInt())
    val guideGroupColor = Color(Preferences.GUIDEGROUPCOLOR.get().toColorInt())

    val defaultBase = Color(State.BaseColors.color.toColorInt())
    val defaultText = Color(State.BaseColors.textColor.toColorInt())
    val defaultGuideGroupColor = Color(State.BaseColors.guideGroupColor.toColorInt())

    val factor = 0.15f
    val textFactor = factor + 0.2f

    // Determine base colors
    var selectedBaseColor = baseColor
    var selectedTextColor = textColor
    var selectedGuideGroupColor = guideGroupColor

    // If Material You is enabled and supported
    if (Preferences.MATERIALYOU.get() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        selectedBaseColor = colorResource(android.R.color.system_accent1_200)
        selectedTextColor = colorResource(android.R.color.system_accent2_500)
        selectedGuideGroupColor = colorResource(android.R.color.system_accent1_800)

        // Adjust colors to match default theme
        selectedBaseColor = lerp(defaultBase, selectedBaseColor, 0.05f)
        selectedTextColor = lerp(defaultText, selectedTextColor, textFactor)
    }

    // Determine whether to use default-based colors
    if (Preferences.COLORSBASEDONDEFAULT.get()) {
        selectedBaseColor = defaultBase
        selectedTextColor = defaultText
        selectedGuideGroupColor = defaultGuideGroupColor
    }

    // Calculate the final app colors
    return calculateAppColors(
        baseColor = selectedBaseColor,
        defaultBase = defaultBase,
        text = selectedTextColor,
        defaultText = defaultText,
        groupGuide = selectedGuideGroupColor,
        factor = factor,
        textFactor = textFactor
    )
}

// Helper function to calculate app colors
private fun calculateAppColors(
    baseColor: Color,
    defaultBase: Color,
    text: Color,
    defaultText: Color,
    groupGuide: Color,
    factor: Float,
    textFactor: Float
): AppColors {
    return AppColors(
        primary = groupGuide,
        secondary = lerp(defaultBase, baseColor, factor),
        background = lerp(defaultBase, baseColor.adjustBrightness(.2f), 0.05f),
        surface = lerp(defaultBase, baseColor, 0.1f),
        accent = lerp(defaultBase, calculateAccentColor(baseColor), factor),
        text = lerp(defaultText, text, textFactor),
        textSecondary = lerp(defaultText, baseColor.adjustBrightness(.4f), textFactor)
    )
}

// Helper function to calculate accent color
private fun calculateAccentColor(baseColor: Color): Color {
    return baseColor.changeHue(10f).adjustBrightness(1.5f)
}*/

fun Color.adjustBrightness(factor: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(this.toArgb(), hsv)
    hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)
    return Color(AndroidColor.HSVToColor(hsv))
}

fun Color.changeHue(degrees: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(this.toArgb(), hsv)
    hsv[0] = ((hsv[0] + degrees) % 360 + 360) % 360
    return Color(AndroidColor.HSVToColor(hsv))
}