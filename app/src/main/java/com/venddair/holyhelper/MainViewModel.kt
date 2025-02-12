package com.venddair.holyhelper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.ShellUtils
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

        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                val versionDeferred = async { Paths.version }
                val deviceNameDeferred = async { "${Device.getModel()} (${Device.get()})" }
                val panelTypeDeferred = async {
                    if (!Device.isPanelCheckingSupported()) return@async null
                    context.getString(R.string.paneltype, Device.getPanelType())
                }
                val drawableDeferred = async { Device.getImage() }
                val isUefiFileDeferred = async { Files.checkFile(Paths.uefiImg) }
                val lastBackupDateDeferred = async { Preferences.getString(Preferences.Preference.SETTINGS, Preferences.Key.LASTBACKUPDATE, "") }
                val mountTextDeferred = async {
                    if (State.isWindowsMounted)
                        context.getString(R.string.mnt_title, context.getString(R.string.unmountt))
                    else
                        context.getString(R.string.mnt_title, context.getString(R.string.mountt))
                }
                val totalRamDeferred = async { Device.getTotalRam(context) }
                val slotDeferred = async { Device.getSlot() }

                // Await all async operations and post results
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
