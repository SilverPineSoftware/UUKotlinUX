package com.silverpine.uu.ux

import android.graphics.Canvas
import android.graphics.Path
import android.view.View
import androidx.annotation.StyleableRes

open class UUCircularShape(view: View,
                           @StyleableRes attributesId: IntArray,
                           @StyleableRes borderWidthAttributeId: Int,
                           @StyleableRes borderColorAttributeId: Int,
                           @StyleableRes fillColorAttributeId: Int): UUViewShape(view, attributesId, borderWidthAttributeId, borderColorAttributeId, fillColorAttributeId)
{
    private val clippingPath = Path()
    private var clipX: Float = 0.0f
    private var clipY: Float = 0.0f
    private var clipRadius: Float = 0.0f

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int)
    {
        clippingPath.reset()
        clipX = w / 2.0f
        clipY = h / 2.0f
        clipRadius = w.coerceAtMost(h) / 2.0f
        clippingPath.addCircle(clipX, clipY, clipRadius, Path.Direction.CW)
    }

    override fun preDraw(canvas: Canvas)
    {
        canvas.clipPath(clippingPath)
        canvas.drawColor(fillColor)
    }

    override fun postDraw(canvas: Canvas)
    {
        canvas.drawCircle(clipX, clipY, clipRadius, borderPaint)
    }
}