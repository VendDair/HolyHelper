package com.venddair.holyhelper.ui.theme

import android.R
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.context
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
object BaseColors {
    val color = "#FF404040"
    val textColor = "#FFFFFFFF"
    val guideGroupColor = "#FF382076"
}
fun generateAppColors(): AppColors {

    val isMaterialYou = Preferences.MATERIALYOU.get() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isColorBasedOnDefault = Preferences.COLORSBASEDONDEFAULT.get()

    val defaultColor = Color(BaseColors.color.toColorInt())
    val defaultTextColor = Color(BaseColors.textColor.toColorInt())

    val factor = 0.15f

    val baseColor =
        if (isMaterialYou)
            Color(context.getColor(R.color.system_accent1_200))
        else if (isColorBasedOnDefault)
            Color(Preferences.COLOR.get().toColorInt())
        else
            Color(BaseColors.color.toColorInt())

    val textColor =
        if (isMaterialYou)
            Color(context.getColor(R.color.system_accent3_10))
        else if (isColorBasedOnDefault)
            Color(Preferences.TEXTCOLOR.get().toColorInt())
        else
            Color(BaseColors.textColor.toColorInt())


    val guideGroupColor =
        if (isMaterialYou)
            Color(context.getColor(R.color.system_accent1_800))
        else if (isColorBasedOnDefault)
            Color(Preferences.GUIDEGROUPCOLOR.get().toColorInt())
        else
            Color(BaseColors.guideGroupColor.toColorInt())

    if (isMaterialYou)
        return AppColors(
            primary = guideGroupColor,
            secondary = lerp(defaultColor, baseColor, factor),
            background = lerp(defaultColor, baseColor.adjustBrightness(.2f), .5f),
            surface = lerp(defaultColor, baseColor, .1f),
            accent = Color(context.getColor(R.color.system_accent2_500)),
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