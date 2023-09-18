package com.silverpine.uu.sample.ux.animation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.ux.UUAlpha

class FadeAnimationViewModel: ViewModel()
{
    private var _viewAlpha: MutableLiveData<UUAlpha> = MutableLiveData(UUAlpha(1.0f))
    var viewAlpha: LiveData<UUAlpha> = _viewAlpha

    private var _durationText: MutableLiveData<String> = MutableLiveData("")
    var durationText: LiveData<String> = _durationText

    private var _startDelayText: MutableLiveData<String> = MutableLiveData("")
    var startDelayText: LiveData<String> = _startDelayText

    private var _alphaText: MutableLiveData<String> = MutableLiveData("")
    var alphaText: LiveData<String> = _alphaText

    var duration: MutableLiveData<Float> = MutableLiveData(500.0f)
    var startDelay: MutableLiveData<Float> = MutableLiveData(0.0f)
    var alpha: MutableLiveData<Float> = MutableLiveData(0.5f)

    init
    {
        duration.observeForever()
        {
            _durationText.value = "${it.toLong()} ms"
        }

        startDelay.observeForever()
        {
            _startDelayText.value = "${it.toLong()} ms"
        }
    }

    fun onAnimate()
    {
        _viewAlpha.value = UUAlpha(alpha.value ?: 0.0f)
            .withDuration(duration.value?.toLong() ?: 0)
            .withStartDelay(startDelay.value?.toLong() ?: 0)
            .withCompletion()
            {
                UULog.d(javaClass, "onAnimate", "Alpha Fade complete")
            }
    }
}





