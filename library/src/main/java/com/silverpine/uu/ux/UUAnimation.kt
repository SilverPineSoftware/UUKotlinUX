package com.silverpine.uu.ux

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

fun View.uuFadeAlpha(
    toAlpha: Float,
    duration: Long,
    interpolator: Interpolator? = null,
    startDelay: Long = 0,
    completion: (() -> Unit)? = null)
{
    animate()
        .alpha(toAlpha)
        .setDuration(duration)
        .setStartDelay(startDelay)
        .setInterpolator(interpolator)
        .withEndAction(completion)
        .start()
}

fun View.uuFadeIn(
    duration: Long,
    startDelay: Long = 0,
    completion: (() -> Unit)? = null)
{
    uuFadeAlpha(1.0f, duration, AccelerateInterpolator(), startDelay, completion)
}

fun View.uuFadeOut(
    duration: Long,
    startDelay: Long,
    completion: ()->Unit)
{
    uuFadeAlpha(0.0f, duration, DecelerateInterpolator(), startDelay, completion)
}

fun ViewGroup.uuAnimateLayout(
    updatedLayoutParams: ViewGroup.LayoutParams,
    duration: Long,
    transitionType: Int = LayoutTransition.CHANGING,
    interpolator: Interpolator? = null,
    startDelay: Long = 0,
    completion: (() -> Unit)? = null)
{
    val lt = LayoutTransition()
    lt.setDuration(duration)
    lt.setStartDelay(transitionType, startDelay)
    lt.setInterpolator(transitionType, interpolator)
    lt.enableTransitionType(transitionType)

    lt.addTransitionListener(object: LayoutTransition.TransitionListener
    {
        override fun startTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int)
        {
        }

        override fun endTransition(transition: LayoutTransition?, container: ViewGroup?, view: View?, transitionType: Int)
        {
            transition?.disableTransitionType(transitionType)
            completion?.invoke()
        }
    })

    layoutTransition = lt

    layoutParams = updatedLayoutParams
}


