package com.silverpine.uu.ux.dragdrop

interface UUDragDropViewModel
{
    val id: String
    val name: String
    val allowDrag: Boolean
    val allowDrop: Boolean
    val mimeType: String

    fun handleDragStart(other: UUDragDropViewModel)
    fun handleDragEnter(other: UUDragDropViewModel)
    fun handleDragExit(other: UUDragDropViewModel)
    fun handleDrop(other: UUDragDropViewModel)
    fun handleDragEnd(other: UUDragDropViewModel)
}