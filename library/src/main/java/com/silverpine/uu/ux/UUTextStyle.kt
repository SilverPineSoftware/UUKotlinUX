package com.silverpine.uu.ux

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUTextStyle
(
    // If fot is set, fontResourceId is ignored
    var font: Typeface? = null,
    @FontRes var fontResourceId: Int? = null,
    var fontTextWeight: Int? = null,
    var fontStyle: Int? = null,

    @ColorRes var textColorResourceId: Int? = null,
    @DimenRes var textSizeResourceId: Int? = null,

    var allCaps: Boolean? = null,
    var enabled: Boolean? = null,
    var gravity: Int? = null,

    // If background is set, backgroundResourceId is ignored
    var background: Drawable? = null,
    @DrawableRes var backgroundResourceId: Int? = null,
)
{
    private fun getTypeface(): Typeface?
    {
        var tf = font

        fontResourceId?.let()
        { fontId ->
            tf = UUResources.getFont(fontId)
        }

        fontTextWeight?.let()
        { fontWeight ->
            tf = Typeface.create(tf, fontWeight)
        }

        val tfStyle = fontStyle ?: Typeface.NORMAL
        return Typeface.create(tf, tfStyle)
    }

    private fun getBackgroundDrawable(): Drawable?
    {
        var bg: Drawable? = background

        backgroundResourceId?.let()
        { backgroundId ->
            bg = UUResources.getDrawable(backgroundId)
        }

        return bg
    }

    open fun applyTo(target: TextView)
    {
        getTypeface()?.let()
        { tf ->
            target.typeface = tf
        }

        textColorResourceId?.let()
        { textColor ->
            target.setTextColor(UUResources.getColor(textColor))
        }

        allCaps?.let()
        { allCaps ->
            target.isAllCaps = allCaps
        }

        target.background = getBackgroundDrawable()

        textSizeResourceId?.let()
        { textSize ->
            target.setTextSize(TypedValue.COMPLEX_UNIT_PX, UUResources.getDimension(textSize))
        }

        gravity?.let()
        { gravity ->
            target.gravity = gravity
        }

        enabled?.let()
        { enabled ->
            target.isEnabled = enabled
        }
    }
}

@BindingAdapter("uuTextStyle")
fun uuBindTextStyle(target: TextView, style: UUTextStyle?)
{
    style?.applyTo(target)
}
