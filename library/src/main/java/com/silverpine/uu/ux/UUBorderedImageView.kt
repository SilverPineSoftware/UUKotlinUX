package com.silverpine.uu.ux

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.silverpine.uu.core.UUResources

open class UUBorderedImageView(context: Context, attrs: AttributeSet?, defStyle: Int): AppCompatImageView(context, attrs, defStyle)
{
    private val shape: UURectangleShape by lazy()
    {
        UURectangleShape(this,
            R.styleable.UUBorderedImageView,
            R.styleable.UUBorderedImageView_uuBorderWidth,
            R.styleable.UUBorderedImageView_uuBorderColor,
            R.styleable.UUBorderedImageView_uuFillColor)
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
