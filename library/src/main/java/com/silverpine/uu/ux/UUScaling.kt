package com.silverpine.uu.ux

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator
import androidx.databinding.BindingAdapter

fun View.uuScale(scaleObject: UUScaling)
{
    animate()
        .setDuration(scaleObject.duration)
        .setStartDelay(scaleObject.startDelay)
        .setInterpolator(scaleObject.interpolator)
        .scaleX(scaleObject.x)
        .scaleY(scaleObject.y)
        .setListener(object: Animator.AnimatorListener
        {
            override fun onAnimationStart(animation: Animator)
            {
            }

            override fun onAnimationEnd(animation: Animator)
            {
                scaleObject.completion?.invoke()
            }

            override fun onAnimationCancel(animation: Animator)
            {
            }

            override fun onAnimationRepeat(animation: Animator)
            {
            }
        })
}

class UUScaling(
    var x: Float,
    var y: Float,
    //var z: Float,
    var duration: Long = 200L,
    var startDelay: Long = 0L,
    var interpolator: Interpolator? = null,
    var completion: (()->Unit)? = null)
{
    fun clone(): UUScaling
    {
        return UUScaling(x, y, duration, startDelay, interpolator, completion)
    }

    fun withX(x: Float): UUScaling
    {
        val clone = clone()
        clone.x = x
        return clone
    }

    fun withY(y: Float): UUScaling
    {
        val clone = clone()
        clone.y = y
        return clone
    }

    fun withDuration(duration: Long): UUScaling
    {
        val clone = clone()
        clone.duration = duration
        return clone
    }

    fun withStartDelay(delay: Long): UUScaling
    {
        val clone = clone()
        clone.startDelay = delay
        return clone
    }

    fun withCompletion(completion: (()->Unit)?): UUScaling
    {
        val clone = clone()
        clone.completion = completion
        return clone
    }

    fun withInterpolator(interpolator: Interpolator?): UUScaling
    {
        val clone = clone()
        clone.interpolator = interpolator
        return clone
    }
}

@BindingAdapter("uuScale")
fun uuBindScale(view: View, scaleObject: UUScaling?)
{
    scaleObject?.let()
    {
        view.uuScale(it)
    }
}