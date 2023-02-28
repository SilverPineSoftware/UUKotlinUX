package com.silverpine.uu.ux

import android.view.View
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

@BindingAdapter(value = ["bind:uuAlpha", "bind:uuAlphaDuration", "bind:uuAlphaStartDelay"], requireAll = false)
fun uuBindAlpha(view: View, alpha: Float?, duration: Long = 200L, startDelay: Long = 0L)
{
    alpha?.let()
    {
        if (view.alpha != it)
        {
            view.uuFadeAlpha(it, duration, startDelay = startDelay)
        }
    }
}

class UUAlpha(var alpha: Float = 1.0f,
              var duration: Long = 200L,
              var startDelay: Long = 0L,
              var completion: (()->Unit)? = null)
{
    fun clone(): UUAlpha
    {
        return UUAlpha(alpha, duration, startDelay, completion)
    }

    fun withAlpha(alpha: Float): UUAlpha
    {
        val clone = clone()
        clone.alpha = alpha
        return clone
    }

    fun withDuration(duration: Long): UUAlpha
    {
        val clone = clone()
        clone.duration = duration
        return clone
    }

    fun withStartDelay(delay: Long): UUAlpha
    {
        val clone = clone()
        clone.startDelay = delay
        return clone
    }

    fun withCompletion(completion: (()->Unit)): UUAlpha
    {
        val clone = clone()
        clone.completion = completion
        return clone
    }
}

@BindingAdapter("uuAlpha")
fun uuBindAlpha(view: View, alphaObject: UUAlpha?)
{
    alphaObject?.let()
    {
        if (view.alpha != it.alpha)
        {
            view.uuFadeAlpha(it.alpha, it.duration, startDelay = it.startDelay, completion = it.completion)
        }
    }
}