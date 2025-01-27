package com.venddair.holyhelper

import android.content.Context
import com.topjohnwu.superuser.ShellUtils

object Device {

    fun get(): String {
        return ShellUtils.fastCmd("getprop ro.product.device").replace("\n", "")
    }

    fun getPanelType(): String {
        val data = ShellUtils.fastCmd(" su -c cat /proc/cmdline ")
        return if (data.isEmpty()) {
            "Unknown"
        } else {
            when {
                data.contains("j20s_42_02_0b") || data.contains("k82_42") || data.contains("ft8756_huaxing") -> "Huaxing"
                data.contains("j20s_36_02_0a") || data.contains("k82_36") || data.contains("nt36675_tianma") || data.contains("tianma_fhd_nt36672a") -> "Tianma"
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

    fun isDbkpSupported(): Boolean {
        return when (get()) {
            "cepheus", "guacamole", "guacamolet", "pipa", "OnePlus7Pro", "OnePlus7Pro4G", "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "nabu" -> {
                true
            }
            else -> false
        }
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
                "https://github.com/n00b69/woa-op7/releases/download/DBKP/guacamole.fd" to listOf("guacamole.fd", "hotdog")
            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G" ->
                "https://github.com/n00b69/woa-op7/releases/download/DBKP/hotdog.fd" to listOf("hotdog.fd", "hotdog")
            "cepheus" ->
                "https://github.com/n00b69/woa-everything/releases/download/Files/cepheus.fd" to listOf("cepheus.fd", "cepheus")
            "nabu" ->
                "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5/releases/download/1.0/nabu.fd" to listOf("nabu.fd", "nabu")
            "pipa" ->
                "https://github.com/n00b69/woa-everything/releases/download/Files/pipa.fd" to listOf("pipa.fd", "pipa")
            else -> "" to listOf("", "")
        }
    }

    fun getDbkpButton(context: Context): String {
        return when (get()) {
            "guacamole", "OnePlus7Pro", "OnePlus7Pro4G", "hotdog", "OnePlus7TPro", "OnePlus7TPro4G" -> context.getString(R.string.op7)
            "cepheus" -> context.getString(R.string.cepheus)
            "nabu", "pipa" -> context.getString(R.string.nabu)
            else -> ""
        }
    }


    fun getGroupLink(): String {
        return when (get()) {
            "andromeda", "t860", "t865", "q2q", "winnerx", "winner" -> "https://t.me/project_aloha_issues"
            "alphalm", "alphaplus", "alpha_lao_com", "alphalm_lao_com", "alphaplus_lao_com",
            "betaplus", "betalm", "beta_lao_com", "betaplus_lao_com", "betalm_lao_com",
            "flashlmdd", "flash_lao_com", "flashlm", "flashlmdd_lao_com", "mh2lm", "mh2plus",
            "mh2plus_lao_com", "mh2lm_lao_com", "mh2lm5g", "mh2lm5g_lao_com", "bhima", "vayu",
            "G973F", "SM-G973F", "beyond1lte", "beyond1qlte", "G973U", "G973U1", "SM-G973U",
            "SM-G973U1", "G9730", "SM-G9730", "G973N", "SM-G973N", "G973X", "SM-G973X",
            "G973C", "SM-G973C", "SCV41", "SM-SC41", "beyond1", "xpeng" -> "https://t.me/woahelperchat"
            "beryllium" -> "https://t.me/WinOnF1"
            "cepheus" -> "http://t.me/woacepheus"
            "cheeseburger", "chiron", "dumpling", "sagit" -> "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA"
            "curtana", "curtana2", "curtana_india", "curtana_cn", "curtanacn", "durandal",
            "durandal_india", "excalibur", "excalibur2", "excalibur_india", "gram", "joyeuse",
            "miatoll" -> "http://t.me/woamiatoll"
            "dipper" -> "https://t.me/woadipper"
            "equuleus", "ursa" -> "https://t.me/woaequuleus"
            "lisa" -> "https://t.me/woalisa"
            "nabu" -> "https://t.me/nabuwoa"
            "perseus" -> "https://t.me/woaperseus"
            "pipa" -> "https://t.me/xiaomi_pipa"
            "polaris" -> "https://t.me/WinOnMIX2S"
            "Pong", "pong" -> "https://t.me/WoA_spacewar_pong"
            "raphael", "raphaelin", "raphaels" -> "https://t.me/woaraphael"
            "surya" -> "https://t.me/windows_on_pocox3_nfc"
            "a52sxq" -> "https://t.me/a52sxq_uefi"
            "judyln", "judyp", "judypn", "joan" -> "https://t.me/lgedevices"
            "OnePlus6", "fajita", "OnePlus6T", "enchilada" -> "https://t.me/WinOnOP6"
            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "guacamole", "guacamolet",
            "OnePlus7Pro", "OnePlus7Pro4G", "guacamoleb", "hotdogb", "OnePlus7T", "OnePlus7",
            "OnePlus7TPro5G", "OnePlus7TProNR", "hotdogg", "OP7ProNRSpr" -> "https://t.me/onepluswoachat"
            "davinci" -> "https://t.me/woa_davinci"
            "marble" -> "https://t.me/woa_marble"
            "venus", "alioth", "ingres", "vili", "lavender", "star2qlte", "star2qltechn", "r3q" -> "https://discord.gg/Dx2QgMx7Sv"
            "RMX2061", "RMX2170" -> "https://t.me/realme6PROwindowsARM64"
            "cmi", "houji", "meizu20pro", "husky", "redfin", "dm3q", "dm3" -> "https://t.me/dumanthecat"
            "e3q" -> "https://t.me/biskupmuf"
            else -> "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA"
        }
    }

    fun getGuideLink(): String {
        return when (get()) {
            "alphalm", "alphaplus", "alpha_lao_com", "alphalm_lao_com", "alphaplus_lao_com" -> "https://github.com/n00b69/woa-alphaplus"
            "betaplus", "betalm", "beta_lao_com", "betaplus_lao_com", "betalm_lao_com" -> "https://github.com/n00b69/woa-betalm"
            "flashlmdd", "flash_lao_com", "flashlm", "flashlmdd_lao_com" -> "https://github.com/n00b69/woa-flashlmdd"
            "mh2lm", "mh2plus", "mh2plus_lao_com", "mh2lm_lao_com" -> "https://github.com/n00b69/woa-mh2lm"
            "mh2lm5g", "mh2lm5g_lao_com" -> "https://github.com/n00b69/woa-mh2lm5g"
            "beryllium" -> "https://github.com/n00b69/woa-beryllium"
            "bhima", "vayu" -> "https://github.com/woa-vayu/POCOX3Pro-Guides"
            "cepheus" -> "https://github.com/ivnvrvnn/Port-Windows-XiaoMI-9"
            "chiron", "cheeseburger", "dumpling" -> "https://renegade-project.tech/"
            "curtana", "curtana2", "curtana_india", "curtana_cn", "curtanacn",
            "durandal", "durandal_india", "excalibur", "excalibur2", "excalibur_india",
            "gram", "joyeuse", "miatoll" -> "https://github.com/woa-miatoll/Port-Windows-11-Redmi-Note-9-Pro"
            "dipper" -> "https://github.com/n00b69/woa-dipper"
            "equuleus", "ursa" -> "https://github.com/n00b69/woa-equuleus"
            "G973F", "SM-G973F", "beyond1lte", "beyond1qlte", "G973U", "G973U1",
            "SM-G973U", "SM-G973U1", "G9730", "SM-G9730", "G973N", "SM-G973N",
            "G973X", "SM-G973X", "G973C", "SM-G973C", "SCV41", "SM-SC41", "beyond1" -> "https://github.com/sonic011gamer/Mu-Samsung"
            "lisa" -> "https://github.com/ETCHDEV/Port-Windows-11-Xiaomi-11-Lite-NE"
            "nabu" -> "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5"
            "perseus" -> "https://github.com/n00b69/woa-perseus"
            "pipa" -> "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md"
            "polaris" -> "https://github.com/n00b69/woa-polaris"
            "Pong", "pong" -> "https://github.com/index986/woa-pong"
            "raphael", "raphaelin", "raphaels" -> "https://github.com/new-WoA-Raphael/woa-raphael"
            "surya" -> "https://github.com/woa-surya/POCOX3NFC-Guides"
            "a52sxq" -> "https://github.com/n00b69/woa-a52s"
            "judyln", "judyp", "judypn" -> "https://github.com/n00b69/woa-everything"
            "joan" -> "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md"
            "OnePlus6", "fajita", "OnePlus6T", "enchilada" -> "https://github.com/n00b69/woa-op6"
            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "guacamole", "guacamolet",
            "OnePlus7Pro", "OnePlus7Pro4G" -> "https://github.com/n00b69/woa-op7"
            "guacamoleb", "hotdogb", "OnePlus7T", "OnePlus7", "OnePlus7TPro5G",
            "OnePlus7TProNR", "hotdogg", "OP7ProNRSpr", "t860", "t865", "q2q", "andromeda" -> "https://project-aloha.github.io/"
            "sagit" -> "https://renegade-project.tech/"
            "winnerx", "winner" -> "https://github.com/n00b69/woa-winner"
            "venus", "alioth", "ingres", "vili", "lavender", "xpeng", "RMX2061",
            "RMX2170", "cmi", "houji", "meizu20pro", "husky", "redfin", "e3q",
            "dm3q", "dm3" -> "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md"
            "davinci" -> "https://github.com/zxcwsurx/woa-davinci"
            "marble" -> "https://github.com/Xhdsos/woa-marble"
            else -> "https://renegade-project.tech/"
        }
    }
}