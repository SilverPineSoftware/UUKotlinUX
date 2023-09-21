package com.silverpine.uu.sample.ux.dragdrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.ux.dragdrop.UUDragTriggerType

class DropManagerViewModel: ViewModel()
{
    val data: Array<LiveData<DropViewModel>> =
        arrayOf(
            MutableLiveData(DropViewModel(true)),
            MutableLiveData(DropViewModel(true)),
            MutableLiveData(DropViewModel(false)),
            MutableLiveData(DropViewModel(true))
        )

    fun update()
    {
        data[0].value?.update(DropModel("one", R.drawable.one, true, UUDragTriggerType.None))
        data[1].value?.update(DropModel("two", R.drawable.two, true, UUDragTriggerType.TouchDown))
        data[2].value?.update(DropModel("three", R.drawable.three, false, UUDragTriggerType.TouchDown))
        data[3].value?.update(DropModel("four", R.drawable.four, true, UUDragTriggerType.LongPress))
    }

    private fun handleDrop()
    {
        data.forEach()
        {
            it.value?.doWiggle()
        }
    }
}