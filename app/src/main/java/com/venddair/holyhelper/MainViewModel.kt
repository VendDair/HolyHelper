package com.venddair.holyhelper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.DeviceConfigProvider
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val versionText = MutableLiveData<String>()
    val deviceName = MutableLiveData<String>()
    val panelType = MutableLiveData<String?>()

    val drawable = MutableLiveData<Drawable>()
    val isUefiFilePresent = MutableLiveData<Boolean>()
    val mountText = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val lastBackupDate = MutableLiveData<String>()
    val totalRam = MutableLiveData<Float>()
    val slot = MutableLiveData<String?>()

    fun loadData(context: Context) {
        isLoading.postValue(true)

        val deviceConfig = DeviceConfigProvider.getConfig(Device.get())

        State.deviceConfig = deviceConfig

        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                val versionDeferred = async { Strings.version }
                val deviceNameDeferred = async { "${Device.getModel()} (${Device.get()})" }
                val panelTypeDeferred = async {
                    if (!deviceConfig.isPanel) return@async null
                    context.getString(R.string.paneltype, Device.getPanelType())
                }
                val drawableDeferred = async { context.getDrawable(deviceConfig.imageResId)!! }
                val isUefiFileDeferred = async { Files.checkFile(Strings.uefiImg) }
                val lastBackupDateDeferred = async { Preferences.getString(Preferences.Preference.SETTINGS, Preferences.Key.LASTBACKUPDATE, "") }
                val mountTextDeferred = async {
                    if (State.isWindowsMounted)
                        context.getString(R.string.mnt_title, context.getString(R.string.unmountt))
                    else
                        context.getString(R.string.mnt_title, context.getString(R.string.mountt))
                }
                val totalRamDeferred = async { Device.getTotalRam(context) }
                val slotDeferred = async { Device.getSlot() }

                val startTime = System.currentTimeMillis()
                versionText.postValue(versionDeferred.await())
                deviceName.postValue(deviceNameDeferred.await())
                panelType.postValue(panelTypeDeferred.await())
                drawable.postValue(drawableDeferred.await())
                isUefiFilePresent.postValue(isUefiFileDeferred.await())
                mountText.postValue(mountTextDeferred.await())
                lastBackupDate.postValue(lastBackupDateDeferred.await())
                totalRam.postValue(totalRamDeferred.await())
                slot.postValue(slotDeferred.await())

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime
                Log.d("INFO", "Creating data: $elapsedTime")


            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(75)
                isLoading.postValue(false)
            }
        }
    }
}
