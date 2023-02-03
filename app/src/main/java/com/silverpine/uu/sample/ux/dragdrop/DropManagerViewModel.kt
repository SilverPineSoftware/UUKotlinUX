package com.silverpine.uu.sample.ux.dragdrop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.uuGetOrNull
import com.silverpine.uu.sample.ux.R

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

        data.value?.forEach()
        {
            it.onFade = onFade
            it.onDrop = this::handleDrop
        }
    }

    private fun handleDrop()
    {
        data.value?.forEach()
        {
            it.doWiggle()
        }
    }
}