package com.silverpine.uu.ux

import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.view.View
import androidx.annotation.StyleableRes
import kotlin.math.cos
import kotlin.math.sin

open class UUPolygonShape(view: View,
                          @StyleableRes attributesId: IntArray,
                          @StyleableRes borderWidthAttributeId: Int,
                          @StyleableRes borderColorAttributeId: Int,
                          @StyleableRes fillColorAttributeId: Int,
                          @StyleableRes private val sidesAttributeId: Int,
                          @StyleableRes private val cornerRadiusAttributeId: Int,
                          @StyleableRes private val rotationOffsetAttributeId: Int): UUViewShape(view, attributesId, borderWidthAttributeId, borderColorAttributeId, fillColorAttributeId)
{
    companion object
    {
        private const val DEFAULT_SIDES = 4
        private const val DEFAULT_CORNER_RADIUS = 0.0f
        private const val DEFAULT_ROTATION_OFFSET = 0.0f
    }

    private val borderPath = Path()
    private var _sides: Int = DEFAULT_SIDES
    private var _cornerRadius: Float = DEFAULT_CORNER_RADIUS
    private var _rotationOffset: Float = DEFAULT_ROTATION_OFFSET

    var sides: Int
        get() = _sides
        set(value)
        {
            _sides = value
            rebuildBorderPath(view.width, view.height)
        }

    var cornerRadius: Float
        get() = _cornerRadius
        set(value)
        {
            _cornerRadius = value
            rebuildBorderPath(view.width, view.height)
        }

    var rotationOffset: Float
        get() = _rotationOffset
        set(value)
        {
            _rotationOffset = value
            rebuildBorderPath(view.width, view.height)
        }

    val borderPathBounds: RectF
        get()
        {
            val bounds = RectF()
            borderPath.computeBounds(bounds, true)
            return bounds
        }

    override fun applyAttributes(attributes: TypedArray)
    {
        super.applyAttributes(attributes)

        sides = attributes.uuGetInteger(sidesAttributeId, DEFAULT_SIDES)
        cornerRadius = attributes.uuGetFloat(cornerRadiusAttributeId, DEFAULT_CORNER_RADIUS)
        rotationOffset = attributes.uuGetFloat(rotationOffsetAttributeId, DEFAULT_ROTATION_OFFSET)
    }

    private fun rebuildBorderPath(w: Int, h: Int)
    {
        borderPath.uuMakePolygonPath(w.toFloat(), h.toFloat(), borderWidth, _sides, _cornerRadius, _rotationOffset)
        view.invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int)
    {
        rebuildBorderPath(w, h)
    }

    override fun preDraw(canvas: Canvas)
    {
        canvas.clipPath(borderPath)
        canvas.drawColor(fillColor)
    }

    override fun postDraw(canvas: Canvas)
    {
        canvas.drawPath(borderPath, borderPaint)
    }
}

fun Path.uuMakePolygonPath(width: Float, height: Float, borderWidth: Float, sides: Int, cornerRadius: Float, rotationOffset: Float = 0.0f): Array<PointF>
{
    reset()

    val rect = RectF(0.0f, 0.0f, width, height)
    val theta: Float = ((2.0f * Math.PI) / sides).toFloat() // How much to turn at every corner

    val center = PointF(rect.centerX(), rect.centerY())

    // Radius of the circle that encircles the polygon
    // Notice that the radius is adjusted for the corners, that way the largest outer
    // dimension of the resulting shape is always exactly the width - lineWidth
    val radius = (width - borderWidth + cornerRadius - (cos(theta) * cornerRadius)) / 2.0

    // Start drawing at a point, which by default is at the right hand edge
    // but can be offset
    var angle = rotationOffset

    val tips = arrayListOf<PointF>()

    var corner = PointF((center.x + (radius - cornerRadius) * cos(angle)).toFloat(), (center.y + (radius - cornerRadius) * sin(angle)).toFloat())
    moveTo(corner.x + cornerRadius * cos(angle + theta), corner.y + cornerRadius * sin(angle + theta))

    for (i in 0 until sides)
    {
        angle += theta

        corner = PointF((center.x + (radius - cornerRadius) * cos(angle)).toFloat(), (center.y + (radius - cornerRadius) * sin(angle)).toFloat())
        val tip = PointF((center.x + radius * cos(angle)).toFloat(), (center.y + radius * sin(angle)).toFloat())
        val start = PointF(corner.x + cornerRadius * cos(angle - theta), corner.y + cornerRadius * sin(angle - theta))
        val end = PointF(corner.x + cornerRadius * cos(angle + theta), corner.y + cornerRadius * sin(angle + theta))

        lineTo(start.x, start.y)
        quadTo(tip.x, tip.y, end.x, end.y)
        tips.add(tip)
    }

    close()

    return tips.toTypedArray()
}
