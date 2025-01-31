package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.Commands.isWindowsMounted
import com.venddair.holyhelper.Permissions.requestInstallPermission
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {


    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.main)


        // val filePicker = FilePicker(this)
        FilePicker.init(this)

        ToastUtil.init(this)
        Preferences.init(this)
        Files.init(this)

        Commands.checkUpdate(this)

        val quickbootButton = findViewById<Button>(R.id.quickbootButton)
        val backupButton = findViewById<Button>(R.id.backupButton)
        val deviceImageView = findViewById<ImageView>(R.id.device)
        mountButton = WeakReference(findViewById(R.id.mountButton))
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
        guideButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Device.getGuideLink())
                )
            )
        }
        groupButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Device.getGroupLink())
                )
            )
        }

        versionTextView.text = Paths.version

        if (!Files.checkFile(Paths.uefiImg)) {
            quickbootButton.isEnabled = false
            quickbootButton.setTitle(getString(R.string.uefi_not_found))
            quickbootButton.setSubtitle(getString(R.string.uefi_not_found_subtitle, Device.get()))
        }
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
                dismissible = false,
                buttons = listOf(
                    Pair("windows") {
                        Info.pleaseWait(this, R.string.backuped, R.drawable.cd) {
                            Commands.backupBootImage(this@MainActivity, true)

                        }
                    },
                    Pair("android") {
                        Info.pleaseWait(this, R.string.backuped, R.drawable.cd) {
                            Commands.backupBootImage(this@MainActivity)

                        }
                    },
                    Pair(getString(R.string.no)) {}
                )
            )
        }

        updateMountText(this)
        mountButton.get()?.setOnClickListener {
            UniversalDialog.showDialog(this,
                title = if (!isWindowsMounted(this)) getString(
                    R.string.mount_question,
                    Files.getMountDir()
                ) else getString(R.string.unmount_question),
                image = R.drawable.folder,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        val text =
                            if (!isWindowsMounted(this)) R.string.mounted else R.string.unmounted
                        Info.pleaseWait(this, text, R.drawable.folder) {
                            Commands.mountWindows(this)
                            updateMountText(this)
                        }

                    },
                    Pair(getString(R.string.no)) {}
                )
            )
        }

        if (Shell.isAppGrantedRoot() != true) {
            Info.noRootDetected(this)
        }

        // Check if the app can request package installs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!packageManager.canRequestPackageInstalls()) {
                requestInstallPermission(this)
                return
            }
        } else requestInstallPermission(this)

    }

    companion object {
        lateinit var mountButton: WeakReference<Button>

        fun updateMountText(context: Context) {
            mountButton.get()?.setTitle(
                if (isWindowsMounted(context)) context.getString(
                    R.string.mnt_title,
                    context.getString(R.string.unmountt)
                ) else context.getString(R.string.mnt_title, context.getString(R.string.mountt))
            )
        }
    }

}