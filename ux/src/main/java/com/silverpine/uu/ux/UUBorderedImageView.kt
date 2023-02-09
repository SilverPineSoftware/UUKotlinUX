package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUBorderedImageView(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageView(context, attrs, defStyle)
{
    private var _fillColor: Int = resources.getColor(android.R.color.transparent, null)
    private var borderRect: Rect = Rect(0,0,0,0)
    private val borderPaint: Paint = Paint()

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init
    {
        borderPaint.isDither = true
        borderPaint.isAntiAlias = true
        borderPaint.color = Color.TRANSPARENT
        borderPaint.strokeWidth = 0.0f
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.style = Paint.Style.STROKE

        attrs?.let()
        {
            val a = context.obtainStyledAttributes(attrs, R.styleable.UUBorderedImageView)

            val borderWidth = a.getDimensionPixelSize(R.styleable.UUBorderedImageView_uuBorderWidth, -1)
            if (borderWidth != -1)
            {
                borderPaint.strokeWidth = borderWidth.toFloat()
            }

            val borderColor = a.getColor(R.styleable.UUBorderedImageView_uuBorderColor, -1)
            if (borderColor != -1)
            {
                borderPaint.color = borderColor
            }

            val fillColor = a.getColor(R.styleable.UUCircularImageButton_uuFillColor, -1)
            if (fillColor != -1)
            {
                this.fillColor = fillColor
            }

            a.recycle()
        }
    }

    var fillColor: Int
        get() = _fillColor
        set(value) = internalSetFillColor(value)

    var borderColor: Int
        get() = borderPaint.color
        set(value) = internalSetBorderColor(value)

    var borderWidth: Float
        get() = borderPaint.strokeWidth
        set(value) = internalSetBorderWidth(value)

    private fun internalSetFillColor(@ColorRes color: Int)
    {
        _fillColor = resources.getColor(color, null)
        invalidate()
    }

    private fun internalSetBorderColor(color: Int)
    {
        borderPaint.color = color
        invalidate()
    }

    private fun internalSetBorderWidth(width: Float)
    {
        borderPaint.strokeWidth = width
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)
    {
        super.onLayout(changed, left, top, right, bottom)

        if (changed)
        {
            borderRect.right = (right - left)
            borderRect.bottom = (bottom - top)
        }
    }

    open fun preDraw(canvas: Canvas)
    {
        canvas.drawColor(fillColor)
    }

    override fun onDraw(canvas: Canvas)
    {
        preDraw(canvas)
        super.onDraw(canvas)
        canvas.drawRect(borderRect, borderPaint)
    }
}

@BindingAdapter("uuBoundBorderColor")
fun uuBindBorderColor(view: UUBorderedImageView, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBoundBorderWidth")
fun uuBindBorderWidth(view: UUBorderedImageView, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimension(resourceId)
}

@BindingAdapter("uuBoundFillColor")
fun uuBindFillColor(view: UUBorderedImageView, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}
