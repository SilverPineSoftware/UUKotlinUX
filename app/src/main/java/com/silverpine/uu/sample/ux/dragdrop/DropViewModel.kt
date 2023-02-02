package com.silverpine.uu.sample.ux.dragdrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UURandom
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.R

class DropViewModel(var allowDrop: Boolean = true, var model: DropModel?): ViewModel()
{
    private var _text = MutableLiveData<String?>(model?.name)
    val text: LiveData<String?> = _text

    private var _sourceDrawable = MutableLiveData<Int?>(null)
    val sourceDrawable: LiveData<Int?> = _sourceDrawable

    private var _borderColor = MutableLiveData(R.color.transparent)
    val borderColor: LiveData<Int> = _borderColor

    private var _borderWidth = MutableLiveData(R.dimen.no_border)
    val borderWidth: LiveData<Int> = _borderWidth

    val id = UURandom.uuid()

    var onFade: (DropViewModel, Float, Long)->Unit = { _,_,_ -> }

    fun update(resourceId: Int?)
    {
        _sourceDrawable.value = resourceId
        clearDrag()
    }

    fun handleDragStart(other: DropViewModel)
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

    fun handleDragEnter(other: DropViewModel)
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

    fun handleDragExit(other: DropViewModel)
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

    fun handleDrop(other: DropViewModel)
    {
        if (id == other.id)
        {
            // nothing
        }
        else
        {
            _borderColor.value = R.color.green
            _borderWidth.value = R.dimen.medium_border
        }
    }

    fun handleDragEnd(other: DropViewModel)
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