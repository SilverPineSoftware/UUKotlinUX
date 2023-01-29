package com.silverpine.uu.ux

import android.content.Intent
import android.os.Parcelable

fun <T: Parcelable> Intent.uuRequireParcelable(key: String): T
{
    val obj = extras?.getParcelable<T>(key)
    if (obj == null)
    {
        throw RuntimeException("Expected extra with key $key to be non-nil.")
    }
    else
    {
        return obj
    }
}

fun Intent.uuRequireString(key: String): String
{
    val obj = extras?.getString(key)
    if (obj == null)
    {
        throw RuntimeException("Expected String extra with key $key to be non-nil.")
    }
    else
    {
        return obj
    }
}