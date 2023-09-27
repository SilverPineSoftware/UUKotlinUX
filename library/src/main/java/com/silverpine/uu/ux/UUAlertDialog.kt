package com.silverpine.uu.ux

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources

class UUAlertDialog
{
    var title: String? = null
    var message: String? = null
    var cancelable: Boolean = true
    var items: ArrayList<UUButton>? = null
    var positiveButton: UUButton? = null
    var negativeButton: UUButton? = null
    var neutralButton: UUButton? = null

    fun setTitleResource(@StringRes resourceId: Int)
    {
        if (resourceId != -1)
        {
            title = UUResources.getString(resourceId)
        }
    }

    fun setMessageResource(@StringRes resourceId: Int)
    {
        if (resourceId != -1)
        {
            message = UUResources.getString(resourceId)
        }
    }
}

fun Context.uuShowAlertDialog(dialog: UUAlertDialog)
{
    val builder = AlertDialog.Builder(this)
    builder.setTitle(dialog.title)
    builder.setMessage(dialog.message)
    builder.setCancelable(dialog.cancelable)

    dialog.items?.let()
    { dlgItems ->
        val items = dlgItems.map { it.title }.toTypedArray()

        builder.setItems(items)
        { dlg, which ->
            if (which >= 0 && which < dlgItems.size)
            {
                dlg.cancel()
                dlgItems[which].action()
            }
        }
    }

    dialog.positiveButton?.let()
    { btn ->

        builder.setPositiveButton(btn.title)
        { dlg, _ ->
            dlg.cancel()
            btn.action()
        }
    }

    dialog.negativeButton?.let()
    { btn ->

        builder.setNegativeButton(btn.title)
        { dlg, _ ->
            dlg.cancel()
            btn.action()
        }
    }

    dialog.neutralButton?.let()
    { btn ->

        builder.setNegativeButton(btn.title)
        { dlg, _ ->
            dlg.cancel()
            btn.action()
        }
    }

    builder.create().show()
}