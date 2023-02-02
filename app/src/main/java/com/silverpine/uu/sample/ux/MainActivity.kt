package com.silverpine.uu.sample.ux

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.binding.BindingActivity
import com.silverpine.uu.sample.ux.databinding.ActivityMainBinding
import com.silverpine.uu.sample.ux.dragdrop.DragDropActivity
import com.silverpine.uu.sample.ux.dragdrop.DropModel
import com.silverpine.uu.sample.ux.dragdrop.DropViewModel
import com.silverpine.uu.ux.*

class MainActivity : AppCompatActivity()
{
    private lateinit var viewModel: MainViewModel
    private lateinit var menuHandler: UUMenuHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

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

        buttonOne.configurePlainDragDrop(DropViewModel(true, DropModel("One")), viewModel)
        buttonTwo.configurePlainDragDrop(DropViewModel(true, DropModel("Two")), viewModel)
        buttonThree.configurePlainDragDrop(DropViewModel(false, DropModel("Three")), viewModel)
        buttonFour.configurePlainDragDrop(DropViewModel(true, DropModel("Four")), viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        val m = menu ?: return false
        menuHandler = UUMenuHandler(m)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean
    {
        menu?.removeGroup(0)

        menuHandler.add(R.string.activity_drag_drop_title)
        {
            startActivity(Intent(this, DragDropActivity::class.java))
        }

        menuHandler.add(R.string.activity_binding_title)
        {
            startActivity(Intent(this, BindingActivity::class.java))
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }

}

private const val CUSTOM_DROP_MIME_TYPE = "UU/CustomDropItem"

private fun UUBorderedImageView.configurePlainDragDrop(viewModel: DropViewModel?, dropManagerViewModel: MainViewModel?)
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
        configurePlainDropTarget(viewModel, dropManagerViewModel)
    }
}

private fun UUBorderedImageView.configurePlainDropTarget(destViewModel: DropViewModel, dropManagerViewModel: MainViewModel)
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
                            borderColor = UUResources.getColor(R.color.drop_accept_border)
                            borderWidth = UUResources.getDimension(R.dimen.drop_accept_border_width)
                            //setBorderColor(UUResources.getColor(R.color.drop_accept_border))
                            //setBorderWidth(UUResources.getDimension(R.dimen.drop_accept_border_width))
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
                if (!isTagSame)
                {
                    v.apply()
                    {
                        borderColor = UUResources.getColor(R.color.drop_hover_border)
                        borderWidth = UUResources.getDimension(R.dimen.drop_hover_border_width)
                        //setBorderColor(UUResources.getColor(R.color.drop_hover_border))
                        //setBorderWidth(UUResources.getDimension(R.dimen.drop_hover_border_width))
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
                        borderColor = UUResources.getColor(R.color.drop_accept_border)
                        borderWidth = UUResources.getDimension(R.dimen.drop_accept_border_width)

//                        setBorderColor(UUResources.getColor(R.color.drop_accept_border))
//                        setBorderWidth(UUResources.getDimension(R.dimen.drop_accept_border_width))
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

                        borderColor = 0
                        borderWidth = 0.0f
//                        setBorderColor(0)
//                        setBorderWidth(0.0f)
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
                        borderColor = 0
                        borderWidth = 0.0f
                        //setBorderColor(0)
                        //setBorderWidth(0.0f)
                        //invalidate()
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