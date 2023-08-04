package com.silverpine.uu.ux

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.databinding.BindingAdapter

class UULayoutTransition(
    var duration: Long = 200L,
    var startDelay: Long = 0L,
    var interpolator: Interpolator? = null,
    var completion: (()->Unit)? = null)
{
    fun clone(): UULayoutTransition
    {
        return UULayoutTransition(duration, startDelay, interpolator, completion)
    }

    fun withDuration(duration: Long): UULayoutTransition
    {
        val clone = clone()
        clone.duration = duration
        return clone
    }

    fun withStartDelay(delay: Long): UULayoutTransition
    {
        val clone = clone()
        clone.startDelay = delay
        return clone
    }

    fun withCompletion(completion: (()->Unit)?): UULayoutTransition
    {
        val clone = clone()
        clone.completion = completion
        return clone
    }

    fun withInterpolator(interpolator: Interpolator?): UULayoutTransition
    {
        val clone = clone()
        clone.interpolator = interpolator
        return clone
    }
}

@BindingAdapter("uuLayoutTransition")
fun uuBindLayoutTransition(target: ViewGroup, model: UULayoutTransition?)
{
    if (model == null)
    {
        target.layoutTransition = null
    }
    else
    {
        val lt = LayoutTransition()
        lt.setDuration(model.duration)
        lt.setStartDelay(LayoutTransition.CHANGING, model.startDelay)
        lt.setInterpolator(LayoutTransition.CHANGING, model.interpolator)
        lt.enableTransitionType(LayoutTransition.CHANGING)

        lt.addTransitionListener(object: LayoutTransition.TransitionListener
        {
            override fun startTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int)
            {
            }

            override fun endTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int)
            {
                model.completion?.invoke()
            }
        })

        target.layoutTransition = lt
    }
}