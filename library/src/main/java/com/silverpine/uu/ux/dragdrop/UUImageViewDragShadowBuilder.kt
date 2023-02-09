package com.silverpine.uu.ux.dragdrop

import android.graphics.drawable.Drawable
import android.widget.ImageView

class UUImageViewDragShadowBuilder(
    v: ImageView,
    scaleFactor: Float = 1.0f,
    shadowDrawable: Drawable? = v.drawable?.constantState?.newDrawable()): UUDragShadowBuilder(v, scaleFactor, shadowDrawable)



