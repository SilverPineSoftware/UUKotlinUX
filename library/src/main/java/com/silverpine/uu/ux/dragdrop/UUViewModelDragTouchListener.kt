package com.silverpine.uu.ux.dragdrop

import android.content.ClipData
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

class UUViewModelDragTouchListener(private val viewModel: UUDragDropViewModel): View.OnTouchListener
{
    companion object
    {
        var shadowBuilder: (View)->View.DragShadowBuilder = { v -> UUDragShadowBuilder(v) }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean
    {
        val view = v ?: return false
        val e = event ?: return false

        val duration = SystemClock.uptimeMillis() - e.downTime
        //UULog.d(javaClass, "onTouch", "action: ${e.action}, $duration")

        if (viewModel.dragTriggerType == UUDragTriggerType.TouchDown)
        {
            if (e.action == MotionEvent.ACTION_MOVE && duration > viewModel.longPressTriggerTime)
            {
                val dragData = ClipData(viewModel.name, arrayOf(viewModel.mimeType), ClipData.Item(viewModel.name))
                v?.startDragAndDrop(dragData, shadowBuilder.invoke(v), viewModel, 0)
                return true
            }
        }

        if (e.action == MotionEvent.ACTION_UP)
        {
            //UULog.d(javaClass, "onTouch", "action: ${e.action}, $duration, calling performClick method")
            return view.performClick()
        }

        return false
    }
}