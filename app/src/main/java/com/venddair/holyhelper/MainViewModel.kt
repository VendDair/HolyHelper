package com.venddair.holyhelper

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.DeviceConfig
import com.venddair.holyhelper.utils.DeviceConfigProvider
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val versionText = MutableStateFlow<String?>(null)
    val deviceName = MutableStateFlow<String?>(null)
    val panelType = MutableStateFlow<String?>(null)

    val drawable = MutableStateFlow(R.drawable.unknown)
    val isUefiFilePresent = MutableStateFlow<Boolean>(false)
    val mountText = MutableStateFlow<String>("")
    val lastBackupDate = MutableStateFlow<String>("")
    val totalRam = MutableStateFlow<String?>(null)
    val slot = MutableStateFlow<String>("null")
    val easterEgg1 = MutableStateFlow<Boolean>(false)

    val isWindowsMounted = MutableStateFlow(false)
    val bootPartition = MutableStateFlow<String?>(null)
    val winPartition = MutableStateFlow<String?>(null)

    val rootPrivilege = MutableStateFlow<Boolean?>(true)

    val isLoading = MutableStateFlow(true)
    val hadLoaded = MutableStateFlow(false)

    var useMaterialYou = MutableStateFlow(true)
    var colorsBasedOnDefault = MutableStateFlow(false)

    fun loadData(context: Context) {
        if (hadLoaded.value) return

        isLoading.update { true }

        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                val startTime = System.currentTimeMillis()

                val deviceConfig = DeviceConfigProvider.getConfig(Device.get())

                State.deviceConfig = deviceConfig

                bootPartition.update { Files.getBootPartition() }
                winPartition.update { Files.getWinPartition() }

                isWindowsMounted.update { Commands.isWindowsMounted() }

                val rootPrivilegeDeferred = async { Shell.isAppGrantedRoot() }


                rootPrivilege.update { rootPrivilegeDeferred.await() }



                versionText.update { Strings.version }
                CoroutineScope(Dispatchers.Main).launch { deviceName.update { getDeviceName() } }
                CoroutineScope(Dispatchers.Main).launch { panelType.update { getPanelType() } }
                CoroutineScope(Dispatchers.Main).launch { drawable.update { getDrawable() } }
                CoroutineScope(Dispatchers.Main).launch { isUefiFilePresent.update { isUefiFilePresent() } }
                CoroutineScope(Dispatchers.Main).launch { mountText.update { getMountText() } }
                CoroutineScope(Dispatchers.Main).launch { slot.update { getSlot() } }
                CoroutineScope(Dispatchers.Main).launch { totalRam.update { getTotalRam() } }
                useMaterialYou.update { Preferences.MATERIALYOU.get() }
                colorsBasedOnDefault.update { Preferences.COLORSBASEDONDEFAULT.get() }
                lastBackupDate.update { Preferences.LASTBACKUPDATE.get() }
                easterEgg1.update { Preferences.EASTEREGG1.get() }



                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime
                Log.d("INFO", "Creating data: $elapsedTime")


                CoroutineScope(Dispatchers.Main).launch {
                    delay(75)
                    isLoading.update {false }
                    hadLoaded.update { true }
                }
            }
        }
    }

    private fun getDeviceName(): String {
        return "${Device.getModel()} (${Device.get()})"
    }
    private fun getDrawable(): Int {
        return State.deviceConfig.imageResId
    }
    private fun isUefiFilePresent(): Boolean {
        return Files.checkFile(Strings.uefiImg)
    }
    private fun getMountText(): String {
        return if (State.isWindowsMounted)
            State.context.getString(
                R.string.mnt_title,
                State.context.getString(R.string.unmountt)
            )
        else
            State.context.getString(
                R.string.mnt_title,
                State.context.getString(R.string.mountt)
            )
    }
    private fun getPanelType(): String? {
        if (!State.deviceConfig.isPanel) return null
        return State.context.getString(R.string.paneltype, Device.getPanelType())
    }
    private fun getSlot(): String {
        return State.context.getString(R.string.slot, Device.getSlot())
    }
    private fun getTotalRam(): String {
        return State.context.getString(R.string.ramvalue, Device.getTotalRam(State.context))
    }


    fun onResume() {
        val startTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            val deviceConfig = DeviceConfigProvider.getConfig(Device.get())
            State.deviceConfig = deviceConfig

            isWindowsMounted.update { Commands.isWindowsMounted() }
            deviceName.update { getDeviceName() }
            drawable.update { getDrawable() }
            isUefiFilePresent.update { isUefiFilePresent() }
            mountText.update { getMountText() }
        }


        /*CoroutineScope(Dispatchers.Main).launch {
            val deviceConfig = DeviceConfigProvider.getConfig(Device.get())
            State.deviceConfig = deviceConfig
        }
        CoroutineScope(Dispatchers.Main).launch { isWindowsMounted.update { Commands.isWindowsMounted() } }
        CoroutineScope(Dispatchers.Main).launch { deviceName.update { getDeviceName() } }
        CoroutineScope(Dispatchers.Main).launch { drawable.update { getDrawable() } }
        CoroutineScope(Dispatchers.Main).launch { isUefiFilePresent.update { isUefiFilePresent() } }
        CoroutineScope(Dispatchers.Main).launch { mountText.update { getMountText() } }*/


        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        Log.d("INFO", "Reactive: $elapsedTime")
    }
}
