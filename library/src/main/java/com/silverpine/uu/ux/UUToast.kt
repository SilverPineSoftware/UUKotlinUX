package com.silverpine.uu.ux

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources
import com.silverpine.uu.core.uuDispatchMain

private const val DEFAULT_TOAST_LENGTH = Toast.LENGTH_LONG

open class UUToast(var text: String, var length: Int = DEFAULT_TOAST_LENGTH)
{
    constructor(@StringRes resourceId: Int, length: Int = Toast.LENGTH_LONG): this(UUResources.getString(resourceId), length)
}

fun Context.uuShowToast(toast: UUToast)
{
    uuDispatchMain()
    {
        Toast.makeText(this, toast.text, toast.length).show()
    }
}

fun Context.uuShowToast(text: String, length: Int = DEFAULT_TOAST_LENGTH)
{
    uuShowToast(UUToast(text, length))
}

fun Context.uuShowToast(@StringRes textResourceId: Int, length: Int = DEFAULT_TOAST_LENGTH)
{
    uuShowToast(UUToast(textResourceId, length))
}