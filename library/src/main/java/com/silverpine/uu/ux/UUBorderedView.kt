package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUBorderedView(context: Context, attrs: AttributeSet?, defStyle: Int): View(context, attrs, defStyle)
{
    private val shape: UURectangleShape by lazy()
    {
        UURectangleShape(this,
            R.styleable.UUBorderedView,
            R.styleable.UUBorderedView_uuBorderWidth,
            R.styleable.UUBorderedView_uuBorderColor,
            R.styleable.UUBorderedView_uuFillColor,
            R.styleable.UUBorderedView_uuCornerRadius)
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
}

@BindingAdapter("uuBorderColor")
fun uuBindBorderColor(view: UUBorderedView, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUBorderedView, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimensionPixelSize(resourceId, 0).toFloat()
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUBorderedView, value: Float)
{
    view.borderWidth = value
}

@BindingAdapter("uuFillColor")
fun uuBindFillColor(view: UUBorderedView, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}

