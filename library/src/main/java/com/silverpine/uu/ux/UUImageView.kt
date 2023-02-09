package com.silverpine.uu.ux

import android.widget.ImageView
import androidx.annotation.DrawableRes
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