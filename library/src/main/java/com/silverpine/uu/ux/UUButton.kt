package com.silverpine.uu.ux

import android.os.Parcelable
import androidx.annotation.StringRes
import com.silverpine.uu.core.UUResources
import kotlinx.parcelize.Parcelize

@Parcelize
open class UUButton(var title: String, var action: ()->Unit): Parcelable
{
    constructor(@StringRes resourceId: Int, action: ()->Unit): this(UUResources.getString(resourceId), action)
}