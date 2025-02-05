package com.venddair.holyhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.venddair.holyhelper.Commands.isWindowsMounted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        State.coroutineInit()
        //FilePicker.init(this@MainActivity)
        ToastUtil.init(this@MainActivity)
        Preferences.init(this@MainActivity)
        Files.init(this@MainActivity)

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
        val loading = findViewById<LinearLayout>(R.id.loading)
        lastBackup = WeakReference(findViewById(R.id.lastBackup))


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.isLoading.observe(this) { isLoading ->
            loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.versionText.observe(this) { versionTextView.text = it }
        viewModel.deviceName.observe(this) { codeNameText.text = it }
        viewModel.panelType.observe(this) {
            if (it == null) {
                panelTypeTextView.visibility = View.GONE
                return@observe
            }
            panelTypeTextView.text = it
        }
        viewModel.drawable.observe(this) { deviceImageView.setImageDrawable(it) }
        viewModel.isUefiFilePresent.observe(this) { isPresent ->
            if (!isPresent) {
                quickbootButton.isEnabled = false
                quickbootButton.setTitle(getString(R.string.uefi_not_found))
                quickbootButton.setSubtitle(getString(R.string.uefi_not_found_subtitle, Device.get()))
            }
        }
        viewModel.mountText.observe(this) { mountText ->
            mountButton.get()?.setTitle(mountText)
        }
        viewModel.lastBackupDate.observe(this) { lastBackupDate ->
            if (lastBackupDate != "")
                lastBackup.get()?.text = lastBackupDate
            else
                lastBackup.get()?.visibility = View.GONE
        }

        if (savedInstanceState == null) {
            CoroutineScope(Dispatchers.Main).launch {
                Commands.checkUpdate(this@MainActivity)
            }
            State.winPartition = Files.getWinPartition(this)

            CoroutineScope(Dispatchers.Main).launch {
                State.bootPartition = Files.getBootPartition()
            }
            State.isWindowsMounted = isWindowsMounted()
            viewModel.loadData(this)
        }



        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", "Main activity: $elapsedTime")

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
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
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
                    Pair(getString(R.string.no)) { UniversalDialog.dialog.dismiss() }
                )
            )
        }

        mountButton.get()?.setOnClickListener {
            State.isWindowsMounted = isWindowsMounted()
            UniversalDialog.showDialog(this,
                title = if (!State.isWindowsMounted) getString(
                    R.string.mount_question,
                    Files.getMountDir()
                ) else getString(R.string.unmount_question),
                image = R.drawable.folder,
                dismissible = false,
                buttons = listOf(
                    Pair(getString(R.string.yes)) {
                        val text =
                            if (!State.isWindowsMounted) R.string.mounted else R.string.unmounted
                        Info.pleaseWait(this, text, R.drawable.folder) {
                            Commands.mountWindows(this)
                            updateMountText(this)
                        }

                    },
                    Pair(getString(R.string.no)) {
                        UniversalDialog.dialog.dismiss()
                    }
                )
            )
        }
    }



    /////////////////////////////////////////////////////////////////////

    //@OptIn(DelicateCoroutinesApi::class)
    /*@SuppressLint("SetTextI18n", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        val startTime = System.currentTimeMillis()
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.main)

        runBlocking {
            listOf(
                async { State.coroutineInit() },
                async { FilePicker.init(this@MainActivity)},
                async { ToastUtil.init(this@MainActivity) },
                async { ToastUtil.init(this@MainActivity) },
                async { Preferences.init(this@MainActivity) },
                async { Files.init(this@MainActivity) },
            ).awaitAll()
        }


        CoroutineScope(Dispatchers.Main).launch {
            Commands.checkUpdate(this@MainActivity)
        }


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

        if (!Files.checkFile(Paths.uefiImg)) {
            quickbootButton.isEnabled = false
            quickbootButton.setTitle(getString(R.string.uefi_not_found))
            quickbootButton.setSubtitle(getString(R.string.uefi_not_found_subtitle, Device.get()))
        }

        updateMountText(this@MainActivity)

        versionTextView.text = Paths.version

        val drawable = Files.getResourceFromDevice()
        deviceImageView.setImageDrawable(drawable)

        val deviceName = Device.get()
        codeNameText.text = deviceName

        val panelType = getString(R.string.paneltype, Device.getPanelType())
        panelTypeTextView.text = panelType

        if (Shell.isAppGrantedRoot() != true) {
            Info.noRootDetected(this@MainActivity)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!packageManager.canRequestPackageInstalls()) {
                requestInstallPermission(this@MainActivity)
            }
        } else {
            requestInstallPermission(this@MainActivity)
        }


        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        Log.d("INFO", elapsedTime.toString())
    }*/

    companion object {
        lateinit var mountButton: WeakReference<Button>
        lateinit var lastBackup: WeakReference<TextView>

        fun updateMountText(context: Context) {
            val startTime = System.currentTimeMillis()

            mountButton.get()?.setTitle(
                if (State.isWindowsMounted) context.getString(
                    R.string.mnt_title,
                    context.getString(R.string.unmountt)
                ) else context.getString(R.string.mnt_title, context.getString(R.string.mountt))
            )

            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime

            Log.d("INFO", "Changing mount text: $elapsedTime")
        }

        fun updateLastBackupDate(context: Context) {
            val startTime = System.currentTimeMillis()

            val formatter = SimpleDateFormat("dd-MM HH:mm", Locale.US)
            val date = context.getString(R.string.last, formatter.format(Date()))
            Preferences.putString(Preferences.Preference.SETTINGS, Preferences.Key.LASTBACKUPDATE, date)
            lastBackup.get()?.text = date
            lastBackup.get()?.visibility = View.VISIBLE


            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime

            Log.d("INFO", "Changing last backup date: $elapsedTime")
        }
    }

}