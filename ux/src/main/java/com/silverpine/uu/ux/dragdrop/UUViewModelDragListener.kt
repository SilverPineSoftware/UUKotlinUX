package com.silverpine.uu.ux.dragdrop

import android.view.DragEvent
import android.view.View

open class UUViewModelDragListener(private val dropTargetViewModel: UUDragDropViewModel): View.OnDragListener
{
    override fun onDrag(v: View?, event: DragEvent?): Boolean
    {
        val dragViewModel = (event?.localState as? UUDragDropViewModel) ?: return false
        if (!dropTargetViewModel.allowDrop)
        {
            return false
        }

        return when (event.action)
        {
            DragEvent.ACTION_DRAG_STARTED ->
            {
                // Determines if this View can accept the dragged data.
                if (event.clipDescription.hasMimeType(dropTargetViewModel.mimeType))
                {
                    dropTargetViewModel.handleDragStart(dragViewModel)
                    true
                }
                else
                {
                    false
                }
            }

            DragEvent.ACTION_DRAG_ENTERED ->
            {
                dropTargetViewModel.handleDragEnter(dragViewModel)
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
            {
                true
            }

            DragEvent.ACTION_DRAG_EXITED ->
            {
                dropTargetViewModel.handleDragExit(dragViewModel)
                true
            }

            DragEvent.ACTION_DROP ->
            {
                dropTargetViewModel.handleDrop(dragViewModel)
                true
            }

            DragEvent.ACTION_DRAG_ENDED ->
            {
                dropTargetViewModel.handleDragEnd(dragViewModel)
                true
            }

            else ->
            {
                false
            }
        }
    }
}