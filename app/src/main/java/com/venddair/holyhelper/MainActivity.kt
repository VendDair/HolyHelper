package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.venddair.holyhelper.Permissions.requestInstallPermission

class MainActivity : ComponentActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.main)

        ToastUtil.init(this)
        Files.init(this)
        Preferences.init(this)

        Commands.checkUpdate(this)



        val quickbootButton = findViewById<LinearLayout>(R.id.quickbootButton)
        val backupButton = findViewById<LinearLayout>(R.id.backupButton)
        val deviceImageView = findViewById<ImageView>(R.id.device)
        val mountButton = findViewById<LinearLayout>(R.id.mountButton)
        val mountText = findViewById<TextView>(R.id.mountText)
        val codeNameText = findViewById<TextView>(R.id.codeName)
        val settingsButton = findViewById<ImageView>(R.id.settingsButton)
        val guideButton = findViewById<TextView>(R.id.guideButton)
        val groupButton = findViewById<TextView>(R.id.groupButton)
        val toolboxButton = findViewById<LinearLayout>(R.id.toolboxButton)
        val versionTextView = findViewById<TextView>(R.id.version)

        deviceImageView.setImageDrawable(Files.getResourceFromDevice())
        codeNameText.text = "Device: ${Commands.getDevice()}"

        settingsButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
        toolboxButton.setOnClickListener { startActivity(Intent(this, ToolboxActivity::class.java)) }
        guideButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getGuideLink())))}
        groupButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getGroupLink())))}

        versionTextView.text = Paths.version

        // Check if the app can request package installs
        if (!packageManager.canRequestPackageInstalls()) {
            // Request permission to install unknown apps
            requestInstallPermission(this)
            return
        }

        quickbootButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Boot in Windows?",
                text = "It flashes uefi.img to boot partition",
                buttons = listOf(
                    Pair("Flash") {
                        Commands.bootInWindows(this)
                    },
                    Pair("Reboot") {
                        Commands.bootInWindows(this, true)
                    },
                    Pair("Cancel") {}
                )
            )
        }
        backupButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = "Backup current boot partition?",
                text = "Backups current boot in /sdcard/boot.img or in win folder when its mounted",
                image = R.drawable.cd,
                buttons = listOf(
                    Pair("YES") {
                        Commands.backupBootImage(this)
                    },
                    Pair("NO") {}
                )
            )
        }

        mountText.text = if (Commands.isWindowsMounted(this)) "Unmount Windows" else "Mount Windows"
        mountButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = if (!Commands.isWindowsMounted(this)) "Mount Windows?" else "Unmount Windows?",
                text = "Mounts/Unmounts Windows in ${Files.getMountDir()}",
                image = R.drawable.folder,
                buttons = listOf(
                    Pair("YES") {
                        Commands.mountWindows(this)
                        mountText.text = if (Commands.isWindowsMounted(this)) "Unmount Windows" else "Mount Windows"
                    },
                    Pair("NO") {}
                )
            )
        }
    }

    fun getGroupLink(): String {
        return when (Commands.getDevice()) {
            "andromeda", "t860", "t865", "q2q", "winnerx", "winner" -> "https://t.me/project_aloha_issues"

            "alphalm", "alphaplus", "alpha_lao_com", "alphalm_lao_com", "alphaplus_lao_com", "betaplus", "betalm", "beta_lao_com",
            "betaplus_lao_com", "betalm_lao_com", "flashlmdd", "flash_lao_com", "flashlm", "flashlmdd_lao_com", "mh2lm", "mh2plus",
            "mh2plus_lao_com", "mh2lm_lao_com", "mh2lm5g", "mh2lm5g_lao_com", "bhima", "vayu", "G973F", "SM-G973F", "beyond1lte",
            "beyond1qlte", "G973U", "G973U1", "SM-G973U", "SM-G973U1", "G9730", "SM-G9730", "G973N", "SM-G973N", "G973X", "SM-G973X",
            "G973C", "SM-G973C", "SCV41", "SM-SC41", "beyond1", "xpeng", "venus", "alioth" -> "https://t.me/woahelperchat"

            "beryllium" -> "https://t.me/WinOnF1"
            "cepheus" -> "http://t.me/woacepheus"
            "cheeseburger", "chiron", "dumpling", "lisa", "sagit" -> "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA"
            "curtana", "curtana2", "curtana_india", "curtana_cn", "curtanacn", "durandal", "durandal_india", "excalibur", "excalibur2",
            "excalibur_india", "gram", "joyeuse", "miatoll" -> "http://t.me/woamiatoll"
            "dipper" -> "https://t.me/woadipper"
            "equuleus" -> "https://t.me/woaequuleus"
            "nabu" -> "https://t.me/nabuwoa"
            "perseus" -> "https://t.me/woaperseus"
            "pipa" -> "https://t.me/pad6_pipa"
            "polaris" -> "https://t.me/WinOnMIX2S"
            "Pong", "pong" -> "https://t.me/woa_pong"
            "raphael", "raphaelin", "raphaels" -> "https://t.me/woaraphael"
            "surya" -> "https://t.me/windows_on_pocox3_nfc"
            "a52sxq" -> "https://t.me/a52sxq_uefi"
            "judyln", "judyp", "judypn", "joan" -> "https://t.me/lgedevices"
            "OnePlus6", "fajita", "OnePlus6T", "enchilada" -> "https://t.me/WinOnOP6"
            "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "guacamole", "guacamolet", "OnePlus7Pro", "OnePlus7Pro4G", "guacamoleb", "hotdogb", "OnePlus7T",
            "OnePlus7", "OnePlus7TPro5G", "OnePlus7TProNR", "hotdogg", "OP7ProNRSpr" -> "https://t.me/onepluswoachat"
            "davinci" -> "https://t.me/woa_davinci"
            "marble" -> "https://t.me/woa_marble"
            "RMX2061", "RMX2170" -> "https://t.me/realme6PROwindowsARM64"
            "cmi", "houji", "meizu20pro", "husky", "redfin", "dm3q", "dm3" -> "https://t.me/dumanthecat"
            "e3q" -> "https://t.me/biskupmuf"
            else -> "https://t.me/joinchat/MNjTmBqHIokjweeN0SpoyA"
        }
    }
    companion object {
        fun getGuideLink(): String {
            return when (Commands.getDevice()) {
                "alphalm", "alphaplus", "alpha_lao_com", "alphalm_lao_com", "alphaplus_lao_com" -> "https://github.com/n00b69/woa-alphaplus"
                "betaplus", "betalm", "beta_lao_com", "betaplus_lao_com", "betalm_lao_com" -> "https://github.com/n00b69/woa-betalm"
                "flashlmdd", "flash_lao_com", "flashlm", "flashlmdd_lao_com" -> "https://github.com/n00b69/woa-flashlmdd"
                "mh2lm", "mh2plus", "mh2plus_lao_com", "mh2lm_lao_com" -> "https://github.com/n00b69/woa-mh2lm"
                "mh2lm5g", "mh2lm5g_lao_com" -> "https://github.com/n00b69/woa-mh2lm5g"
                "beryllium" -> "https://github.com/n00b69/woa-beryllium"
                "bhima", "vayu" -> "https://github.com/woa-vayu/POCOX3Pro-Guides"
                "cepheus" -> "https://github.com/ivanvorvanin/Port-Windows-XiaoMI-9"
                "dumpling", "chiron", "cheeseburger" -> "https://renegade-project.tech/"
                "curtana", "curtana2", "curtana_india", "curtana_cn", "curtanacn", "durandal", "durandal_india",
                "excalibur", "excalibur2", "excalibur_india", "gram", "joyeuse", "miatoll" -> "prod_link"
                "production" -> "https://github.com/woa-miatoll/Port-Windows-11-Redmi-Note-9-Pro"
                "dipper" -> "https://github.com/n00b69/woa-dipper"
                "equuleus" -> "https://github.com/n00b69/woa-equuleus"
                "G973F", "SM-G973F", "beyond1lte", "beyond1qlte", "G973U", "G973U1", "SM-G973U", "SM-G973U1", "G9730", "SM-G9730", "G973N",
                "SM-G973N", "G973X", "SM-G973X", "G973C", "SM-G973C", "SCV41", "SM-SC41", "beyond1" -> "https://github.com/sonic011gamer/Mu-Samsung"
                "lisa" -> "https://github.com/ETCHDEV/Port-Windows-11-Xiaomi-11-Lite-NE"
                "nabu" -> "https://github.com/erdilS/Port-Windows-11-Xiaomi-Pad-5/tree/main"
                "perseus" -> "https://github.com/n00b69/woa-perseus"
                "pipa" -> "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md"
                "polaris" -> "https://github.com/n00b69/woa-polaris"
                "Pong", "pong" -> "https://github.com/Govro150/woa-pong"
                "raphael", "raphaelin", "raphaels" -> "https://github.com/woa-raphael/woa-raphael"
                "surya" -> "https://github.com/woa-surya/pocox3nfc-guides"
                "a52sxq" -> "https://github.com/woa-a52s/Samsung-A52s-5G-Guides"
                "judyln", "judyp", "judypn" -> "https://github.com/n00b69/woa-everything"
                "joan" -> "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md"
                "OnePlus6", "fajita", "OnePlus6T", "enchilada" -> "https://github.com/n00b69/woa-op6"
                "hotdog", "OnePlus7TPro", "OnePlus7TPro4G", "guacamole", "guacamolet", "OnePlus7Pro", "OnePlus7Pro4G" -> "https://github.com/n00b69/woa-op7"
                "guacamoleb", "hotdogb", "OnePlus7T", "OnePlus7", "OnePlus7TPro5G", "OnePlus7TProNR", "hotdogg", "OP7ProNRSpr",
                "t860", "t865", "q2q", "andromeda" -> "https://project-aloha.github.io/"
                "sagit" -> "https://renegade-project.tech/"
                "winnerx", "winner" -> "https://github.com/n00b69/woa-winner"
                "xpeng", "venus", "RMX2061", "RMX2170", "cmi", "houji", "meizu20pro", "husky", "redfin", "e3q", "dm3q", "dm3" -> "https://github.com/Robotix22/WoA-Guides/blob/main/Mu-Qcom/README.md"
                "davinci" -> "https://github.com/zxcwsurx/woa-davinci"
                "marble" -> "https://github.com/Xhdsos/woa-marble"
                else -> "https://renegade-project.tech/"
            }
        }
    }

}