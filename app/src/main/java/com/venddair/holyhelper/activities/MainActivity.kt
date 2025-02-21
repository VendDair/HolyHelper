package com.venddair.holyhelper.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.topjohnwu.superuser.Shell
import com.venddair.holyhelper.Info
import com.venddair.holyhelper.R
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.utils.Commands
import com.venddair.holyhelper.utils.Commands.isWindowsMounted
import com.venddair.holyhelper.utils.Device
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import com.venddair.holyhelper.utils.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        State.coroutineInit()
        //FilePicker.init(this@MainActivity)
        ToastUtil.init(this@MainActivity)
        Preferences.init(this@MainActivity)
        Files.init(this@MainActivity)

        CoroutineScope(Dispatchers.Main).launch {
            Commands.checkUpdate(this@MainActivity)
        }
        State.winPartition = Files.getWinPartition(this)

        CoroutineScope(Dispatchers.Main).launch {
            State.bootPartition = Files.getBootPartition()
        }
        State.isWindowsMounted = isWindowsMounted()

        if (Shell.isAppGrantedRoot() != true)
            Info.noRootDetected(this)

        if (Device.isRestricted())
            Info.appRestricted(this)

        setContent {
            MainTheme()
        }
    }

    companion object {
        fun updateMountText(context: Context) {
            val startTime = System.currentTimeMillis()

            State.viewModel.mountText.postValue(if (State.isWindowsMounted) context.getString(
                R.string.mnt_title,
                context.getString(R.string.unmountt)
            ) else context.getString(R.string.mnt_title, context.getString(R.string.mountt)))


            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime

            Log.d("INFO", "Changing mount text: $elapsedTime")
        }

        fun updateLastBackupDate(context: Context) {
            val startTime = System.currentTimeMillis()

            val formatter = SimpleDateFormat("dd-MM HH:mm", Locale.US)
            val date = context.getString(R.string.last, formatter.format(Date()))
            Preferences.putString(Preferences.Preference.SETTINGS, Preferences.Key.LASTBACKUPDATE, date)
            State.viewModel.lastBackupDate.postValue(date)

            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime

            Log.d("INFO", "Changing last backup date: $elapsedTime")
        }
    }

}