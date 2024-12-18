package com.venddair.holyhelper

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    fun get(name: String): SharedPreferences {
        return appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}