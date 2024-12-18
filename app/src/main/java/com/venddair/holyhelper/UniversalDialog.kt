package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

object UniversalDialog {
    private lateinit var dialog: Dialog
    lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

    fun showDialog(title: String = "", text: String = "", textGravity: Int = Gravity.CENTER, image: Int = R.drawable.win11logo,
                   buttons: List<Pair<String, () -> Unit>> = listOf()
    ) {

        // Inflate the custom layout
        val dialogView: View = LayoutInflater.from(appContext).inflate(R.layout.universal_dialog, null)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(appContext)
            .setView(dialogView)
            .setCancelable(true)

        val imageView: ImageView = dialogView.findViewById(R.id.image)
        val dialogText: TextView = dialogView.findViewById(R.id.title)
        val textText: TextView = dialogView.findViewById(R.id.text)
        val container: LinearLayout = dialogView.findViewById(R.id.container)

        dialogText.text = title
        textText.text = text
        textText.gravity = textGravity
        imageView.setImageResource(image)

        if (text == "") {
            val textTextParams = textText.layoutParams as ViewGroup.MarginLayoutParams
            textTextParams.setMargins(textTextParams.leftMargin, 0, textTextParams.rightMargin, 0)
        }

        for (button in buttons) {
            val buttonView = Button(appContext)
            buttonView.text = button.first
            buttonView.textSize = 14.0f
            buttonView.setOnClickListener {
                button.second()
                dialog.dismiss()
            }
            container.addView(buttonView)
        }

        // Create and show the dialog
        dialog = dialogBuilder.create()
        dialog.show()
    }
}