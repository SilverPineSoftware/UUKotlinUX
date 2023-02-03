package com.silverpine.uu.ux.dragdrop

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap

open class UUDragShadowBuilder(
    v: View,
    private val scaleFactor: Float = 1.0f,
    private val shadowDrawable: Drawable? = v.drawToBitmap().toDrawable(v.resources)) : View.DragShadowBuilder(v)
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