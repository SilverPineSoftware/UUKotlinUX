package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUCircularImageButton(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageButton(context, attrs, defStyle)
{
    private val shape: UUCircularShape by lazy()
    {
        UUCircularShape(this,
            R.styleable.UUCircularImageButton,
            R.styleable.UUCircularImageButton_uuBorderWidth,
            R.styleable.UUCircularImageButton_uuBorderColor,
            R.styleable.UUCircularImageButton_uuFillColor)
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
fun uuBindBorderColor(view: UUCircularImageButton, @ColorRes resourceId: Int)
{
    view.borderColor = UUResources.getColor(resourceId)
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUCircularImageButton, @DimenRes resourceId: Int)
{
    view.borderWidth = UUResources.getDimensionPixelSize(resourceId, 0).toFloat()
}

@BindingAdapter("uuBorderWidth")
fun uuBindBorderWidth(view: UUCircularImageButton, value: Float)
{
    view.borderWidth = value
}

@BindingAdapter("uuFillColor")
fun uuBindFillColor(view: UUCircularImageButton, @ColorRes resourceId: Int)
{
    view.fillColor = UUResources.getColor(resourceId)
}