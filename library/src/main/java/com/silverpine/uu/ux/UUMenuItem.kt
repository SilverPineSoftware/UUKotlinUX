package com.silverpine.uu.ux

import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources

open class UUMenuItem(title: String, action: ()->Unit, var isAction: Boolean = false): UUButton(title, action)
{
    var groupId: Int = 0
    var itemId: Int = 0
    var order: Int = 0

    constructor(@StringRes resourceId: Int, action: ()->Unit, isAction: Boolean = false): this(UUResources.getString(resourceId), action, isAction)
}