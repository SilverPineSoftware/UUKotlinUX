package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUCircularImageButton(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageButton(context, attrs, defStyle)
{
    private var _fillColor: Int = android.R.color.transparent
    private val clippingPath = Path()
    private var clipX: Float = 0.0f
    private var clipY: Float = 0.0f
    private var clipRadius: Float = 0.0f
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
            val a = context.obtainStyledAttributes(attrs, R.styleable.UUCircularImageButton)

            val borderWidth = a.getDimensionPixelSize(R.styleable.UUCircularImageButton_uuBorderWidth, -1)
            if (borderWidth != -1)
            {
                borderPaint.strokeWidth = borderWidth.toFloat()
            }

            val borderColor = a.getColor(R.styleable.UUCircularImageButton_uuBorderColor, -1)
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

    var borderColor: Int
        get() = borderPaint.color
        set(value) = internalSetBorderColor(value)

    var borderWidth: Float
        get() = borderPaint.strokeWidth
        set(value) = internalSetBorderWidth(value)

    var fillColor: Int
        get() = _fillColor
        set(value) = internalSetFillColor(value)

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

    private fun internalSetFillColor(@ColorRes color: Int)
    {
        _fillColor = color
        invalidate()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)
    {
        super.onLayout(changed, left, top, right, bottom)

        if (changed)
        {
            clippingPath.reset()
            val w = right - left
            val h = bottom - top
            clipX = w / 2.0f
            clipY = h / 2.0f
            clipRadius = w.coerceAtMost(h) / 2.0f
            clippingPath.addCircle(clipX, clipY, clipRadius, Path.Direction.CW)
        }
    }

    open fun preDraw(canvas: Canvas)
    {
        canvas.clipPath(clippingPath)
        canvas.drawColor(fillColor)
    }

    override fun onDraw(canvas: Canvas)
    {
        preDraw(canvas)
        super.onDraw(canvas)
        canvas.drawCircle(clipX, clipY, clipRadius, borderPaint)
    }
}

@BindingAdapter("uuBoundBorderColor")
fun uuBindBorderColor(view: UUCircularImageButton, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBoundBorderWidth")
fun uuBindBorderWidth(view: UUCircularImageButton, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimension(resourceId)
}

@BindingAdapter("uuBoundFillColor")
fun uuBindFillColor(view: UUCircularImageButton, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}