package com.silverpine.uu.sample.ux.binding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.sample.ux.R

class PolygonImageButtonBindingViewModel: ViewModel()
{
    var fillColors: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            R.color.transparent,
            R.color.orange,
            R.color.yellow,
            R.color.green,
            R.color.blue,
            R.color.purple
        ))

    var images: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six
        ))

    /*var visibility: MutableLiveData<Array<Boolean>> = MutableLiveData(
        arrayOf(
            true,
            false,
            true,
            true,
            false,
            true))*/

    var sides: MutableLiveData<Array<Int>> = MutableLiveData(
        arrayOf(
            9,
            8,
            6,
            5,
            3,
            4))

    private var _borderColors: Array<Int> =
        arrayOf(
            R.color.purple,
            R.color.blue,
            R.color.green,
            R.color.red,
            R.color.white,
            R.color.orange
        )

    private var _altBorderColors: Array<Int> =
        arrayOf(
            R.color.green,
            R.color.yellow,
            R.color.purple,
            R.color.red,
            R.color.orange,
            R.color.blue
        )

    private var _mutableBorderColors: Array<MutableLiveData<Int>> =
        arrayOf(
            MutableLiveData(R.color.purple),
            MutableLiveData(R.color.blue),
            MutableLiveData(R.color.green),
            MutableLiveData(R.color.red),
            MutableLiveData(R.color.yellow),
            MutableLiveData(R.color.orange)
        )

    var borderColors: Array<LiveData<Int>> =
        arrayOf(
            _mutableBorderColors[0],
            _mutableBorderColors[1],
            _mutableBorderColors[2],
            _mutableBorderColors[3],
            _mutableBorderColors[4],
            _mutableBorderColors[5])

    var borderWidths: Array<MutableLiveData<Float>> =
        arrayOf(
            MutableLiveData(2.0f),
            MutableLiveData(5.0f),
            MutableLiveData(10.0f),
            MutableLiveData(0.0f),
            MutableLiveData(30.0f),
            MutableLiveData(10.0f)
        )

    var cornerRadius: Array<MutableLiveData<Float>> =
        arrayOf(
            MutableLiveData(0.0f),
            MutableLiveData(0.0f),
            MutableLiveData(5.0f),
            MutableLiveData(10.0f),
            MutableLiveData(15.0f),
            MutableLiveData(3.0f)
        )

    var rotationOffset: Array<MutableLiveData<Float>> =
        arrayOf(
            MutableLiveData(0.0f),
            MutableLiveData(0.0f),
            MutableLiveData((Math.PI / 2.0f).toFloat()),
            MutableLiveData((Math.PI / 3.0f).toFloat()),
            MutableLiveData(3.0f),
            MutableLiveData(1.5f)
        )


    fun update()
    {

    }

    fun onTapColorView(index: Int)
    {

    }

    fun onTapImageView(index: Int)
    {

    }

    var onTapBorderView: (Int)->Unit = { }
    fun onTapBorderView(index: Int)
    {
        onTapBorderView.invoke(index)
    }

    fun onTapBoundBorderView(index: Int)
    {
        if (_mutableBorderColors[index].value == _borderColors[index])
        {
            _mutableBorderColors[index].value = _altBorderColors[index]
            borderWidths[index].value = 10.0f
        }
        else
        {
            _mutableBorderColors[index].value = _borderColors[index]
            borderWidths[index].value = 3.0f
        }
    }
}





