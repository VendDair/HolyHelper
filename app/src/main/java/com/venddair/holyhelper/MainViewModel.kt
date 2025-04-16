package com.venddair.holyhelper

import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.DeviceConfigProvider
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.checkAppRestriction
import com.venddair.holyhelper.utils.checkRoot
import com.venddair.holyhelper.utils.deviceConfig
import com.venddair.holyhelper.utils.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {

    val versionText = MutableStateFlow<String?>(null)
    val deviceName = MutableStateFlow<String?>(null)
    val panelType = MutableStateFlow<String?>(null)

    val drawable = MutableStateFlow(R.drawable.unknown)
    val isUefiFilePresent = MutableStateFlow(false)
    val mountText = MutableStateFlow("")
    val lastBackupDate = MutableStateFlow("")
    val totalRam = MutableStateFlow<String?>(null)
    val slot = MutableStateFlow("null")
    val easterEgg1 = MutableStateFlow(false)

    val isWindowsMounted = MutableStateFlow(false)
    val bootPartition = MutableStateFlow<String?>(null)
    val winPartition = MutableStateFlow<String?>(null)

    val rootPrivilege = MutableStateFlow<Boolean?>(false)

    val isLoading = MutableStateFlow(true)
    val hadLoaded = MutableStateFlow(false)

    var useMaterialYou = MutableStateFlow(true)
    var colorsBasedOnDefault = MutableStateFlow(false)

    val blurAmount = MutableStateFlow(0.dp)

    fun loadData() {
        val startTime = System.currentTimeMillis()

        deviceConfig = DeviceConfigProvider.getConfig(Device.get())

        updateBootPartition()
        updateWinPartition()

        isWindowsMounted.update { Commands.isWindowsMounted() }

        val rootPrivilegeDeferred = Shell.isAppGrantedRoot()


        rootPrivilege.update { rootPrivilegeDeferred }

        versionText.update { Strings.version }
        updateDeviceName()
        updatePanelType()
        updateDrawable()
        updateIsUefiFilePresent()
        updateMountText()
        updateSlot()
        updateTotalRam()
        useMaterialYou.update { Preferences.MATERIALYOU.get() }
        colorsBasedOnDefault.update { Preferences.COLORSBASEDONDEFAULT.get() }
        lastBackupDate.update { Preferences.LASTBACKUPDATE.get() }
        easterEgg1.update { Preferences.EASTEREGG1.get() }



        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        Log.d("INFO", "Creating data: $elapsedTime")
    }

    fun updateWinPartition() {
        val partition = Files.getWinPartition()
        if (partition == null && !Strings.dev)
        //if (partition == null)
            Info.noWinPartition()
        winPartition.update { partition }
    }
    fun updateBootPartition() {
        bootPartition.update { Files.getBootPartition() }
    }
    fun updateDeviceName() {
        deviceName.update { "${Device.getModel()} (${Device.get()})" }
    }
    fun updateDrawable() {
        drawable.update { deviceConfig.imageResId }
    }
    fun updateIsUefiFilePresent() {
        isUefiFilePresent.update { Files.checkFile(Strings.uefiImg) }
    }
    fun updateMountText() {
        mountText.update {
            if (Commands.isWindowsMounted()) context.getString(R.string.mnt_title, context.getString(R.string.unmountt))
            else context.getString(R.string.mnt_title, context.getString(R.string.mountt))
        }
    }
    fun updatePanelType() {
        panelType.update {
            if (!deviceConfig.isPanel) null
            else  context.getString(R.string.paneltype, Device.getPanelType())
        }

    }
    fun updateSlot() {
        slot.update { context.getString(R.string.slot, Device.getSlot()) }
    }
    fun updateTotalRam() {
        totalRam.update { context.getString(R.string.ramvalue, Device.getTotalRam(context)) }
    }
    fun updateLastBackupDate() {
        val formatter = SimpleDateFormat("dd-MM HH:mm", Locale.US)
        val date = context.getString(R.string.last, formatter.format(Date()))
        Preferences.LASTBACKUPDATE.set(date)
        lastBackupDate.update { date }
    }


    fun reloadEssentials() {
        CoroutineScope(Dispatchers.Main).launch {
            checkRoot()
            checkAppRestriction()
            deviceConfig = DeviceConfigProvider.getConfig(Device.get())

            isWindowsMounted.update { Commands.isWindowsMounted() }
            updateDeviceName()
            updateDrawable()
            updateIsUefiFilePresent()
            updateMountText()
        }
    }
}
