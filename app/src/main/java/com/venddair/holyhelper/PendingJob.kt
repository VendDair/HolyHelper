package com.venddair.holyhelper

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class PendingJob @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val textView: TextView

    init {
        // Set up LinearLayout properties (matches XML)
        setBackgroundResource(R.drawable.rounded_light_gray)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        // Create and configure TextView (matches XML)
        textView = TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            setTextColor(context.getColor(R.color.white))
            gravity = Gravity.CENTER
            setPadding(60, dpToPx(20), 60, dpToPx(20))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTypeface(typeface, Typeface.BOLD)
        }

        addView(textView)
    }

    fun setText(text: String) {
        visibility = View.VISIBLE
        textView.text = "Pending Job: $text"
    }

    fun hide() {
        visibility = View.GONE
    }

    // Helper to convert dp to pixels
    private fun dpToPx(dp: Int): Int =
        (dp * context.resources.displayMetrics.density).toInt()
}