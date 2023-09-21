package com.silverpine.uu.sample.ux.dragdrop

import com.silverpine.uu.ux.dragdrop.UUDragTriggerType

data class DropModel(
    val name: String,
    val image: Int,
    val allowDrop: Boolean,
    val dragTriggerType: UUDragTriggerType)