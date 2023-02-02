package com.silverpine.uu.sample.ux.dragdrop

import android.content.ClipData
import android.view.DragEvent
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.uuGetOrNull
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.ux.UUBorderedImageView
import com.silverpine.uu.ux.UUImageViewDragShadowBuilder
import com.silverpine.uu.ux.uuClearDragDrop

class DropManagerViewModel: ViewModel()
{
    var data: LiveData<ArrayList<DropViewModel>> = MutableLiveData(
        arrayListOf(
            DropViewModel(true, DropModel("one")),
            DropViewModel(true, DropModel("two")),
            DropViewModel(false, DropModel("three")),
            DropViewModel(true, DropModel("four"))
        )
    )

    var onFade: (DropViewModel, Float, Long)->Unit = { _,_,_ -> }

    fun update()
    {
        data.value?.uuGetOrNull(0)?.update(R.drawable.one)
        data.value?.uuGetOrNull(1)?.update(R.drawable.two)
        data.value?.uuGetOrNull(2)?.update(R.drawable.three)
        data.value?.uuGetOrNull(3)?.update(R.drawable.four)

        data.value?.forEach { it.onFade = onFade }
    }
}
private const val CUSTOM_DROP_MIME_TYPE = "UU/CustomDropItem"

@BindingAdapter(value = ["uuDropViewModel", "uuDropManagerViewModel"], requireAll = true)
fun uuConfigureDropBindings(view: UUBorderedImageView?, viewModel: DropViewModel?, dropManagerViewModel: DropManagerViewModel?)
{
    view?.configureDragDrop(viewModel, dropManagerViewModel)
}

private fun UUBorderedImageView.configureDragDrop(viewModel: DropViewModel?, dropManagerViewModel: DropManagerViewModel?)
{
    if (viewModel == null)
    {
        uuClearDragDrop()
        return
    }

    if (!viewModel.allowDrop)
    {
        uuClearDragDrop()
        return
    }

    tag = viewModel.id

    viewModel.model?.let()
    { sourceItem ->
        setOnLongClickListener()
        { v ->

            val dragData = ClipData(sourceItem.name, arrayOf(CUSTOM_DROP_MIME_TYPE), ClipData.Item(sourceItem.name))

            v.startDragAndDrop(dragData, UUImageViewDragShadowBuilder(this), viewModel, 0)
            true
        }
    }

    if (viewModel.allowDrop && dropManagerViewModel != null)
    {
        this.configureDropTarget(viewModel, dropManagerViewModel)
    }
}

private fun UUBorderedImageView.configureDropTarget(dropTargetViewModel: DropViewModel, dropManagerViewModel: DropManagerViewModel)
{
    setOnDragListener()
    { v, e ->

        val dragViewModel = e.localState as? DropViewModel

        if (dragViewModel == null)
        {
            false
        }
        else
        {
//            val viewModel = e.localState as? DropViewModel
//            val viewTag = (v.tag as? String) ?: ""
//            val vmTag = viewModel?.id
//            val isTagSame = (viewTag == vmTag)

            // Handles each of the expected events.
            when (e.action)
            {
                DragEvent.ACTION_DRAG_STARTED ->
                {
                    // Determines if this View can accept the dragged data.
                    if (e.clipDescription.hasMimeType(CUSTOM_DROP_MIME_TYPE))
                    {
                        dropTargetViewModel.handleDragStart(dragViewModel)
                        true
                    }
                    else
                    {
                        // Returns false to indicate that, during the current drag and drop operation,
                        // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                        UULog.d(javaClass, "configureDropTarget", "Ignoring Drag events, MIME type mismatch")
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
                    UULog.d(javaClass,"configureDropTarget", "Unknown action type received by View.OnDragListener.")
                    false
                }
            }
        }
    }
}
