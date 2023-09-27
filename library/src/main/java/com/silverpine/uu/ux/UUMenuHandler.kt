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

open class UUMenuHandler(private val menu: Menu)
{
    private val items = ArrayList<UUMenuItem>()

    fun add(item: UUMenuItem): MenuItem
    {
        if (item.itemId == 0)
        {
            item.itemId = (items.size + 1)
        }

        val mi = menu.add(item.groupId, item.itemId, item.order, item.title)

        if (item.isAction)
        {
            mi.uuSetAsActionAlways()
        }

        synchronized(items)
        {
            items.add(item)
        }


        return mi
    }

    fun add(title: String, action: ()->Unit): MenuItem
    {
        return add(UUMenuItem(title, action, false))
    }

    fun add(@StringRes titleResource: Int, action: ()->Unit): MenuItem
    {
        return add(UUMenuItem(titleResource, action, false))
    }

    fun addAction(title: String, action: ()->Unit): MenuItem
    {
        return add(UUMenuItem(title, action, true))
    }

    fun addAction(@StringRes titleResource: Int, action: ()->Unit): MenuItem
    {
        return add(UUMenuItem(titleResource, action, false))
    }

    fun handleMenuClick(item: MenuItem):  Boolean
    {
        val mi = items.firstOrNull { it.groupId == item.groupId && it.itemId == item.itemId && it.order == item.order }

        return if (mi != null)
        {
            mi.action()
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







