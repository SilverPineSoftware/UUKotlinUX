package com.silverpine.uu.ux

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.databinding.BindingAdapter
import com.silverpine.uu.logging.UULog

private const val LOG_TAG = "UULayoutTransitions"

class UULayoutTransition(
    var duration: Long = 200L,
    var startDelay: Long = 0L,
    var interpolator: Interpolator? = null,
    var completion: ((ViewGroup?,View?,Int)->Unit)? = null)
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

    fun withCompletion(completion: ((ViewGroup?,View?,Int)->Unit)?): UULayoutTransition
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
                UULog.debug(LOG_TAG, "startTransition, container: $container, view: $view, transitionType: $transitionType")
            }

            override fun endTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int)
            {
                UULog.debug(LOG_TAG, "endTransition, container: $container, view: $view, transitionType: $transitionType")

                transition?.disableTransitionType(transitionType)
                model.completion?.invoke(container, view, transitionType)
            }
        })

        target.layoutTransition = lt
    }
}