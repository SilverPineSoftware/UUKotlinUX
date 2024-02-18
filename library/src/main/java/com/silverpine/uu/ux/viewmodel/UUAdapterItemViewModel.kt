package com.silverpine.uu.ux.viewmodel

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UURandom

open class UUAdapterItemViewModel: ViewModel()
{
    // Give each recycler item a unique identifier
    val uuId = UURandom.uuid()

    var onDataChanged: ()->Unit = { }
}

data class UUAdapterItemViewModelMapping(val viewModelClass: Class<out UUAdapterItemViewModel>, @LayoutRes val layoutResourceId: Int, val bindingId: Int)