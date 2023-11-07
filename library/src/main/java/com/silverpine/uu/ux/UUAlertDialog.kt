package com.silverpine.uu.ux

import android.app.AlertDialog
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources

class UUAlertDialog() : Parcelable
{
    var title: String? = null
    var message: String? = null
    var cancelable: Boolean = true
    var items: ArrayList<UUButton>? = null
    var positiveButton: UUButton? = null
    var negativeButton: UUButton? = null
    var neutralButton: UUButton? = null
    var editText: UUEditText? = null

    constructor(parcel: Parcel) : this()
    {
        title = parcel.readString()
        message = parcel.readString()
        cancelable = parcel.readByte() != 0.toByte()
        val itemsList = parcel.readParcelableArray(UUButton::class.java.classLoader)?.mapNotNull { it as? UUButton }
        itemsList?.let()
        { list ->
            items = ArrayList(list)
        }

        positiveButton = parcel.readParcelable(UUButton::class.java.classLoader)
        negativeButton = parcel.readParcelable(UUButton::class.java.classLoader)
        neutralButton = parcel.readParcelable(UUButton::class.java.classLoader)
        editText = parcel.readParcelable(UUEditText::class.java.classLoader)
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeByte(if (cancelable) 1 else 0)
        parcel.writeParcelableArray(items?.toTypedArray(), flags)
        parcel.writeParcelable(positiveButton, flags)
        parcel.writeParcelable(negativeButton, flags)
        parcel.writeParcelable(neutralButton, flags)
        parcel.writeParcelable(editText, flags)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UUAlertDialog>
    {
        override fun createFromParcel(parcel: Parcel): UUAlertDialog
        {
            return UUAlertDialog(parcel)
        }

        override fun newArray(size: Int): Array<UUAlertDialog?>
        {
            return arrayOfNulls(size)
        }
    }
}

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