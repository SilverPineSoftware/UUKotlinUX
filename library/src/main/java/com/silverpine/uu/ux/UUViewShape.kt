package com.silverpine.uu.ux

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes

abstract class UUViewShape(
    protected val view: View,
    @StyleableRes private val attributesId: IntArray,
    @StyleableRes private val borderWidthAttributeId: Int,
    @StyleableRes private val borderColorAttributeId: Int,
    @StyleableRes private val fillColorAttributeId: Int)
{
    private var _fillColor: Int = Color.TRANSPARENT
    protected val borderPaint: Paint = Paint()

    init
    {
        borderPaint.isDither = true
        borderPaint.isAntiAlias = true
        borderPaint.color = Color.TRANSPARENT
        borderPaint.strokeWidth = 0.0f
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.style = Paint.Style.STROKE
    }

    fun initAttributes(context: Context, attributeSet: AttributeSet?)
    {
        val attrs = attributeSet ?: return

        val a = context.obtainStyledAttributes(attrs, attributesId)
        applyAttributes(a)
        a.recycle()
    }

    open fun applyAttributes(attributes: TypedArray)
    {
        borderWidth = attributes.uuGetDimensionPixelSize(borderWidthAttributeId, 0.0f)
        borderColor = attributes.uuGetColor(borderColorAttributeId, Color.TRANSPARENT)
        fillColor = attributes.uuGetColor(fillColorAttributeId, Color.TRANSPARENT)
    }

    var borderColor: Int
        get() = borderPaint.color
        set(value)
        {
            borderPaint.color = value
            view.invalidate()
        }

    var borderWidth: Float
        get() = borderPaint.strokeWidth
        set(value)
        {
            borderPaint.strokeWidth = value
            view.invalidate()
        }

    var fillColor: Int
        get() = _fillColor
        set(value)
        {
            _fillColor = value
            view.invalidate()
        }

    /**
     * Pre-Draw.  This is called before chaining to the super class onDraw
     *
     * @param canvas the drawing canvas
     *
     */
    abstract fun preDraw(canvas: Canvas)

    /**
     * Post-Draw.  This is called after chaining to the super class onDraw
     *
     * @param canvas the drawing canvas
     *
     */
    abstract fun postDraw(canvas: Canvas)

    /**
     * Handle the view's size changing
     *
     * @param w new view width
     * @param h new view height
     * @param oldWidth old view width
     * @param oldHeight old view height
     */
    abstract fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int)
}