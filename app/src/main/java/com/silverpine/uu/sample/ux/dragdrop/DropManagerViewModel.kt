package com.silverpine.uu.sample.ux.dragdrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.sample.ux.R

class DropManagerViewModel: ViewModel()
{
    val data: Array<LiveData<DropViewModel>> =
        arrayOf(
            MutableLiveData(DropViewModel(true)),
            MutableLiveData(DropViewModel(true)),
            MutableLiveData(DropViewModel(false)),
            MutableLiveData(DropViewModel(true))
        )

    var onFade: (DropViewModel, Float, Long)->Unit = { _,_,_ -> }

    fun update()
    {
        data[0].value?.update(DropModel("one", R.drawable.one, true))
        data[1].value?.update(DropModel("two", R.drawable.two, true))
        data[2].value?.update(DropModel("three", R.drawable.three, false))
        data[3].value?.update(DropModel("four", R.drawable.four, true))
    }

    private fun handleDrop()
    {
        data.forEach()
        {
            it.value?.doWiggle()
        }
    }
}