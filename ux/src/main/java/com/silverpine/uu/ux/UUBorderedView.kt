package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUBorderedView(context: Context, attrs: AttributeSet?, defStyle: Int): View(context, attrs, defStyle)
{
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
            val a = context.obtainStyledAttributes(attrs, R.styleable.UUBorderedView)
            val borderWidth = a.getDimensionPixelSize(R.styleable.UUBorderedView_uuBorderWidth, -1)
            val borderColor = a.getColor(R.styleable.UUBorderedView_uuBorderColor, -1)

            if (borderColor != -1 && borderWidth != -1)
            {
                borderPaint.color = borderColor
                borderPaint.strokeWidth = borderWidth.toFloat()
            }

            a.recycle()
        }
    }

    var borderColor: Int
        get() = borderPaint.color
        set(value) = internalSetBorderColor(value)

    var borderWidth: Float
        get() = borderPaint.strokeWidth
        set(value) = internalSetBorderWidth(value)

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

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)
        canvas.drawRect(borderRect, borderPaint)
    }
}

@BindingAdapter("uuBoundBorderColor")
fun uuBindBorderColor(view: UUBorderedView, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBoundBorderWidth")
fun uuBindBorderWidth(view: UUBorderedView, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimension(resourceId)
}
