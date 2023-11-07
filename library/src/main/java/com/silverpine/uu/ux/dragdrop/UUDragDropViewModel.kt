package com.silverpine.uu.ux.dragdrop

interface UUDragDropViewModel
{
    val id: String
    val name: String
    val allowDrop: Boolean
    val mimeType: String
    val dragTriggerType: UUDragTriggerType
    val longPressTriggerTime: Long

    fun handleDragStart(other: UUDragDropViewModel): Boolean
    fun handleDragEnter(other: UUDragDropViewModel)
    fun handleDragExit(other: UUDragDropViewModel)
    fun handleDrop(other: UUDragDropViewModel)
    fun handleDragEnd(other: UUDragDropViewModel)
}