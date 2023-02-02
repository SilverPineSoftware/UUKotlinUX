package com.silverpine.uu.ux

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("uuBackgroundColor")
fun uuBindBackgroundColor(view: View, backgroundColor: Int?)
{
    backgroundColor?.let()
    {
        view.setBackgroundColor(it)
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