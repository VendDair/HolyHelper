/*
package com.venddair.holyhelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

@SuppressLint("ClickableViewAccessibility")
class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val titleTextView: TextView
    private val subtitleTextView: TextView

    init {
        orientation = HORIZONTAL
        background = ContextCompat.getDrawable(context, R.drawable.rounded_gray)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        // ImageView on the left
        imageView = ImageView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(resources.getDimension(R.dimen._65dp).toInt(), LayoutParams.MATCH_PARENT).apply {
                setMargins(0, 0, 8.dp, 0)
            }
            scaleX = 0.7f
            scaleY = 0.7f
            setImageResource(R.drawable.cd) // Default image
        }
        addView(imageView)

        // Vertical LinearLayout container for title and subtitle, centered in its space
        val textContainer = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
            }
        }

        // Title TextView without weights; use wrap_content
        titleTextView = TextView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                setPadding(0, resources.getDimension(com.intuit.sdp.R.dimen._5sdp).toInt(), 0, 0)
            }
            text = context.getString(R.string.backup_boot_title) // Default title
            // Use a dimension resource for textSize; ensure it returns a sp value in float
            textSize = spToPx(resources.getDimension(com.intuit.sdp.R.dimen._minus4sdp))
            setTextColor(ContextCompat.getColor(context, R.color.light_gray))
            setTypeface(typeface, BOLD)
        }
        textContainer.addView(titleTextView)

        // Subtitle TextView without weights; use wrap_content
        subtitleTextView = TextView(context).apply {
            id = generateViewId()
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                setPadding(0, 0, 10.dp, 0)
                setMargins(0, 0, 0, 5.dp)
            }
            text = context.getString(R.string.backup_boot_subtitle) // Default subtitle
            textSize = spToPx(resources.getDimension(com.intuit.sdp.R.dimen._1sdp))
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
        val centered = typedArray.getBoolean(R.styleable.Button_centered, false)

        // Optionally update titleTextView's layout if centered attribute is true
        if (centered) {
            // Here you could update layout parameters, though our text container is already centered.
        }

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
            if (!isClickable) return@setOnTouchListener false
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

    fun setSubtitle(text: String) {
        subtitleTextView.text = text
    }

    fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sp,
            context.resources.displayMetrics
        )
    }
}
*/
