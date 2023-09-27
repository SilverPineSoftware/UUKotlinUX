package com.silverpine.uu.ux

import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources

open class UUButton(var title: String, var action: ()->Unit)
{
    constructor(@StringRes resourceId: Int, action: ()->Unit): this(UUResources.getString(resourceId), action)
}