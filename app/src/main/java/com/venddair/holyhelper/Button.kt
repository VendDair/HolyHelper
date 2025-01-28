package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.util.AttributeSet
import android.view.Gravity.BOTTOM
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

@SuppressLint("ClickableViewAccessibility")
class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val titleTextView: TextView
    private val subtitleTextView: TextView

    // Animation properties
    private val scaleDuration = 150L // milliseconds
    private val pressedScale = 0.7f

    init {
        orientation = HORIZONTAL
        background = ContextCompat.getDrawable(context, R.drawable.rounded_gray)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        // ImageView
        imageView = ImageView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(65.dp, LayoutParams.MATCH_PARENT).apply {
                setMargins(0, 0, 8.dp, 0)
            }
            scaleX = 0.7f
            scaleY = 0.7f
            setImageResource(R.drawable.cd) // Default image
        }
        addView(imageView)

        // Vertical LinearLayout for title and subtitle
        val textContainer = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        }

        // Title TextView
        titleTextView = TextView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.8f)
            text = context.getString(R.string.backup_boot_title) // Default title
            textSize = 13f
            setTextColor(ContextCompat.getColor(context, R.color.light_gray))
            setTypeface(typeface, BOLD)
            gravity = BOTTOM
        }
        textContainer.addView(titleTextView)

        // Subtitle TextView
        subtitleTextView = TextView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f).apply {
                setPadding(0, 0, 10.dp, 0)
            }
            text = context.getString(R.string.backup_boot_subtitle) // Default subtitle
            textSize = 11f
            setTextColor(ContextCompat.getColor(context, R.color.gray))
            setTypeface(typeface, ITALIC)
        }
        textContainer.addView(subtitleTextView)

        // Add text container to the root layout
        addView(textContainer)

        // Obtain custom attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button)
        val title = typedArray.getString(R.styleable.Button_title)
        val subtitle = typedArray.getString(R.styleable.Button_subtitle)
        val imageResId = typedArray.getResourceId(R.styleable.Button_image, 0)
        val imageScaleX = typedArray.getFloat(R.styleable.Button_imageScaleX, 0.7f)
        val imageScaleY = typedArray.getFloat(R.styleable.Button_imageScaleY, 0.7f)

        // Set custom attributes
        title?.let { titleTextView.text = it }
        subtitle?.let { subtitleTextView.text = it }
        if (imageResId != 0) {
            imageView.setImageResource(imageResId)
        }

        imageView.scaleX = imageScaleX
        imageView.scaleY = imageScaleY


        typedArray.recycle()

        isClickable = true
        isFocusable = true

        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(75)
                        .start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(75)
                        .start()
                }
            }
            // Return false to allow other events like clicks to proceed
            false
        }
    }

    // Extension function to convert dp to px
    private val Int.dp: Int
        get() = (this * context.resources.displayMetrics.density).toInt()

    fun setTitle(text: String) {
        titleTextView.text = text
    }
}
