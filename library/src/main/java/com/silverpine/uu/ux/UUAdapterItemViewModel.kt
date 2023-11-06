package com.silverpine.uu.ux

import androidx.lifecycle.ViewModel

open class UUAdapterItemViewModel: ViewModel()
{
    var onDataChanged: ()->Unit = { }
}