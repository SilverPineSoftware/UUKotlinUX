package com.silverpine.uu.ux.dragdrop

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("uuDragDropViewModel")
fun uuConfigureDragDropBindings(view: View?, viewModel: UUDragDropViewModel?)
{
    view?.uuConfigureDragDrop(viewModel)
}

@BindingAdapter("uuDropTargetViewModel")
fun uuConfigureDropTargetBindings(view: View?, viewModel: UUDragDropViewModel?)
{
    view?.uuConfigureDragDrop(viewModel, configureDragSupport = false)
}

fun View.uuConfigureDragDrop(
    viewModel: UUDragDropViewModel?,
    configureDragSupport: Boolean = true,
    configureDropSupport: Boolean = true)
{
    if (viewModel == null)
    {
        setOnLongClickListener(null)
        setOnDragListener(null)
        setOnClickListener(null)
        return
    }

    if (configureDragSupport)
    {
        setOnLongClickListener(UUViewModelDragLongClickListener(viewModel))
        setOnTouchListener(UUViewModelDragTouchListener(viewModel))
    }

    if (configureDropSupport)
    {
        setOnDragListener(UUViewModelDragListener(viewModel))
    }
}