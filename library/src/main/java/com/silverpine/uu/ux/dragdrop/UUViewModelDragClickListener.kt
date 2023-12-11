package com.silverpine.uu.ux.dragdrop

import android.view.View

class UUViewModelDragClickListener(private val viewModel: UUDragDropViewModel): View.OnClickListener
{
    companion object
    {
        var shadowBuilder: (View)->View.DragShadowBuilder = { v -> UUDragShadowBuilder(v) }
    }

    override fun onClick(v: View?)
    {
        //UULog.d(javaClass, "onClick", "v: $v")
        viewModel.onClick()
    }
}