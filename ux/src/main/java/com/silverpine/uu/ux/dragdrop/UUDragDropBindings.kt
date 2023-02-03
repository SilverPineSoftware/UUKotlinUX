package com.silverpine.uu.ux.dragdrop

import android.view.View
import androidx.databinding.BindingAdapter
import com.silverpine.uu.ux.uuClearDragDrop

@BindingAdapter("uuDragDropViewModel")
fun uuConfigureDropBindings(view: View?, viewModel: UUDragDropViewModel?)
{
    view?.uuConfigureDragDrop(viewModel)
}

fun View.uuConfigureDragDrop(viewModel: UUDragDropViewModel?)
{
    if (viewModel == null)
    {
        uuClearDragDrop()
        return
    }

    tag = viewModel.id
    setOnLongClickListener(UUViewModelDragLongClickListener(viewModel))
    setOnDragListener(UUViewModelDragListener(viewModel))
}