package com.silverpine.uu.ux

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources

open class UUButton()
{
    var title: String = ""
    @DrawableRes var icon: Int? = null
    var action: ()->Unit = {   }

    constructor(title: String, action: ()->Unit = { }): this()
    {
        this.title = title
        this.action = action
    }

    constructor(@StringRes titleResourceId: Int, action: ()->Unit = { }): this(UUResources.getString(titleResourceId), action)

    companion object
    {
        fun iconButton(@DrawableRes iconResourceId: Int, action: () -> Unit): UUButton
        {
            val button = UUButton("", action)
            button.icon = iconResourceId
            return button
        }
    }
}