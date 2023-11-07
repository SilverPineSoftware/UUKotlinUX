package com.silverpine.uu.ux

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.silverpine.uu.core.UUResources

open class UUAlertDialog()
{
    var title: String? = null
    var message: String? = null
    var cancelable: Boolean = true
    var items: ArrayList<UUButton>? = null
    var positiveButton: UUButton? = null
    var negativeButton: UUButton? = null
    var neutralButton: UUButton? = null
    var editText: UUEditText? = null

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

class UUAlertDialogViewModel(var model: UUAlertDialog): ViewModel()

fun Context.uuShowAlertDialog(dialog: UUAlertDialog)
{
    val builder = AlertDialog.Builder(this)
    builder.setTitle(dialog.title)
    builder.setMessage(dialog.message)
    builder.setCancelable(dialog.cancelable)

    dialog.editText?.let()
    {
        val editBox = EditText(builder.context)
        editBox.inputType = it.inputType

        editBox.addTextChangedListener(object: TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                it.text = s?.toString() ?: ""
            }

            override fun afterTextChanged(s: Editable?)
            {
            }
        })

        builder.setView(editBox)
    }

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