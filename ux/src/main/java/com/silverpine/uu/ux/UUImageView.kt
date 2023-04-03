package com.silverpine.uu.ux

import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

@BindingAdapter("uuSourceDrawable")
fun uuBindImageDrawable(view: ImageView, @DrawableRes sourceDrawable: Int?)
{
    sourceDrawable?.let()
    {
        view.setImageDrawable(UUResources.getDrawable(it, null))
    }
}

@BindingAdapter("uuBackgroundDrawable")
fun setBackgroundDrawable(view: View, @DrawableRes drawableResource:  Int?)
{
    drawableResource?.let {
        view.background = UUResources.getDrawable(it, null)
    }
}

@BindingAdapter("uuImageOverlay")
fun uuImageOverlay(view: ImageView, colorResource: Int)
{
    view.setColorFilter(
        ContextCompat.getColor(
            view.context,
            colorResource
        ), android.graphics.PorterDuff.Mode.SRC_OVER
    )
}

@BindingAdapter("uuImageTint")
fun uuSetImageTint(view: ImageView, @ColorRes colorResource:  Int)
{
    view.setColorFilter(UUResources.getColor(colorResource, null))
}
