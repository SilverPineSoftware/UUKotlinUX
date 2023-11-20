package com.silverpine.uu.sample.ux.binding

import android.graphics.Typeface
import android.view.Gravity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.sample.ux.R
import com.silverpine.uu.ux.UUTextStyle

class TextStyleViewModel: ViewModel()
{
    private val _styles: MutableLiveData<Array<UUTextStyle>> = MutableLiveData()
    val styles: LiveData<Array<UUTextStyle>> = _styles

    fun update()
    {
        _styles.postValue(arrayOf(
            UUTextStyle(
                fontResourceId = R.font.open_dyslexic_mono_regular,
                fontTextWeight = 500,
                textColorResourceId = R.color.red,
                textSizeResourceId = R.dimen.text_size_12,
                gravity = Gravity.START,
                allCaps = false),
            UUTextStyle(
                fontStyle = Typeface.ITALIC,
                textColorResourceId = R.color.orange,
                textSizeResourceId = R.dimen.text_size_16,
                gravity = Gravity.END,
                allCaps = true),
            UUTextStyle(
                fontResourceId = R.font.open_dyslexic_mono_regular,
                fontTextWeight = 800,
                textColorResourceId = R.color.purple,
                textSizeResourceId = R.dimen.text_size_20,
                gravity = Gravity.CENTER,
                allCaps = true),
            UUTextStyle(
                fontStyle = Typeface.BOLD,
                textColorResourceId = R.color.blue,
                textSizeResourceId = R.dimen.text_size_24,
                gravity = Gravity.START,
                allCaps = false),
        ))

    }
}





