package com.venddair.holyhelper

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

object UniversalDialog {
    lateinit var dialog: Dialog

    fun showDialog(
        context: Context,
        title: String = "",
        text: String = "",
        textGravity: Int = Gravity.CENTER,
        image: Int = R.drawable.win11logo,
        buttons: List<Pair<String, () -> Unit>> = listOf(),
        after: (dialog: Dialog) -> Unit = {}
    ) {

        // Inflate the custom layout
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.universal_dialog, null)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
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

        for ((index, button) in buttons.withIndex()) {
            val buttonView = Button(context)
            buttonView.text = button.first
            buttonView.textSize = 14.0f
            buttonView.setPadding(10, 0, 10, 0)
            buttonView.setBackgroundResource(R.drawable.rounded_light_gray)
            buttonView.setTextColor(context.getColor(R.color.white))
            buttonView.setTypeface(buttonView.typeface, Typeface.BOLD)
            buttonView.setOnClickListener {
                dialog.dismiss()
                button.second()
                after(dialog)
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            if (index < buttons.size - 1) {
                params.rightMargin = 20
            }

            buttonView.layoutParams = params
            container.addView(buttonView)
        }

        dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setOnDismissListener { after(dialog) }

        dialog.show()
    }
}