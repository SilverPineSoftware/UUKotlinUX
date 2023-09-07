package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUPolygonView(context: Context, attrs: AttributeSet?, defStyle: Int): View(context, attrs, defStyle)
{
    private val shape: UUPolygonShape by lazy()
    {
        UUPolygonShape(this,
            R.styleable.UUPolygonView,
            R.styleable.UUPolygonView_uuBorderWidth,
            R.styleable.UUPolygonView_uuBorderColor,
            R.styleable.UUPolygonView_uuFillColor,
            R.styleable.UUPolygonView_uuSides,
            R.styleable.UUPolygonView_uuCornerRadius,
            R.styleable.UUPolygonView_uuRotationOffset)
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

@BindingAdapter("uuBorderColor")
fun uuBindBorderColor(view: UUPolygonView, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUPolygonView, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimension(resourceId)
}

@BindingAdapter("uuFillColor")
fun uuBindFillColor(view: UUPolygonView, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuSides")
fun uuBindPolygonSides(view: UUPolygonView, sides: Int)
{
    view.sides = sides
}

@BindingAdapter("uuCornerRadius")
fun uuBindPolygonCornerRadius(view: UUPolygonView, cornerRadius: Float)
{
    view.cornerRadius = cornerRadius
}

@BindingAdapter("uuRotationOffset")
fun uuBindPolygonRotationOffset(view: UUPolygonView, rotationOffset: Float)
{
    view.rotationOffset = rotationOffset
}