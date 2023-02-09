package com.silverpine.uu.ux

import android.view.View

fun View.uuClearDragDrop()
{
    tag = null
    setOnLongClickListener(null)
    setOnDragListener(null)
}