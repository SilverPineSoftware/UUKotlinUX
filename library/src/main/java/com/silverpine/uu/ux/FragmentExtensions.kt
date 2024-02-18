package com.silverpine.uu.ux

import android.transition.Fade
import android.transition.Slide
import android.transition.Visibility
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

fun Fragment.uuHideKeyboard()
{
    activity?.uuHideKeyboard()
}
fun Fragment.uuFinish()
{
    activity?.uuRemoveFragment(this)
}

fun Fragment.uuSetupDataBoundViewModel(viewModel: ViewModel, binding: ViewDataBinding, @IdRes bindingId: Int)
{
    binding.setVariable(bindingId, viewModel)
    binding.lifecycleOwner = viewLifecycleOwner
}

fun Fragment.uuConfigureForFadeTransitions()
{
    uuConfigureForFadeInTransition()
    uuConfigureForFadeOutTransition()
}

fun Fragment.uuConfigureForSlideInFromBottomTransitions(
    duration: Long = 400L,
    startDelay: Long = 200L)
{
    val direction = Gravity.BOTTOM
    uuConfigureForSlideInTransition(direction, duration, startDelay)
    uuConfigureForSlideOutTransition(direction, duration, 0)
}

fun Fragment.uuConfigureForFadeInTransition(duration: Long = 500L, delay: Long = 0L)
{
    val fadeIn = Fade(Visibility.MODE_IN)
    fadeIn.duration = duration
    fadeIn.startDelay = delay
    fadeIn.interpolator = DecelerateInterpolator()

    enterTransition = fadeIn
}

fun Fragment.uuConfigureForFadeOutTransition(duration: Long = 500L, delay: Long = 0L)
{
    val fadeOut = Fade(Visibility.MODE_OUT)
    fadeOut.duration = duration
    fadeOut.startDelay = delay
    fadeOut.interpolator = AccelerateInterpolator()

    exitTransition = fadeOut
}

fun Fragment.uuConfigureForSlideInTransition(direction: Int, duration: Long, delay: Long)
{
    val slideIn = Slide(direction)
    slideIn.duration = duration
    slideIn.startDelay = delay
    slideIn.interpolator = DecelerateInterpolator()

    enterTransition = slideIn
}

fun Fragment.uuConfigureForSlideOutTransition(direction: Int, duration: Long, delay: Long)
{
    val slideOut = Slide(direction)
    slideOut.duration = duration
    slideOut.startDelay = delay
    slideOut.interpolator = AccelerateInterpolator()

    exitTransition = slideOut
}
