package com.silverpine.uu.sample.ux.dragdrop

import android.content.ClipData
import android.view.DragEvent
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.core.uuGetOrNull
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.ux.UUBorderedImageView
import com.silverpine.uu.ux.UUImageViewDragShadowBuilder
import com.silverpine.uu.ux.uuClearDragDrop

class DropManagerViewModel: ViewModel()
{
    var data: LiveData<ArrayList<DropViewModel>> = MutableLiveData(arrayListOf(
        DropViewModel(true, DropModel("one")),
        DropViewModel(true, DropModel("two")),
        DropViewModel(false, DropModel("three")),
        DropViewModel(true, DropModel("four"))
    ))

    fun update()
    {
        data.value?.uuGetOrNull(0)?.apply()
        {
            changeBorderColor(UUResources.getColor(android.R.color.holo_red_dark))
            changeBorderWidth(5.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }

        data.value?.uuGetOrNull(1)?.apply()
        {
            changeBorderColor(UUResources.getColor(android.R.color.holo_orange_light))
            changeBorderWidth(5.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }

        data.value?.uuGetOrNull(2)?.apply()
        {
            changeBorderColor(UUResources.getColor(android.R.color.holo_green_light))
            changeBorderWidth(80.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }

        data.value?.uuGetOrNull(3)?.apply()
        {
            changeBorderColor(UUResources.getColor(R.color.purple_500))
            changeBorderWidth(5.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }
    }

    fun dispatchDrop(source: DropViewModel?, dest: DropViewModel)
    {

    }

    fun dispatchDragStart(id: String)
    {
        data.value?.filter { it.id == id }?.forEach()
        { vm ->
            vm.handleDragStart()
        }
    }

    fun dispatchDragEnd()
    {
        data.value?.forEach()
        { vm ->
            vm.handleDragEnd()
        }
    }

    fun dispatchDragEnter(id: String)
    {
        data.value?.filter { it.id == id }?.forEach()
        { vm ->
            vm.handleDragEnter()
        }
    }

    fun dispatchDragExit(id: String)
    {
        data.value?.filter { it.id == id }?.forEach()
        { vm ->
            vm.handleDragExit()
        }
    }
}

private const val CUSTOM_DROP_MIME_TYPE = "UU/CustomDropItem"

@BindingAdapter(value = ["bind:uuDropViewModel", "bind:uuDropManagerViewModel"], requireAll = true)
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

private fun UUBorderedImageView.configureDropTarget(destViewModel: DropViewModel, dropManagerViewModel: DropManagerViewModel)
{
    setOnDragListener()
    { v, e ->

        val viewModel = e.localState as? DropViewModel
        val viewTag = (v.tag as? String) ?: ""
        val vmTag = viewModel?.id
        val isTagSame = (viewTag == vmTag)

        // Handles each of the expected events.
        when (e.action)
        {
            DragEvent.ACTION_DRAG_STARTED ->
            {
                // Determines if this View can accept the dragged data.
                if (e.clipDescription.hasMimeType(CUSTOM_DROP_MIME_TYPE))
                {
                    dropManagerViewModel.dispatchDragStart(viewTag)

                    /*
                    if (isTagSame)
                    {
                        uuFadeAlpha(0.2f, 200L)
                    }
                    else
                    {
                        dropManagerViewModel.dispatchDragStart(viewTag)*/
                    //viewModel?.handleDragAccept()

                    /*v.apply()
                    {
                        setBorderColor(resources.getColor(R.color.drop_accept_border, null))
                        setBorderWidth(20.0f)
                        invalidate()
                    }*/
                    //}

                    //v.invalidate()
                    true
                }
                else
                {
                    // Returns false to indicate that, during the current drag and drop operation,
                    // this View will not receive events again until ACTION_DRAG_ENDED is sent.
                    false
                }
            }

            DragEvent.ACTION_DRAG_ENTERED ->
            {
                // Applies a green tint to the View.
                //(v as? ImageView)?.setColorFilter(Color.GREEN)
                //if (!isTagSame)
                //{
//                    v.apply()
//                    {
//                        setBorderColor(resources.getColor(R.color.drop_hover_border, null))
//                        setBorderWidth(40.0f)
//                        invalidate()
//                    }

                //viewModel?.handleDragEnter()
                dropManagerViewModel.dispatchDragEnter(viewTag)
                //}

                // Returns true; the value is ignored.
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
            {    // Ignore the event.
                true
            }

            DragEvent.ACTION_DRAG_EXITED ->
            {
                //if (!isTagSame)
                // {
//                    v.apply()
//                    {
//                        setBorderColor(resources.getColor(R.color.drop_accept_border, null))
//                        setBorderWidth(20.0f)
//                        invalidate()
//                    }

                dropManagerViewModel.dispatchDragExit(viewTag)
                //viewModel?.handleDragExit()
                //}

                // Returns true; the value is ignored.
                true
            }

            DragEvent.ACTION_DROP ->
            {
                UULog.d(javaClass, "configureDropTarget", "DROP")

                //if (!isTagSame)
                //{
//                    v.apply()
//                    {
//                        setBorderColor(resources.getColor(R.color.transparent, null))
//                        setBorderWidth(0.0f)
//                        invalidate()
//                    }

                //viewModel?.handleDragReset()
                dropManagerViewModel.dispatchDragEnd()

//                    viewModel?.let()
//                    {
//                        dropManagerViewModel.handleDrop(viewModel, destViewModel)
//                    }

                dropManagerViewModel.dispatchDrop(viewModel, destViewModel)
                //}

                true
            }

            DragEvent.ACTION_DRAG_ENDED ->
            {
                UULog.d(javaClass, "configureDropTarget", "DRAG_ENDED, isTagSame: $isTagSame")
                dropManagerViewModel.dispatchDragEnd()

                /*
                if (!isTagSame)
                {
//                    v.apply()
//                    {
//                        setBorderColor(resources.getColor(R.color.transparent, null))
//                        setBorderWidth(0.0f)
//                        invalidate()
//                    }

                    //viewModel?.handleDragEnd()

                    dropManagerViewModel.dispatchDragEnd()
                }
                else
                {
                    v.uuFadeAlpha(1.0f, 200L)
                }*/

                // Returns true; the value is ignored.
                true
            }

            else ->
            {
                // An unknown action type was received.
                UULog.d(javaClass, "configureDropTarget", "Unknown action type received by View.OnDragListener.")
                false
            }
        }
    }
}
