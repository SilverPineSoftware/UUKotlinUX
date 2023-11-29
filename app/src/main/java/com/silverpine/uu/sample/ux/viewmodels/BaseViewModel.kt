package com.silverpine.uu.sample.ux.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.uuDispatchMain
import com.silverpine.uu.ux.UUMenuItem
import com.silverpine.uu.ux.UUToast

open class BaseViewModel: ViewModel()
{
    private var _menuItems: MutableLiveData<ArrayList<UUMenuItem>> = MutableLiveData()
    val menuItems: LiveData<ArrayList<UUMenuItem>> = _menuItems

    var onToast: ((UUToast)->Unit) = {  }

    protected fun updateMenu()
    {
        val menu = ArrayList<UUMenuItem>()
        buildMenu(menu)

        uuDispatchMain()
        {
            _menuItems.value = menu
        }
    }

    open fun buildMenu(menu: ArrayList<UUMenuItem>)
    {
    }
}