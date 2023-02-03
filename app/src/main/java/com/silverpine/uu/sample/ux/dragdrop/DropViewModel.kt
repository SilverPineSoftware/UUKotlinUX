package com.silverpine.uu.sample.ux.dragdrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UURandom
import com.silverpine.uu.core.UUThread
import com.silverpine.uu.core.UUTimer
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.ux.dragdrop.UUDragDropViewModel

class DropViewModel(private val slotAllowsDrop: Boolean): ViewModel(), UUDragDropViewModel
{
    private var _text = MutableLiveData<String?>(null)
    val text: LiveData<String?> = _text

    private var _sourceDrawable = MutableLiveData<Int?>(null)
    val sourceDrawable: LiveData<Int?> = _sourceDrawable

    private var _borderColor = MutableLiveData(R.color.transparent)
    val borderColor: LiveData<Int> = _borderColor

    private var _borderWidth = MutableLiveData(R.dimen.no_border)
    val borderWidth: LiveData<Int> = _borderWidth

    override val id: String = UURandom.uuid()
    override val name: String = ""
    override val mimeType: String = "UU/CustomMimeType"

    override val allowDrag: Boolean
        get() = (model != null)

    override val allowDrop: Boolean
        get() = slotAllowsDrop


    var onFade: (DropViewModel, Float, Long)->Unit = { _,_,_ -> }
    var onDrop: ()->Unit = { }

    private var model: DropModel? = null

    fun update(model: DropModel?)
    {
        this.model = model
        _text.value = model?.name
        _sourceDrawable.value = model?.image
        clearDrag()
    }


    override fun handleDragStart(other: UUDragDropViewModel)
    {
        if (id == other.id)
        {
            fadeOut()
        }
        else
        {
            UULog.d(javaClass, "handleDragStart", "Handle Drag Start, other is ${other.id}")

            _borderColor.value = R.color.drop_accept_border
            _borderWidth.value = R.dimen.drop_accept_border_width
        }
    }

    override fun handleDragEnter(other: UUDragDropViewModel)
    {
        if (id == other.id)
        {
            // nothing
        }
        else
        {
            _borderColor.value = R.color.drop_hover_border
            _borderWidth.value = R.dimen.drop_hover_border_width
        }
    }

    override fun handleDragExit(other: UUDragDropViewModel)
    {
        if (id == other.id)
        {
            // nothing
        }
        else
        {
            _borderColor.value = R.color.drop_accept_border
            _borderWidth.value = R.dimen.drop_accept_border_width
        }
    }

    override fun handleDrop(other: UUDragDropViewModel)
    {
        if (id == other.id)
        {
            // nothing
        }
        else
        {
            UULog.d(javaClass, "handleDrop", "Dropped View Model ${other.id} onto $id")
            _borderColor.value = R.color.green
            _borderWidth.value = R.dimen.medium_border
            clearDrag()

            (other as? DropViewModel)?.let()
            { otherVm ->

                val srcModel = model
                update(otherVm.model)
                other.update(srcModel)

//                val srcDrawable = _sourceDrawable.value
//                val srcText = _text.value
//
//                _sourceDrawable.value = otherVm.sourceDrawable.value
//                _text.value = otherVm.text.value
//
//                otherVm._sourceDrawable.value = srcDrawable
//                otherVm._text.value = srcText
            }

            onDrop.invoke()
        }
    }

    override fun handleDragEnd(other: UUDragDropViewModel)
    {
        if (id == other.id)
        {
            fadeIn()
        }
        else
        {
            clearDrag()
        }
    }

    fun doWiggle()
    {
        changeBorder(R.color.red, R.dimen.small_border)
        {
            changeBorder(R.color.orange, R.dimen.medium_border)
            {
                changeBorder(R.color.yellow, R.dimen.large_border)
                {
                    changeBorder(R.color.green, R.dimen.large_border)
                    {
                        changeBorder(R.color.blue, R.dimen.medium_border)
                        {
                            changeBorder(R.color.purple, R.dimen.small_border)
                            {
                                changeBorder(R.color.transparent, R.dimen.no_border)
                                {

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun changeBorder(color: Int, width: Int, completion: ()->Unit)
    {
        UUTimer.startTimer("changeBorder", 200L, null)
        { _, _ ->
            UUThread.runOnMainThread()
            {
                _borderColor.value = color
                _borderWidth.value = width
                completion.invoke()
            }
        }
    }

    private fun clearDrag()
    {
        _borderColor.value = R.color.transparent
        _borderWidth.value = R.dimen.no_border
    }

    private fun fadeIn()
    {
        fade(1.0f)
    }

    private fun fadeOut()
    {
        fade(0.5f)
    }

    private fun fade(alpha: Float, duration: Long = 200L)
    {
        onFade.invoke(this, alpha, duration)
    }
}