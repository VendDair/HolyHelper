package com.venddair.holyhelper

object State {

    private var failed: Boolean = false

    fun setFailed(state: Boolean) {
        if (state) {
        }
        failed = state
    }

    fun getFailed(): Boolean { return failed }

}