package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources
import java.lang.Math.PI

open class UUBorderedImageButton(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageButton(context, attrs, defStyle)
{
    private val shape: UUPolygonShape by lazy()
    {
        UUPolygonShape(this,
            R.styleable.UUBorderedImageButton,
            R.styleable.UUBorderedImageButton_uuBorderWidth,
            R.styleable.UUBorderedImageButton_uuBorderColor,
            R.styleable.UUBorderedImageButton_uuFillColor,
            -1,
            R.styleable.UUBorderedImageButton_uuCornerRadius,
            -1)
    }

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init
    {
        shape.initAttributes(context, attrs)
        shape.sides = 4
        shape.rotationOffset = (PI / 4).toFloat()
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        super.onSizeChanged(w, h, oldw, oldh)
        shape.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas)
    {
        shape.preDraw(canvas)
        super.onDraw(canvas)
        shape.postDraw(canvas)
    }

    var borderColor: Int
        get() = shape.borderColor
        set(value) { shape.borderColor = value }

    var borderWidth: Float
        get() = shape.borderWidth
        set(value) { shape.borderWidth = value }

    var fillColor: Int
        get() = shape.fillColor
        set(value) { shape.fillColor = value }

    var cornerRadius: Float
        get() = shape.cornerRadius
        set(value) { shape.cornerRadius = value }
}

@BindingAdapter("uuBorderColor")
fun uuBindBorderColor(view: UUBorderedImageButton, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUBorderedImageButton, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimensionPixelSize(resourceId, 0).toFloat()
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUBorderedImageButton, value: Float)
{
    view.borderWidth = value
}

@BindingAdapter("uuFillColor")
fun uuBindFillColor(view: UUBorderedImageButton, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuCornerRadius")
fun uuBindPolygonCornerRadius(view: UUBorderedImageButton, cornerRadius: Float)
{
    view.cornerRadius = cornerRadius
}
