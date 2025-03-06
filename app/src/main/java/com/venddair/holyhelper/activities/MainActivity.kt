package com.venddair.holyhelper.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.venddair.holyhelper.R
import com.venddair.holyhelper.ui.themes.main.MainTheme
import com.venddair.holyhelper.utils.Files
import com.venddair.holyhelper.utils.Preferences
import com.venddair.holyhelper.utils.State
import com.venddair.holyhelper.utils.ToastUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.intuit.sdp.R.dimen
import com.venddair.holyhelper.MainViewModel
import com.venddair.holyhelper.ui.theme.generateAppColors
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        State.coroutineInit()
        //FilePicker.init(this@MainActivity)
        ToastUtil.init(this@MainActivity)
        Preferences.init(this@MainActivity)
        Files.init(this@MainActivity)

        State.context = WeakReference(this)


        if (savedInstanceState == null) {
            val viewModel: MainViewModel by viewModels()

            State.viewModel = viewModel

        }

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
            Preferences.LASTBACKUPDATE.set(date)
            State.viewModel.lastBackupDate.postValue(date)

            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime

            Log.d("INFO", "Changing last backup date: $elapsedTime")
        }
    }
}

@Composable
fun sdp(sdp: Int): Dp {
    return dimensionResource(sdp)
}

@Composable
fun ssp(ssp: Int): TextUnit {
    return dimensionResource(ssp).value.sp
}
