package com.silverpine.uu.ux

import androidx.fragment.app.Fragment

fun Fragment.uuHideKeyboard()
{
    activity?.uuHideKeyboard()
}
fun Fragment.uuFinish()
{
    activity?.uuRemoveFragment(this)
}
