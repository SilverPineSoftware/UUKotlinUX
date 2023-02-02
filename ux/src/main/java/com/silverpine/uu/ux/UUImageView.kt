package com.silverpine.uu.ux

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

@BindingAdapter("uuSourceDrawable")
fun uuBindImageDrawable(view: ImageView, sourceDrawable: Int?)
{
    sourceDrawable?.let()
    {
        view.setImageDrawable(UUResources.getDrawable(it, null))
    }
}