package com.silverpine.uu.ux

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.silverpine.uu.core.UUThread

fun Context.uuPrompt(
    @StringRes title: Int = -1,
    @StringRes message: Int,
    @StringRes positiveButtonTextId: Int,
    @StringRes negativeButtonTextId: Int = -1,
    cancelable: Boolean = true,
    positiveAction: () -> Unit = { },
    negativeAction: () -> Unit = { })
{
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(cancelable)

    if (title != -1)
    {
        builder.setTitle(title)
    }

    if (message != -1)
    {
        builder.setMessage(message)
    }

    if (positiveButtonTextId != -1)
    {
        builder.setPositiveButton(positiveButtonTextId)
        { dialog, _ ->
            dialog.cancel()
            positiveAction()
        }
    }

    if (negativeButtonTextId != -1)
    {
        builder.setNegativeButton(negativeButtonTextId)
        { dialog, _ ->
            dialog.cancel()
            negativeAction()
        }
    }

    val dialog = builder.create()
    dialog.show()
}


fun Context.uuPrompt(
    title: String? = null,
    message: String? = null,
    positiveButtonText: String?,
    negativeButtonText: String? = null,
    cancelable: Boolean = true,
    positiveAction: () -> Unit = { },
    negativeAction: () -> Unit = { })
{
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(cancelable)

    title?.let()
    {
        builder.setTitle(it)
    }

    message?.let()
    {
        builder.setMessage(it)
    }

    positiveButtonText?.let()
    {
        builder.setPositiveButton(it)
        { dialog, _ ->
            dialog.cancel()
            positiveAction()
        }
    }

    negativeButtonText?.let()
    {
        builder.setNegativeButton(it)
        { dialog, _ ->
            dialog.cancel()
            negativeAction()
        }
    }

    val dialog = builder.create()
    dialog.show()
}

fun Context.uuPrompt(
    @StringRes title: Int = -1,
    @StringRes message: Int = -1,
    @StringRes negativeButtonTextId: Int = -1,
    selections: ArrayList<String>,
    cancelable: Boolean = true,
    negativeAction: () -> Unit = { },
    selection: (Int) -> Unit)
{
    val builder = AlertDialog.Builder(this)
    builder.setCancelable(cancelable)

    if (title != -1)
    {
        builder.setTitle(title)
    }

    if (message != -1)
    {
        builder.setMessage(message)
    }

    if (negativeButtonTextId != -1)
    {
        builder.setNegativeButton(negativeButtonTextId)
        { dialog, _ ->
            dialog.cancel()
            negativeAction()
        }
    }

    builder.setItems(selections.toTypedArray())
    { dialog, which ->

        dialog.cancel()
        selection.invoke(which)
    }

    val dialog = builder.create()
    dialog.show()
}

fun Context.uuShowToast(text: String)
{
    UUThread.runOnMainThread()
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}