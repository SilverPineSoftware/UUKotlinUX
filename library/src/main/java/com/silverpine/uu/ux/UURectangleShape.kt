package com.silverpine.uu.ux

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.StyleableRes

open class UURectangleShape(view: View,
                            @StyleableRes attributesId: IntArray,
                            @StyleableRes borderWidthAttributeId: Int,
                            @StyleableRes borderColorAttributeId: Int,
                            @StyleableRes fillColorAttributeId: Int): UUViewShape(view, attributesId, borderWidthAttributeId, borderColorAttributeId, fillColorAttributeId)
{
    private var borderRect: Rect = Rect(0,0,0,0)

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int)
    {
        borderRect.right = w
        borderRect.bottom = h
    }

    override fun preDraw(canvas: Canvas)
    {
        canvas.drawColor(fillColor)
    }

    override fun postDraw(canvas: Canvas)
    {
        canvas.drawRect(borderRect, borderPaint)
    }
}