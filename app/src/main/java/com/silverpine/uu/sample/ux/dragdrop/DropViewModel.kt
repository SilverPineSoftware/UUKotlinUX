package com.silverpine.uu.sample.ux.dragdrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UURandom
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.sample.ux.R

class DropViewModel(var allowDrop: Boolean = true, var model: DropModel?): ViewModel()
{
    private var _text = MutableLiveData<String?>(model?.name)
    val text: LiveData<String?> = _text

    private var _backgroundColor = MutableLiveData<Int?>(null)
    val backgroundColor: LiveData<Int?> = _backgroundColor

    private var _sourceDrawable = MutableLiveData<Int?>(null)
    val sourceDrawable: LiveData<Int?> = _sourceDrawable

    private var _borderColor = MutableLiveData<Int?>(null)
    val borderColor: LiveData<Int?> = _borderColor

    private var _borderWidth = MutableLiveData<Float?>(null)
    val borderWidth: LiveData<Float?> = _borderWidth

    val id = UURandom.uuid()

    fun handleDragEnter()
    {
        _borderColor.value = UUResources.getColor(R.color.drop_hover_border)
        _borderWidth.value = 40.0f
    }

    fun handleDragExit()
    {
        handleDragStart()
    }

    fun handleDragStart()
    {
        _borderColor.value = UUResources.getColor(R.color.drop_accept_border)
        _borderWidth.value = 20.0f
    }

    fun handleDragEnd()
    {
        _borderColor.value = null
        _borderWidth.value = null
    }

    fun changeBackgroundColor(resourceId: Int?)
    {
        _backgroundColor.value = resourceId
    }

    fun changeSourceDrawable(resourceId: Int?)
    {
        _sourceDrawable.value = resourceId
    }

    fun changeBorderColor(resourceId: Int?)
    {
        _borderColor.value = resourceId
    }

    fun changeBorderWidth(width: Float?)
    {
        _borderWidth.value = width
    }
}