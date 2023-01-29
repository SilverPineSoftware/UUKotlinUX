package com.silverpine.uu.ux

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes

/**
 * UUMenuHandler provides a simple and easy way to create programmatic menu's and map
 * runnable actions to them.
 *
 * @sample

open class SomeActivity: AppCompatActivity()
{
    private lateinit var menuHandler: UUMenuHandler

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

 */

class UUMenuHandler(private val menu: Menu)
{
    private val handlers: HashMap<Int, Runnable> = HashMap()
    private var lastId = 0

    fun add(title: String, action: Runnable): MenuItem
    {
        val id = addHandler(action)
        return menu.add(0, id, 0, title)
    }

    fun add(@StringRes titleResource: Int, action: Runnable): MenuItem
    {
        val id = addHandler(action)
        return menu.add(0, id, 0, titleResource)
    }

    fun addAction(title: String, action: Runnable): MenuItem
    {
        val id = addHandler(action)
        val item = menu.add(0, id, 0, title)
        item.uuSetAsActionAlways()
        return item
    }

    fun addAction(@StringRes titleResource: Int, action: Runnable): MenuItem
    {
        val id = addHandler(action)
        val item = menu.add(0, id, 0, titleResource)
        item.uuSetAsActionAlways()
        return item
    }

    private fun addHandler(action: Runnable): Int
    {
        lastId++
        handlers[lastId] = action
        return lastId
    }

    fun handleMenuClick(item: MenuItem):  Boolean
    {
        val handler = handlers[item.itemId]

        return if (handler != null)
        {
            handler.run()
            true
        }
        else
        {
            false
        }
    }
}

fun MenuItem.uuSetAsActionAlways()
{
    setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
}







