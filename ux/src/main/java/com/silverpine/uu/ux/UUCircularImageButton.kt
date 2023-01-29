package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

class UUCircularImageButton(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageButton(context, attrs, defStyle)
{
    private val clippingPath = Path()
    private var clipX: Float = 0.0f
    private var clipY: Float = 0.0f
    private var clipRadius: Float = 0.0f
    private val borderPaint: Paint = Paint()

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init
    {
        attrs?.let()
        {
            val a = context.obtainStyledAttributes(attrs, R.styleable.UUCircularImageButton)
            val borderWidth = a.getDimensionPixelSize(R.styleable.UUCircularImageButton_borderWidth, -1)
            val borderColor = a.getColor(R.styleable.UUCircularImageButton_borderColor, -1)

            if (borderColor != -1 && borderWidth != -1)
            {
                borderPaint.isDither = true
                borderPaint.isAntiAlias = true
                borderPaint.color = borderColor
                borderPaint.strokeWidth = borderWidth.toFloat()
                borderPaint.strokeCap = Paint.Cap.ROUND
                borderPaint.style = Paint.Style.STROKE
            }

            a.recycle()
        }
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

    override fun onDraw(canvas: Canvas)
    {
        canvas.clipPath(clippingPath)
        super.onDraw(canvas)
        canvas.drawCircle(clipX, clipY, clipRadius, borderPaint)
    }
}