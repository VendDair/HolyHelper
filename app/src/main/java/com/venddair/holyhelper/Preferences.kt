package com.venddair.holyhelper

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private lateinit var appContext: Context

    enum class Preference(val key: String) {
        SETTINGS("settings"),
    }

    enum class Key(val key: String) {
        MOUNTTOMNT("mountToMnt"),
        DISABLEUPDATES("disableUpdates"),
        AUTOMOUNT("autoMount"),
        UEFIIMG("uefiImg"),
        LASTBACKUPDATE("lastBackupDate")
    }

    fun init(context: Context) {
        appContext = context
    }

    fun get(name: Preference): SharedPreferences {
        return appContext.getSharedPreferences(name.key, Context.MODE_PRIVATE)
    }

    fun getBoolean(preference: Preference, name: Key, defValue: Boolean): Boolean {
        return get(preference).getBoolean(name.key, defValue)
    }

    fun putBoolean(preference: Preference, name: Key, content: Boolean) {
        val editor = get(preference).edit()
        editor.putBoolean(name.key, content)
        editor.apply()
    }

    fun getString(preference: Preference, name: Key, defValue: String?): String {
        return get(preference).getString(name.key, defValue)!!
    }

    fun putString(preference: Preference, name: Key, content: String) {
        val editor = get(preference).edit()
        editor.putString(name.key, content)
        editor.apply()
    }
}