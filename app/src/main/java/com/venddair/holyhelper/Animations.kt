package com.venddair.holyhelper

import android.animation.ObjectAnimator
import android.view.View

object Animations {
    fun startLongPressAnimation(view: View) {
        val scaleUp = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.2f)
        val scaleDown = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f)

        scaleUp.duration = 200
        scaleDown.duration = 200

        scaleUp.start()
        scaleDown.start()
    }
}