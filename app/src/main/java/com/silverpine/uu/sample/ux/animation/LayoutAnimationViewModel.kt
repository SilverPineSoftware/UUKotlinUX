package com.silverpine.uu.sample.ux.animation

import android.view.animation.AccelerateInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.LOG_TAG
import com.silverpine.uu.ux.UULayoutTransition

class LayoutAnimationViewModel: ViewModel()
{
    private var _layoutTransition: MutableLiveData<UULayoutTransition?> = MutableLiveData(null)
    var layoutTransition: LiveData<UULayoutTransition?> = _layoutTransition

    private var _oneVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    var oneVisibility: LiveData<Boolean> = _oneVisibility

    private var _twoVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    var twoVisibility: LiveData<Boolean> = _twoVisibility

    private var _threeVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    var threeVisibility: LiveData<Boolean> = _threeVisibility

    private var _fourVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    var fourVisibility: LiveData<Boolean> = _fourVisibility

    fun hideAll()
    {
        UULog.debug(LOG_TAG, "hideAll, Starting layout animation")
        _layoutTransition.value = UULayoutTransition(1000L, 0L, AccelerateInterpolator())
        { container, view, transitionType ->
            UULog.debug(LOG_TAG, "hideAll, Ending layout animation")
        }

        _oneVisibility.value = false
        _twoVisibility.value = false
        _threeVisibility.value = false
        _fourVisibility.value = false
    }

    fun showOne()
    {
        UULog.debug(LOG_TAG, "showOne, Starting layout animation")
        _layoutTransition.value = UULayoutTransition(1000L, 0L, AccelerateInterpolator())
        { container, view, transitionType ->
            UULog.debug(LOG_TAG, "showOne, Ending layout animation")
        }

        _oneVisibility.value = true
//        _twoVisibility.value = false
//        _threeVisibility.value = false
//        _fourVisibility.value = false
    }

    fun showTwo()
    {
        UULog.debug(LOG_TAG, "showTwo, Starting layout animation")
        _layoutTransition.value = UULayoutTransition(1000L, 0L, AccelerateInterpolator())
        { container, view, transitionType ->
            UULog.debug(LOG_TAG, "showTwo, Ending layout animation")
        }

        _oneVisibility.value = true
        _twoVisibility.value = true
        //_threeVisibility.value = false
        //_fourVisibility.value = false
    }

    fun showThree()
    {
        UULog.debug(LOG_TAG, "showThree, Starting layout animation")
        _layoutTransition.value = UULayoutTransition(1000L, 0L, AccelerateInterpolator())
        { container, view, transitionType ->
            UULog.debug(LOG_TAG, "showThree, Ending layout animation")
        }

        _oneVisibility.value = true
        _twoVisibility.value = true
        _threeVisibility.value = true
        //_fourVisibility.value = false
    }

    fun showFour()
    {
        UULog.debug(LOG_TAG, "showFour, Starting layout animation")
        _layoutTransition.value = UULayoutTransition(1000L, 0L, AccelerateInterpolator())
        { container, view, transitionType ->
            UULog.debug(LOG_TAG, "showFour, Ending layout animation")
        }

        _oneVisibility.value = true
        _twoVisibility.value = true
        _threeVisibility.value = true
        _fourVisibility.value = true
    }

}





