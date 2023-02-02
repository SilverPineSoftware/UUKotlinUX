package com.silverpine.uu.ux

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView

class UUImageViewDragShadowBuilder(
    v: ImageView,
    private val scaleFactor: Float = 1.0f,
    private val shadowDrawable: Drawable? = v.drawable.constantState?.newDrawable()) : View.DragShadowBuilder(v)
{
    override fun onProvideShadowMetrics(size: Point, touch: Point)
    {
        val width: Int = (view.width.toFloat() * scaleFactor).toInt()
        val height: Int = (view.height.toFloat() * scaleFactor).toInt()
        shadowDrawable?.setBounds(0, 0, width, height)
        size.set(width, height)
        touch.set(width / 2, height / 2)
    }

    override fun onDrawShadow(canvas: Canvas)
    {
        shadowDrawable?.draw(canvas)
    }
}