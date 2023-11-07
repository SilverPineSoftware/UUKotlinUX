package com.silverpine.uu.ux

import android.os.Parcelable
import android.text.InputType
import kotlinx.parcelize.Parcelize

@Parcelize
class UUEditText(var hint: String = "", var text: String = "", var inputType: Int = InputType.TYPE_CLASS_TEXT): Parcelable