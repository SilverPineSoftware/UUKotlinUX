package com.silverpine.uu.ux

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.WindowInsetsAnimation.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter

interface UUKeyboardListener
{
    fun onKeyboardVisibilityChanged(visible: Boolean, height: Int)
}

@RequiresApi(Build.VERSION_CODES.R)
private class UUWindowInsetAnimationListener(dispatchMode: Int, private val listener: UUKeyboardListener): WindowInsetsAnimation.Callback(dispatchMode)
{
    private var insets: WindowInsets = WindowInsets.Builder().build()
    private var imeVisible: Boolean? = null
    private var imeHeight: Int? = null

    override fun onProgress(insets: WindowInsets, runningAnimations: MutableList<WindowInsetsAnimation>): WindowInsets
    {
        this.insets = insets
        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimation)
    {
        super.onEnd(animation)

        val visible = insets.isVisible(WindowInsets.Type.ime())
        val height = insets.getInsets(WindowInsets.Type.ime()).bottom
        if (visible != imeVisible || height != imeHeight)
        {
            imeVisible = visible
            imeHeight = height
            listener.onKeyboardVisibilityChanged(visible, height)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@BindingAdapter("uuKeyboardListener")
fun uuBindKeyboardListener(view: View, listener: UUKeyboardListener?)
{
    if (listener == null)
    {
        view.setWindowInsetsAnimationCallback(null)
    }
    else
    {
        view.setWindowInsetsAnimationCallback(UUWindowInsetAnimationListener(DISPATCH_MODE_CONTINUE_ON_SUBTREE, listener))
        /*
        view.setOnApplyWindowInsetsListener()
        { _, insets ->

            val imeVisible = view.rootWindowInsets.isVisible(WindowInsets.Type.ime())
            val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
            listener.onKeyboardVisibilityChanged(imeVisible, imeHeight)
            insets
        }*/

        /*
        view.setWindowInsetsAnimationCallback(object : WindowInsetsAnimation.Callback(DISPATCH_MODE_CONTINUE_ON_SUBTREE)
        {
            private var insets: WindowInsets = WindowInsets.Builder().build()

            override fun onProgress(insets: WindowInsets, runningAnimations: MutableList<WindowInsetsAnimation>): WindowInsets
            {
                this.insets = insets
                return insets
            }

            override fun onEnd(animation: WindowInsetsAnimation)
            {
                super.onEnd(animation)

                val imeVisible = view.rootWindowInsets.isVisible(WindowInsets.Type.ime())
                val imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom
                listener.onKeyboardVisibilityChanged(imeVisible, imeHeight)
            }
        })*/
    }
}