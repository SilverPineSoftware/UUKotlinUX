package com.silverpine.uu.ux.dragdrop

import android.content.ClipData
import android.view.View

class UUViewModelDragLongClickListener(private val viewModel: UUDragDropViewModel): View.OnLongClickListener
{
    companion object
    {
        var shadowBuilder: (View)->View.DragShadowBuilder = { v -> UUDragShadowBuilder(v) }
    }

    override fun onLongClick(v: View?): Boolean
    {
        //UULog.d(javaClass, "onLongClick", "v: $v")

        return if (viewModel.dragTriggerType == UUDragTriggerType.LongPress)
        {
            val dragData = ClipData(viewModel.name, arrayOf(viewModel.mimeType), ClipData.Item(viewModel.name))
            v?.startDragAndDrop(dragData, shadowBuilder.invoke(v), viewModel, 0)
            true
        }
        else
        {
            false
        }
    }
}