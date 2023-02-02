package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter

class UUBorderedImageView(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageButton(context, attrs, defStyle)
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
            val a = context.obtainStyledAttributes(attrs, R.styleable.UUBorderedImageView)
            val borderWidth = a.getDimensionPixelSize(R.styleable.UUBorderedImageView_uuBorderWidth, -1)
            val borderColor = a.getColor(R.styleable.UUBorderedImageView_uuBorderColor, -1)

            if (borderColor != -1 && borderWidth != -1)
            {
                borderPaint.color = borderColor
                borderPaint.strokeWidth = borderWidth.toFloat()
            }

            a.recycle()
        }
    }

    fun setBorderColor(color: Int)
    {
        borderPaint.color = color
        invalidate()
    }

    fun setBorderWidth(width: Float)
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

@BindingAdapter(value = ["bind:uuBorderColor", "bind:uuBorderWidth"], requireAll = true)
fun uuConfigureBindings(view: UUBorderedImageView, color: Int?, width: Float?)
{
    color?.let()
    {
        view.setBorderColor(it)
    }

    width?.let()
    {
        view.setBorderWidth(width)
    }
}