package com.silverpine.uu.ux.dragdrop

import android.content.ClipData
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
        if (viewModel.dragTriggerType == UUDragTriggerType.TouchDown)
        {
            if (event?.action == MotionEvent.ACTION_DOWN)
            {
                val dragData = ClipData(viewModel.name, arrayOf(viewModel.mimeType), ClipData.Item(viewModel.name))
                v?.startDragAndDrop(dragData, shadowBuilder.invoke(v), viewModel, 0)
                return true
            }
        }

        return false
    }
}