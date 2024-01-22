package com.silverpine.uu.ux

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.databinding.BindingAdapter


class UUAlpha(var alpha: Float = 1.0f,
              var duration: Long = 200L,
              var startDelay: Long = 0L,
              var interpolator: Interpolator? = null,
              var completion: (()->Unit)? = null)
{
    companion object
    {
        val FADE_IN = UUAlpha(1.0f, 500L, 0L, AccelerateInterpolator())
        val FADE_OUT = UUAlpha(0.0f, 500L, 0L, DecelerateInterpolator())
    }

    fun clone(): UUAlpha
    {
        return UUAlpha(alpha, duration, startDelay, interpolator, completion)
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

    fun withCompletion(completion: (()->Unit)?): UUAlpha
    {
        val clone = clone()
        clone.completion = completion
        return clone
    }

    fun withInterpolator(interpolator: Interpolator?): UUAlpha
    {
        val clone = clone()
        clone.interpolator = interpolator
        return clone
    }
}

@BindingAdapter("uuAlpha")
fun uuBindAlpha(view: View, alphaObject: UUAlpha?)
{
    alphaObject?.let()
    {
        view.uuFadeAlpha(it.alpha, it.duration, it.interpolator, it.startDelay, it.completion)
    }
}