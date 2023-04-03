package com.silverpine.uu.ux

import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

@BindingAdapter("uuBackgroundColor")
fun uuBindBackgroundColor(view: View, @ColorRes resourceId: Int?)
{
    resourceId?.let()
    {
        view.setBackgroundColor(UUResources.getColor(it))
    }
}

@BindingAdapter("uuBackgroundDrawable")
fun uuBindBackgroundDrawable(view: View, @DrawableRes resourceId: Int?)
{
    resourceId?.let()
    {
        view.background = UUResources.getDrawable(it)
    }
}

@BindingAdapter("uuVisibleOrGone")
fun uuBindVisibleOrGone(view: View, visible: Boolean?)
{
    if (visible == true)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}

@BindingAdapter("uuVisibleOrInvisible")
fun uuBindVisibleOrInvisible(view: View, visible: Boolean?)
{
    if (visible == true)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.INVISIBLE
    }
}