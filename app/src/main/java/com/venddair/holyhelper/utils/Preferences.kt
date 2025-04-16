package com.venddair.holyhelper.utils

import android.content.Context
import android.content.SharedPreferences
import com.venddair.holyhelper.ui.theme.BaseColors
import com.venddair.holyhelper.ui.themes.ThemeType
import com.venddair.holyhelper.ui.themes.ogwoaheler2_0.OGWoaHelper2_0Theme

object Preferences {

    private lateinit var sharedPrefs: SharedPreferences

    @Suppress("UNCHECKED_CAST")
    class Preference<T : Any>(private val key: String, private val default: T) {
        fun get(): T {
            return when (default) {
                is Boolean -> sharedPrefs.getBoolean(key, default as Boolean) as T
                is String -> sharedPrefs.getString(key, default as String) as T
                else -> throw IllegalArgumentException("Invalid type")
            }
        }

        fun set(value: T) {
            with(sharedPrefs.edit()) {
                when (value) {
                    is Boolean -> putBoolean(key, value as Boolean)
                    is String -> putString(key, value as String)
                }
                commit()
            }
        }
    }

    val COLOR = Preference("color", BaseColors.color)
    val TEXTCOLOR = Preference("textColor", BaseColors.textColor)
    val GUIDEGROUPCOLOR = Preference("guideGroupColor", BaseColors.guideGroupColor)
    val LASTBACKUPDATE = Preference("lastBackupDate", "")
    val MOUNTTOMNT = Preference("mountToMnt", false)
    val DISABLEUPDATES = Preference("disableUpdates", false)
    val AUTOMOUNT = Preference("autoMount", false)
    val BACKUPBOOT = Preference("backupBoot", true)
    val BACKUPBOOTANDROID = Preference("backupBootToAndroid", true)
    val BACKUPBOOTWINDOWS = Preference("backupBootToWindows", true)
    val QSCONFIRMATION = Preference("qsConfirmation", true)
    val REQUIREUNLOCKED = Preference("requireUnlocked", true)
    val COLORSBASEDONDEFAULT = Preference("colorsBasedOnDefault", false)
    val MATERIALYOU = Preference("materialyou", true)
    val EASTEREGG1 = Preference("redfinImage", false)

    val THEME = Preference("theme", ThemeType.MAIN.type)
    /*val DEFAULTTHEME = Preference("defaultMenu", true)
    val EASYTHEME = Preference("easyMenu", false)
    val OGWoaHelper2_0 = Preference("OGWoaHelper2.0", false)*/



    fun init(context: Context) {
        sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}