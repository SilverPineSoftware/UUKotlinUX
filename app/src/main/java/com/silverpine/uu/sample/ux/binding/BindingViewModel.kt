package com.silverpine.uu.sample.ux.binding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.sample.ux.R

class BindingViewModel: ViewModel()
{
    var colors: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            R.color.red,
            R.color.orange,
            R.color.yellow,
            R.color.green,
            R.color.blue,
            R.color.purple))

    var images: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six))

    var visibility: MutableLiveData<Array<Boolean>> = MutableLiveData(
        arrayOf(
            true,
            false,
            true,
            true,
            false,
            true))

    var borderColors: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            R.color.red,
            R.color.orange,
            R.color.yellow,
            R.color.green,
            R.color.blue,
            R.color.purple))

    var borderWidths: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            R.dimen.large_border,
            R.dimen.medium_border,
            R.dimen.small_border,
            R.dimen.no_border,
            R.dimen.small_border,
            R.dimen.medium_border,))

    fun update()
    {

    }
}





