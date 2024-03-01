package com.silverpine.uu.ux

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
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

@BindingAdapter("uuTextColor")
fun uuBindTextColor(view: TextView, @ColorRes resourceId: Int?)
{
    resourceId?.let()
    {
        view.setTextColor(UUResources.getColor(it))
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

@BindingAdapter("uuBottomMargin")
fun View.uuBindBottomMargin(margin: Int)
{
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let()
    { lp ->
        lp.bottomMargin = margin
        layoutParams = lp
    }
}

@BindingAdapter("uuTopMargin")
fun View.uuBindTopMargin(margin: Int)
{
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let()
    { lp ->
        lp.topMargin = margin
        layoutParams = lp
    }
}

@BindingAdapter("uuStartMargin")
fun View.uuBindStartMargin(margin: Int)
{
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let()
    { lp ->
        lp.marginStart = margin
        layoutParams = lp
    }
}

@BindingAdapter("uuEndMargin")
fun View.uuBindEndMargin(margin: Int)
{
    (layoutParams as? ViewGroup.MarginLayoutParams)?.let()
    { lp ->
        lp.marginEnd = margin
        layoutParams = lp
    }
}