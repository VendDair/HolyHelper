package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
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
        Preferences.init(this)
        Files.init(this)

        Commands.checkUpdate(this)

        val quickbootButton = findViewById<Button>(R.id.quickbootButton)
        val backupButton = findViewById<Button>(R.id.backupButton)
        val deviceImageView = findViewById<ImageView>(R.id.device)
        val mountButton = findViewById<Button>(R.id.mountButton)
        val codeNameText = findViewById<TextView>(R.id.codeName)
        val settingsButton = findViewById<ImageView>(R.id.settingsButton)
        val guideButton = findViewById<TextView>(R.id.guideButton)
        val groupButton = findViewById<TextView>(R.id.groupButton)
        val toolboxButton = findViewById<Button>(R.id.toolboxButton)
        val versionTextView = findViewById<TextView>(R.id.version)
        val panelTypeTextView = findViewById<TextView>(R.id.panelType)

        deviceImageView.setImageDrawable(Files.getResourceFromDevice())
        codeNameText.text = Device.get()

        panelTypeTextView.text = getString(R.string.paneltype, Device.getPanelType())

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        toolboxButton.setOnClickListener {
            startActivity(Intent(this, ToolboxActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        guideButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Device.getGuideLink())))}
        groupButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Device.getGroupLink())))}

        versionTextView.text = Paths.version

        quickbootButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.quickboot_question),
                buttons = listOf(
                    Pair(getString(R.string.flash1)) {
                        Commands.bootInWindows(this)
                    },
                    Pair(getString(R.string.reboot)) {
                        Commands.bootInWindows(this, true)
                    },
                    Pair(getString(R.string.no)) {}
                )
            )
        }

        backupButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = getString(R.string.backup_boot_question),
                image = R.drawable.cd,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Commands.backupBootImage(this, mountButton)
                    },
                    Pair(getString(R.string.no)) {}
                )
            )
        }

        mountButton.setTitle(if (Commands.isWindowsMounted(this)) getString(R.string.mnt_title, getString(R.string.unmountt)) else getString(R.string.mnt_title, getString(R.string.mountt)))
        mountButton.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = if (!Commands.isWindowsMounted(this)) getString(R.string.mount_question, Files.getMountDir()) else getString(R.string.unmount_question),
                image = R.drawable.folder,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        Commands.mountWindows(this)
                        mountButton.setTitle(if (Commands.isWindowsMounted(this)) getString(R.string.mnt_title, getString(R.string.unmountt)) else getString(R.string.mnt_title, getString(R.string.mountt)))
                    },
                    Pair(getString(R.string.no)) {}
                )
            )
        }

        // Check if the app can request package installs
        if (!packageManager.canRequestPackageInstalls()) {
            // Request permission to install unknown apps
            requestInstallPermission(this)
            return
        }
    }

}