package com.venddair.holyhelper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venddair.holyhelper.Commands.isWindowsMounted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val versionText = MutableLiveData<String>()
    val deviceName = MutableLiveData<String>()
    val panelType = MutableLiveData<String>()
    val drawable = MutableLiveData<Drawable>()
    val isUefiFilePresent = MutableLiveData<Boolean>()
    val mountText = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun loadData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)

            coroutineScope {
                val versionDeferred = async { Paths.version }
                val deviceNameDeferred = async { Device.get() }
                val panelTypeDeferred = async { context.getString(R.string.paneltype, Device.getPanelType()) }
                val drawableDeferred = async { Files.getResourceFromDevice() }
                val isUefiFileDeferred = async { Files.checkFile(Paths.uefiImg) }
                val mountTextDeferred = async {
                    if (State.isWindowsMounted)
                        context.getString(R.string.mnt_title, context.getString(R.string.unmountt))
                    else
                        context.getString(R.string.mnt_title, context.getString(R.string.mountt))
                }

                // Await all async operations and post results
                val startTime = System.currentTimeMillis()
                versionText.postValue(versionDeferred.await())
                deviceName.postValue(deviceNameDeferred.await())
                panelType.postValue(panelTypeDeferred.await())
                drawable.postValue(drawableDeferred.await())
                isUefiFilePresent.postValue(isUefiFileDeferred.await())
                mountText.postValue(mountTextDeferred.await())

                val endTime = System.currentTimeMillis()
                val elapsedTime = endTime - startTime

                Log.d("INFO", "Creating data: $elapsedTime")

            }
            isLoading.postValue(false)
        }
    }
}
