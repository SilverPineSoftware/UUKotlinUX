package com.silverpine.uu.ux

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Base activity class that has a UUMenuHandler
 */
open class UUAppCompatActivity(@StringRes val titleResourceId: Int = -1, @LayoutRes val layoutResourceId: Int = -1) : AppCompatActivity()
{
    private lateinit var menuHandler: UUMenuHandler

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (layoutResourceId != -1)
        {
            setContentView(layoutResourceId)
        }

        if (titleResourceId != -1)
        {
            setTitle(titleResourceId)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean
    {
        menu?.let()
        {
            it.clear()

            populateMenu(menuHandler)
            return true
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuHandler = UUMenuHandler(menu)
        return true
    }

    open fun populateMenu(menuHandler: UUMenuHandler)
    {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return menuHandler.handleMenuClick(item)
    }
}






