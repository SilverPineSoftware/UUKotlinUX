package com.silverpine.uu.sample.ux

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UURandom
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.ux.UUBorderedImageView

class MainViewModel: ViewModel()
{
    var data: LiveData<ArrayList<DropViewModel>> = MutableLiveData(arrayListOf(
        DropViewModel(true, DropModel("one")),
        DropViewModel(true, DropModel("two")),
        DropViewModel(false, DropModel("three")),
        DropViewModel(true, DropModel("four"))
    ))

    fun update()
    {
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

class DropModel(val name: String)
{

}

class DropViewModel(var allowDrop: Boolean = true, var model: DropModel?): ViewModel()
{
    private var _text = MutableLiveData<String?>(model?.name)
    val text: LiveData<String?> = _text

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
}

@BindingAdapter(value = ["bind:uuDropViewModel", "bind:uuDropManagerViewModel"], requireAll = true)
fun uuConfigureDropBindings(view: UUBorderedImageView?, viewModel: DropViewModel?, dropManagerViewModel: MainViewModel?)
{
    view?.configureDragDrop(viewModel, dropManagerViewModel)

}