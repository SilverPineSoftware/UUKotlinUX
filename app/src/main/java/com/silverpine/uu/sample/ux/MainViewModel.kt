package com.silverpine.uu.sample.ux

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.uuGetOrNull
import com.silverpine.uu.sample.ux.dragdrop.DropModel
import com.silverpine.uu.sample.ux.dragdrop.DropViewModel

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
        data.value?.uuGetOrNull(0)?.update(R.drawable.one)
        data.value?.uuGetOrNull(1)?.update(R.drawable.two)
        data.value?.uuGetOrNull(2)?.update(R.drawable.three)
        data.value?.uuGetOrNull(3)?.update(R.drawable.four)

        /*
        data.value?.uuGetOrNull(0)?.apply()
        {
            //changeBorderColor(UUResources.getColor(android.R.color.holo_red_dark))
            //changeBorderWidth(5.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }

        data.value?.uuGetOrNull(1)?.apply()
        {
            //changeBorderColor(UUResources.getColor(android.R.color.holo_orange_light))
            //changeBorderWidth(5.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }

        data.value?.uuGetOrNull(2)?.apply()
        {
            //changeBorderColor(UUResources.getColor(android.R.color.holo_green_light))
            //changeBorderWidth(80.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }

        data.value?.uuGetOrNull(3)?.apply()
        {
            //changeBorderColor(UUResources.getColor(R.color.purple_500))
            //changeBorderWidth(5.0f)
            changeSourceDrawable(R.mipmap.ic_launcher)
        }*/
    }
}





