package com.silverpine.uu.sample.ux.activities

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.silverpine.uu.logging.UULog
import com.silverpine.uu.sample.ux.viewmodels.BaseViewModel
import com.silverpine.uu.ux.UUMenuHandler
import com.silverpine.uu.ux.UUMenuItem
import com.silverpine.uu.ux.uuShowToast

open class BaseActivity: AppCompatActivity()
{
    private lateinit var menuHandler: UUMenuHandler
    private var menuViewModels: ArrayList<UUMenuItem> = arrayListOf()

    protected fun setupViewModel(viewModel: BaseViewModel)
    {
        viewModel.onToast = this::uuShowToast

        viewModel.menuItems.observe(this)
        {
            UULog.d(javaClass, "setupViewModel", "Menu items have changed")
            menuViewModels.clear()
            menuViewModels.addAll(it)
            invalidateMenu()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // UUMenuHandler
    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuHandler = UUMenuHandler(menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean
    {
        menu?.clear()

        menuViewModels.forEach()
        { mi ->

            if (mi.isAction)
            {
                menuHandler.addAction(mi.title, mi.action)
            }
            else
            {
                menuHandler.add(mi.title, mi.action)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }
}