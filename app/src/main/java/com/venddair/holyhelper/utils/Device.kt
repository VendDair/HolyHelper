package com.venddair.holyhelper.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.topjohnwu.superuser.ShellUtils
import com.venddair.holyhelper.R

data class DeviceConfig(
    val codenames: List<String>,
    val groupLink: String,
    val guideLink: String,
    @DrawableRes val imageResId: Int,
    val isPanel: Boolean = false,
    val isDbkp: Boolean = false,
    val isDumpModem: Boolean = false,
    val isDevCfg: Boolean = false,
)

object DeviceConfigProvider {

    private val defaultConfig = DeviceConfig(
        codenames = emptyList(),
        groupLink = "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA",
        guideLink = "https://renegade-project.tech/",
        imageResId = R.drawable.unknown,
        isDumpModem = true
    )

    // List of configurations for supported devices.
    private val configs = listOf(
        DeviceConfig(
            codenames = listOf("alphalm", "alphaplus", "alphalm_lao_com", "alphaplus_lao_com"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/n00b69/woa-alphaplus",
            imageResId = R.drawable.alphaplus

        ),
        DeviceConfig(
            codenames = listOf("betalm", "betalm_lao_com"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/n00b69/woa-betalm",
            imageResId = R.drawable.betalm
        ),
        DeviceConfig(
            codenames = listOf("flashlmdd", "flashlm", "flashlm_lao_com", "flashlmdd_lao_com"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/n00b69/woa-flashlmdd",
            imageResId = R.drawable.flashlmdd
        ),
        DeviceConfig(
            codenames = listOf("mh2lm", "mh2lm_lao_com"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/n00b69/woa-mh2lm",
            imageResId = R.drawable.mh2lm
        ),
        DeviceConfig(
            codenames = listOf("mh2lm5g", "mh2lm5g_lao_com"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/n00b69/woa-mh2lm5g",
            imageResId = R.drawable.mh2lm
        ),
        DeviceConfig(
            codenames = listOf("judyln", "judyp", "judypn"),
            groupLink = "https://t.me/lgedevices",
            guideLink = "https://github.com/n00b69/woa-everything",
            imageResId = R.drawable.unknown
        ),
        DeviceConfig(
            codenames = listOf("joan"),
            groupLink = "https://t.me/lgedevices",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.unknown
        ),
        DeviceConfig(
            codenames = listOf("andromeda"),
            groupLink = "https://t.me/project_aloha_issues",
            guideLink = "https://project-aloha.github.io/",
            imageResId = R.drawable.unknown
        ),
        DeviceConfig(
            codenames = listOf("beryllium"),
            groupLink = "https://t.me/WinOnF1",
            guideLink = "https://github.com/n00b69/woa-beryllium",
            imageResId = R.drawable.beryllium
        ),
        DeviceConfig(
            codenames = listOf("bhima", "vayu"),
            groupLink = "https://t.me/winonvayualt",
            guideLink = "https://github.com/woa-vayu/POCOX3Pro-Guides",
            imageResId = R.drawable.vayu
        ),
        DeviceConfig(
            codenames = listOf("cepheus"),
            groupLink = "http://t.me/woacepheus",
            guideLink = "https://github.com/ivnvrvnn/Port-Windows-XiaoMI-9",
            imageResId = R.drawable.cepheus,
            isPanel = true,
            isDbkp = true,
            isDumpModem = true
        ),
        DeviceConfig(
            codenames = listOf("chiron"),
            groupLink = "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA",
            guideLink = "https://renegade-project.tech/",
            imageResId = R.drawable.chiron
        ),
        DeviceConfig(
            codenames = listOf("curtana", "curtana2", "curtana_india", "curtana_cn", "curtanacn", "durandal", "durandal_india", "excalibur", "excalibur2", "excalibur_india", "gram", "joyeuse", "miatoll"),
            groupLink = "http://t.me/woamiatoll",
            guideLink = "https://github.com/woa-miatoll/Port-Windows-11-Redmi-Note-9-Pro",
            imageResId = R.drawable.miatoll,
            isPanel = true
        ),
        DeviceConfig(
            codenames = listOf("dipper"),
            groupLink = "https://t.me/woadipper",
            guideLink = "https://github.com/n00b69/woa-dipper",
            imageResId = R.drawable.dipper
        ),
        DeviceConfig(
            codenames = listOf("equuleus", "ursa"),
            groupLink = "https://t.me/woaequuleus",
            guideLink = "https://github.com/n00b69/woa-equuleus",
            imageResId = R.drawable.equuleus
        ),
        DeviceConfig(
            codenames = listOf("lisa"),
            groupLink = "https://t.me/woalisa",
            guideLink = "https://github.com/n00b69/woa-lisa",
            imageResId = R.drawable.lisa
        ),
        DeviceConfig(
            codenames = listOf("nabu"),
            groupLink = "https://t.me/nabuwoa",
            guideLink = "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5",
            imageResId = R.drawable.nabu,
            isPanel = true,
            isDbkp = true,
        ),
        DeviceConfig(
            codenames = listOf("perseus"),
            groupLink = "https://t.me/woaperseus",
            guideLink = "https://github.com/n00b69/woa-perseus",
            imageResId = R.drawable.perseus
        ),
        DeviceConfig(
            codenames = listOf("pipa"),
            groupLink = "https://t.me/xiaomi_pipa",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.pipa,
            isPanel = true,
            isDbkp = true
        ),
        DeviceConfig(
            codenames = listOf("polaris"),
            groupLink = "https://t.me/WinOnMIX2S",
            guideLink = "https://github.com/n00b69/woa-polaris",
            imageResId = R.drawable.polaris
        ),
        DeviceConfig(
            codenames = listOf("raphael", "raphaelin", "raphaels"),
            groupLink = "https://t.me/woaraphael",
            guideLink = "https://github.com/new-WoA-Raphael/woa-raphael",
            imageResId = R.drawable.raphael
        ),
        DeviceConfig(
            codenames = listOf("surya"),
            groupLink = "https://t.me/windows_on_pocox3_nfc",
            guideLink = "https://github.com/woa-surya/POCOX3NFC-Guides",
            imageResId = R.drawable.vayu,
            isPanel = true
        ),
        DeviceConfig(
            codenames = listOf("sagit"),
            groupLink = "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA",
            guideLink = "https://renegade-project.tech/",
            imageResId = R.drawable.unknown
        ),
        DeviceConfig(
            codenames = listOf("ingres"),
            groupLink = "https://discord.gg/Dx2QgMx7Sv",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.ingres
        ),
        DeviceConfig(
            codenames = listOf("vili", "lavender"),
            groupLink = "https://discord.gg/Dx2QgMx7Sv",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.unknown
        ),
        DeviceConfig(
            codenames = listOf("OnePlus5", "cheeseburger"),
            groupLink = "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA",
            guideLink = "https://renegade-project.tech/",
            imageResId = R.drawable.cheeseburger
        ),
        DeviceConfig(
            codenames = listOf("OnePlus5T", "dumpling"),
            groupLink = "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA",
            guideLink = "https://renegade-project.tech/",
            imageResId = R.drawable.dumpling
        ),
        DeviceConfig(
            codenames = listOf("OnePlus6", "fajita"),
            groupLink = "https://t.me/WinOnOP6",
            guideLink = "https://github.com/n00b69/woa-op6",
            imageResId = R.drawable.fajita
        ),
        DeviceConfig(
            codenames = listOf("OnePlus6T", "OnePlus6TSingle", "enchilada"),
            groupLink = "https://t.me/WinOnOP6",
            guideLink = "https://github.com/n00b69/woa-op6",
            imageResId = R.drawable.enchilada
        ),
        DeviceConfig(
            codenames = listOf("hotdog", "OnePlus7TPro", "OnePlus7TPro4G"),
            groupLink = "https://t.me/onepluswoachat",
            guideLink = "https://github.com/n00b69/woa-op7",
            imageResId = R.drawable.hotdog,
            isDumpModem = true,
            isDbkp = true,
            isDevCfg = true,
        ),
        DeviceConfig(
            codenames = listOf("guacamole", "guacamolet", "OnePlus7Pro", "OnePlus7Pro4G", "OnePlus7ProTMO"),
            groupLink = "https://t.me/onepluswoachat",
            guideLink = "https://github.com/n00b69/woa-op7",
            imageResId = R.drawable.guacamole,
            isDumpModem = true,
            isDbkp = true,
            isDevCfg = true,
        ),
        DeviceConfig(
            codenames = listOf("guacamoleb", "hotdogb", "OnePlus7T", "OnePlus7"),
            groupLink = "https://t.me/onepluswoachat",
            guideLink = "https://project-aloha.github.io/",
            imageResId = R.drawable.unknown,
            isDumpModem = true
        ),
        DeviceConfig(
            codenames = listOf("OnePlus7TPro5G", "OnePlus7TProNR", "hotdogg"),
            groupLink = "https://t.me/onepluswoachat",
            guideLink = "https://project-aloha.github.io/",
            imageResId = R.drawable.hotdog
        ),
        DeviceConfig(
            codenames = listOf("OP7ProNRSpr", "OnePlus7ProNR", "guacamoleg", "guacamoles"),
            groupLink = "https://t.me/onepluswoachat",
            guideLink = "https://project-aloha.github.io/",
            imageResId = R.drawable.guacamole
        ),
        DeviceConfig(
            codenames = listOf("a52sxq"),
            groupLink = "https://t.me/a52sxq_uefi",
            guideLink = "https://github.com/n00b69/woa-a52s",
            imageResId = R.drawable.a52sxq
        ),
        DeviceConfig(
            codenames = listOf("beyond1lte", "beyond1qlte", "beyond1"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/sonic011gamer/Mu-Samsung",
            imageResId = R.drawable.beyond1
        ),
        DeviceConfig(
            codenames = listOf("dm3q", "dm3"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.dm3q
        ),
        DeviceConfig(
            codenames = listOf("e3q"),
            groupLink = "https://t.me/biskupmuf",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.e3q
        ),
        DeviceConfig(
            codenames = listOf("gts6l", "gts6lwifi"),
            groupLink = "https://t.me/project_aloha_issues",
            guideLink = "https://project-aloha.github.io/",
            imageResId = R.drawable.gts6l
        ),
        DeviceConfig(
            codenames = listOf("q2q"),
            groupLink = "https://t.me/project_aloha_issues",
            guideLink = "https://project-aloha.github.io/",
            imageResId = R.drawable.q2q
        ),
        DeviceConfig(
            codenames = listOf("star2qlte", "star2qltechn", "r3q"),
            groupLink = "https://discord.gg/Dx2QgMx7Sv",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.unknown
        ),
        DeviceConfig(
            codenames = listOf("winnerx", "winner"),
            groupLink = "https://t.me/project_aloha_issues",
            guideLink = "https://t.me/project_aloha_issues",
            imageResId = R.drawable.winner
        ),
        DeviceConfig(
            codenames = listOf("venus"),
            groupLink = "https://discord.gg/Dx2QgMx7Sv",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.venus
        ),
        DeviceConfig(
            codenames = listOf("alioth"),
            groupLink = "https://discord.gg/Dx2QgMx7Sv",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.alioth
        ),
        DeviceConfig(
            codenames = listOf("davinci"),
            groupLink = "https://t.me/woa_davinci",
            guideLink = "https://github.com/zxcwsurx/woa-davinci",
            imageResId = R.drawable.raphael
        ),
        DeviceConfig(
            codenames = listOf("marble"),
            groupLink = "https://t.me/woa_marble",
            guideLink = "https://github.com/Xhdsos/woa-marble",
            imageResId = R.drawable.marble
        ),
        DeviceConfig(
            codenames = listOf("Pong", "pong"),
            groupLink = "https://t.me/WoA_spacewar_pong",
            guideLink = "https://github.com/index986/woa-pong",
            imageResId = R.drawable.pong
        ),
        DeviceConfig(
            codenames = listOf("xpeng"),
            groupLink = "https://t.me/woahelperchat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.xpeng
        ),
        DeviceConfig(
            codenames = listOf("RMX2061"),
            groupLink = "https://t.me/realme6PROwindowsARM64",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.rmx2061
        ),
        DeviceConfig(
            codenames = listOf("RMX2170"),
            groupLink = "https://t.me/realme6PROwindowsARM64",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.rmx2170
        ),
        DeviceConfig(
            codenames = listOf("cmi"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.cmi
        ),
        DeviceConfig(
            codenames = listOf("houji"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.houji
        ),
        DeviceConfig(
            codenames = listOf("meizu20pro", "meizu20Pro"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.meizu20pro
        ),
        DeviceConfig(
            codenames = listOf("husky"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.husky
        ),
        DeviceConfig(
            codenames = listOf("redfin", "herolte"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.redfin
        ),
        DeviceConfig(
            codenames = listOf("haotian"),
            groupLink = "https://t.me/dumanthecat",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.haotian
        ),
        DeviceConfig(
            codenames = listOf("Nord", "nord"),
            groupLink = "https://t.me/dikeckaan",
            guideLink = "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md",
            imageResId = R.drawable.nord
        ),
    )

    /**
     * Returns the configuration for the given device codename.
     * If the device isn’t found, returns a default configuration.
     */
    fun getConfig(codename: String): DeviceConfig {
        // Normalize the codename (e.g. lowercase) to match your configuration keys.
        val normalized = codename.lowercase()
        return configs.firstOrNull { config ->
            config.codenames.any { it.lowercase() == normalized }
        } ?: defaultConfig
    }
}


object Device {
    fun get(): String {
        return ShellUtils.fastCmd("getprop ro.product.device").replace("\n", "")
    }

    fun getModel(): String {
        return ShellUtils.fastCmd("getprop ro.product.model").replace("\n", "")
    }

    fun getPanelType(): String {
        val data = ShellUtils.fastCmd(" su -c cat /proc/cmdline ")
        return if (data.isEmpty()) {
            "Unknown"
        } else {
            when {
                data.contains("j20s_42_02_0b") || data.contains("k82_42") || data.contains("ft8756_huaxing") -> "Huaxing"
                data.contains("j20s_36_02_0a") || data.contains("k82_36") || data.contains("nt36675_tianma") || data.contains(
                    "tianma_fhd_nt36672a"
                ) -> "Tianma"

                data.contains("ebbg_fhd_ft8719") -> "EBBG"
                data.contains("fhd_ea8076_global") -> "global"
                data.contains("fhd_ea8076_f1mp_cmd") -> "f1mp"
                data.contains("fhd_ea8076_f1p2_cmd") -> "f1p2"
                data.contains("fhd_ea8076_f1p2_2") -> "f1p2_2"
                data.contains("fhd_ea8076_f1_cmd") -> "f1"
                data.contains("fhd_ea8076_cmd") -> "ea8076_cmd"
                else -> ShellUtils.fastCmd("su -c cat /proc/cmdline | tr ' :=' '\n'|grep dsi|tr ' _' '\n'|tail -3|head -1")
            }
        }
    }

    fun getTotalRam(context: Context): Float {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memInfo = ActivityManager.MemoryInfo().apply {
            activityManager?.getMemoryInfo(this)
        }

        return memInfo.totalMem.let { bytes ->
            when {
                bytes >= 1L shl 60 -> bytes / (1L shl 60).toFloat() // EB
                bytes >= 1L shl 50 -> bytes / (1L shl 50).toFloat() // PB
                bytes >= 1L shl 40 -> bytes / (1L shl 40).toFloat() // TB
                bytes >= 1L shl 30 -> bytes / (1L shl 30).toFloat() // GB
                bytes >= 1L shl 20 -> bytes / (1L shl 20).toFloat() // MB
                bytes >= 1L shl 10 -> bytes / (1L shl 10).toFloat() // KB
                else -> bytes.toFloat() // Bytes
            }
        }
    }

    fun getSlot(): String? {
        val slot = ShellUtils.fastCmd("getprop ro.boot.slot_suffix")
        if (slot == "") return null
        return slot.replace("_", "").uppercase()
    }

    fun getDbkpDeviceName(): String {
        return when (get()) {
            "guacamole", "guacamolet", "OnePlus7Pro", "OnePlus7Pro4G", "OnePlus7ProTMO" -> "ONEPLUS 7 PRO"
            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G" -> "ONEPLUS 7T PRO"
            "cepheus" -> "XIAOMI MI 9"
            "nabu" -> "XIAOMI PAD 5"
            "pipa" -> "XIAOMI PAD 6"
            else -> "UNSUPPORTED"
        }
    }

    fun getDbkpDownloadInfo(): Pair<String, List<String>> {
        return when (get()) {
            "guacamole", "OnePlus7Pro", "OnePlus7Pro4G" ->
                "https://github.com/n00b69/woa-op7/releases/download/DBKP/guacamole.fd" to listOf(
                    "guacamole.fd",
                    "hotdog"
                )

            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G" ->
                "https://github.com/n00b69/woa-op7/releases/download/DBKP/hotdog.fd" to listOf(
                    "hotdog.fd",
                    "hotdog"
                )

            "cepheus" ->
                "https://github.com/n00b69/woa-everything/releases/download/Files/cepheus.fd" to listOf(
                    "cepheus.fd",
                    "cepheus"
                )

            "nabu" ->
                "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5/releases/download/1.0/nabu.fd" to listOf(
                    "nabu.fd",
                    "nabu"
                )

            "pipa" ->
                "https://github.com/n00b69/woa-everything/releases/download/Files/pipa.fd" to listOf(
                    "pipa.fd",
                    "pipa"
                )

            else -> "" to listOf("", "")
        }
    }

    fun getDbkpButton(context: Context): String {
        return when (get()) {
            "guacamole", "OnePlus7Pro", "OnePlus7Pro4G", "hotdog", "OnePlus7TPro", "OnePlus7TPro4G" -> context.getString(
                R.string.op7
            )

            "cepheus" -> context.getString(R.string.cepheus)
            "nabu", "pipa" -> context.getString(R.string.nabu)
            else -> ""
        }
    }

    val isLandscape: Boolean
        @Composable
        get() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val isPortrait: Boolean
        @Composable
        get() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    val isLocked: Boolean
        get() = (context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceLocked

    val isRestricted: Boolean
        get() = when (get()) {
            "duo", "duo2", "duoeu", "duoatt", "a0", "b1", "c1", "c2" -> true
            else -> false
        }
}