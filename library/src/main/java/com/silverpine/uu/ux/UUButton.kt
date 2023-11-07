package com.silverpine.uu.ux

import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources

open class UUButton()
{
    var title: String = ""
    var action: ()->Unit = {   }

    constructor(title: String, action: ()->Unit = { }): this()
    {
        this.title = title
        this.action = action
    }

    constructor(@StringRes titleResourceId: Int, action: ()->Unit = { }): this(UUResources.getString(titleResourceId), action)
}