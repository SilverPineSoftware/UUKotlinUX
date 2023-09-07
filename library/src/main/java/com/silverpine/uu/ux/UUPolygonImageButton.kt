package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUPolygonImageButton(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageButton(context, attrs, defStyle)
{
    private val shape: UUPolygonShape by lazy()
    {
        UUPolygonShape(this,
            R.styleable.UUPolygonImageButton,
            R.styleable.UUPolygonImageButton_uuBorderWidth,
            R.styleable.UUPolygonImageButton_uuBorderColor,
            R.styleable.UUPolygonImageButton_uuFillColor,
            R.styleable.UUPolygonImageButton_uuSides,
            R.styleable.UUPolygonImageButton_uuCornerRadius,
            R.styleable.UUPolygonImageButton_uuRotationOffset)
    }

    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init
    {
        shape.initAttributes(context, attrs)
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

    var sides: Int
        get() = shape.sides
        set(value) { shape.sides = value }

    var cornerRadius: Float
        get() = shape.cornerRadius
        set(value) { shape.cornerRadius = value }

    var rotationOffset: Float
        get() = shape.rotationOffset
        set(value) { shape.rotationOffset = value }
}

@BindingAdapter("uuBoundBorderColor")
fun uuBindBorderColor(view: UUPolygonImageButton, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBoundBorderWidth")
fun uuBindBorderWidth(view: UUPolygonImageButton, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimension(resourceId)
}

@BindingAdapter("uuBoundFillColor")
fun uuBindFillColor(view: UUPolygonImageButton, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBoundSides")
fun uuBindPolygonSides(view: UUPolygonImageButton, sides: Int)
{
    view.sides = sides
}

@BindingAdapter("uuBoundCornerRadius")
fun uuBindPolygonCornerRadius(view: UUPolygonImageButton, cornerRadius: Float)
{
    view.cornerRadius = cornerRadius
}

@BindingAdapter("uuBoundRotationOffset")
fun uuBindPolygonRotationOffset(view: UUPolygonImageButton, rotationOffset: Float)
{
    view.rotationOffset = rotationOffset
}