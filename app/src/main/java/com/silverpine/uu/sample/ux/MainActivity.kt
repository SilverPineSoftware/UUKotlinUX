package com.silverpine.uu.sample.ux

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.databinding.ActivityMainBinding
import com.silverpine.uu.ux.UUBorderedImageView
import com.silverpine.uu.ux.UUImageViewDragShadowBuilder
import com.silverpine.uu.ux.uuClearDragDrop
import com.silverpine.uu.ux.uuFadeAlpha

class MainActivity : AppCompatActivity()
{
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        UUResources.init(applicationContext)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        viewModel.update()
        val buttonOne = findViewById<UUBorderedImageView>(R.id.one)
        val buttonTwo = findViewById<UUBorderedImageView>(R.id.two)
        val buttonThree = findViewById<UUBorderedImageView>(R.id.three)
        val buttonFour = findViewById<UUBorderedImageView>(R.id.four)

        buttonOne.configureDragDrop(DropViewModel(true, DropModel("One")), viewModel)
        buttonTwo.configureDragDrop(DropViewModel(true, DropModel("Two")), viewModel)
        buttonThree.configureDragDrop(DropViewModel(false, DropModel("Three")), viewModel)
        buttonFour.configureDragDrop(DropViewModel(true, DropModel("Four")), viewModel)
    }
}

private const val CUSTOM_DROP_MIME_TYPE = "UU/CustomDropItem"


fun UUBorderedImageView.configureDragDrop(viewModel: DropViewModel?, dropManagerViewModel: MainViewModel?)
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
        configureDropTarget2(viewModel, dropManagerViewModel)
    }
}

private fun UUBorderedImageView.configureDropTarget(destViewModel: DropViewModel, dropManagerViewModel: MainViewModel)
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



private fun UUBorderedImageView.configureDropTarget2(destViewModel: DropViewModel, dropManagerViewModel: MainViewModel)
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
                    if (isTagSame)
                    {
                        uuFadeAlpha(0.2f, 200L)
                    }
                    else
                    {
                        v.apply()
                        {
                            setBorderColor(resources.getColor(R.color.drop_accept_border, null))
                            setBorderWidth(20.0f)
                            invalidate()
                        }
                    }

                    v.invalidate()
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
                if (!isTagSame)
                {
                    v.apply()
                    {
                        setBorderColor(resources.getColor(R.color.drop_hover_border, null))
                        setBorderWidth(40.0f)
                        invalidate()
                    }
                }

                // Returns true; the value is ignored.
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
            {    // Ignore the event.
                true
            }

            DragEvent.ACTION_DRAG_EXITED ->
            {
                if (!isTagSame)
                {
                    v.apply()
                    {
                        setBorderColor(resources.getColor(R.color.drop_accept_border, null))
                        setBorderWidth(20.0f)
                        invalidate()
                    }
                }

                // Returns true; the value is ignored.
                true
            }

            DragEvent.ACTION_DROP ->
            {
                UULog.d(javaClass, "configureDropTarget", "DROP")

                if (!isTagSame)
                {
                    v.apply()
                    {
                        setBorderColor(resources.getColor(R.color.transparent, null))
                        setBorderWidth(0.0f)
                        invalidate()
                    }

                    //dropManagerViewModel.dispatchDrop(viewModel, destViewModel)
                }

                true
            }

            DragEvent.ACTION_DRAG_ENDED ->
            {
                UULog.d(javaClass, "configureDropTarget", "DRAG_ENDED, isTagSame: $isTagSame")

                if (!isTagSame)
                {
                    v.apply()
                    {
                        setBorderColor(resources.getColor(R.color.transparent, null))
                        setBorderWidth(0.0f)
                        invalidate()
                    }
                }
                else
                {
                    v.uuFadeAlpha(1.0f, 200L)
                }

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