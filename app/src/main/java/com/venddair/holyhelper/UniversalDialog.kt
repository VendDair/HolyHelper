    package com.venddair.holyhelper

    import android.animation.ObjectAnimator
    import android.annotation.SuppressLint
    import android.app.AlertDialog
    import android.app.Dialog
    import android.content.Context
    import android.graphics.Color
    import android.graphics.drawable.ColorDrawable
    import android.view.Gravity
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.LinearLayout
    import android.widget.ProgressBar
    import android.widget.TextView
    import androidx.activity.ComponentActivity
    import androidx.constraintlayout.widget.ConstraintLayout
    import jp.wasabeef.blurry.Blurry
    import java.lang.ref.WeakReference

    object UniversalDialog {
        lateinit var dialog: Dialog
        lateinit var progressBar: WeakReference<ProgressBar>
        lateinit var textText: WeakReference<TextView>
        lateinit var imageView: WeakReference<ImageView>
        lateinit var dialogText: WeakReference<TextView>
        lateinit var container: WeakReference<LinearLayout>

        fun showDialog(
            context: Context,
            title: String = "",
            text: String = "",
            textGravity: Int = Gravity.CENTER,
            image: Int = R.drawable.win11logo,
            progress: Boolean = false,
            progressMax: Int = 1,
            animations: Boolean = true,
            dismissible: Boolean = true,
            buttons: List<Pair<String, () -> Unit>> = listOf(),
            after: (dialog: Dialog) -> Unit = {},
        ) {

            // Inflate the custom layout
            val dialogView: View = LayoutInflater.from(context).inflate(R.layout.universal_dialog, null)

            // Create the AlertDialog
            val dialogBuilder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(true)


            imageView = WeakReference(dialogView.findViewById(R.id.image))
            dialogText = WeakReference(dialogView.findViewById(R.id.title))
            textText = WeakReference(dialogView.findViewById(R.id.text))
            container = WeakReference(dialogView.findViewById(R.id.container))
            progressBar = WeakReference(dialogView.findViewById(R.id.progressBar))


            dialogText.get()?.text = title
            textText.get()?.text = text
            textText.get()?.gravity = textGravity
            imageView.get()?.setImageResource(image)

            if (text == "") {
                val textTextParams = textText.get()?.layoutParams as ViewGroup.MarginLayoutParams
                textTextParams.setMargins(textTextParams.leftMargin, 0, textTextParams.rightMargin, 0)
            }

            setButtons(context, dismissible, buttons)

            dialog = dialogBuilder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (animations) dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

            val rootView = (context as ComponentActivity).findViewById<ConstraintLayout>(R.id.root)
            val imageView = context.findViewById<ImageView>(R.id.blur)

            dialog.setOnDismissListener {
                imageView.setImageDrawable(null)
                after(dialog)
            }
            dialog.setOnShowListener {
                after(dialog)
            }

            // Progress Bar logic
            setupProgressBar(progress, progressMax)
            dialog.show()

            Blurry.with(context).radius(10).sampling(1).capture(rootView).into(imageView)
        }

        fun increaseProgress(progress: Int) {
            val newProgress = progressBar.get()!!.progress + (progress * 10)
            val animation = ObjectAnimator.ofInt(
                progressBar.get(),
                "progress",
                progressBar.get()!!.progress,
                newProgress
            )
            animation.duration = 250 // Set duration for smooth animation (500ms)
            animation.start()
        }

        fun clear() {
            dialogText.get()?.text = ""
            textText.get()?.text = ""
            container.get()?.removeAllViews()
        }

        @SuppressLint("InflateParams")
        fun setButtons(context: Context, dismissible: Boolean, buttons: List<Pair<String, () -> Unit>> = listOf()) {
            for ((index, button) in buttons.withIndex()) {
                //val buttonView = Button(context)
                val buttonView = LayoutInflater.from(context).inflate(R.layout.button, null)
                val textView = buttonView.findViewById<TextView>(R.id.text)
                textView.text = button.first.uppercase()
                buttonView.setOnClickListener {
                    button.second()
                    if (dismissible) dialog.dismiss()

                    //after(dialog)
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                params.weight = 1f
                if (index < buttons.size - 1) {
                    params.rightMargin = 2
                }

                buttonView.layoutParams = params
                container.get()?.addView(buttonView)
            }
        }

        fun setupProgressBar(progress: Boolean, progressMax: Int) {
            if (!progress) progressBar.get()?.visibility = View.GONE
            else {
                progressBar.get()?.visibility = View.VISIBLE
                progressBar.get()?.max = progressMax * 10
                progressBar.get()?.progress = 0
            }
        }
    }