package com.silverpine.uu.ux

import android.content.res.TypedArray
import androidx.annotation.StyleableRes

fun TypedArray.uuGetInteger(@StyleableRes resId: Int, defaultValue: Int): Int
{
    return try
    {
        getInteger(resId, defaultValue)
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}

fun TypedArray.uuGetFloat(@StyleableRes resId: Int, defaultValue: Float): Float
{
    return try
    {
        getFloat(resId, defaultValue)
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}

fun TypedArray.uuGetDimension(@StyleableRes resId: Int, defaultValue: Float): Float
{
    return try
    {
        getDimension(resId, defaultValue)
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}

fun TypedArray.uuGetDimensionPixelSize(@StyleableRes resId: Int, defaultValue: Float): Float
{
    return try
    {
        getDimensionPixelSize(resId, 0).toFloat()
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}

fun TypedArray.uuGetColor(@StyleableRes resId: Int, defaultValue: Int): Int
{
    return try
    {
        getColor(resId, defaultValue)
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}

fun TypedArray.uuGetString(@StyleableRes resId: Int): String?
{
    return try
    {
        getString(resId)
    }
    catch (ex: Exception)
    {
        null
    }
}

fun TypedArray.uuGetNonResourceString(@StyleableRes resId: Int): String?
{
    return try
    {
        getNonResourceString(resId)
    }
    catch (ex: Exception)
    {
        null
    }
}

fun TypedArray.uuSafeGetString(@StyleableRes resId: Int, defaultValue: String): String
{
    return try
    {
        getString(resId) ?: defaultValue
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}

fun TypedArray.uuSafeGetNonResourceString(@StyleableRes resId: Int, defaultValue: String): String
{
    return try
    {
        getNonResourceString(resId) ?: defaultValue
    }
    catch (ex: Exception)
    {
        defaultValue
    }
}