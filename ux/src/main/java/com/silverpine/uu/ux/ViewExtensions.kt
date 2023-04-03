package com.silverpine.uu.ux

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun View.uuSetVisible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.uuSetHidden(hidden: Boolean) {
    visibility = if (hidden) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

fun ViewGroup.uuInflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.uuGone() = run { visibility = View.GONE }

fun View.uuVisible() = run { visibility = View.VISIBLE }

fun View.uuInvisible() = run { visibility = View.INVISIBLE }

/*
    Example usage:  dataFound, loading, and condition should be a valid boolean expression
    view uuGoneIf dataFound
    view uuVisibleIf loading
    view uuInvisibleIf condition
 */
infix fun View.uuVisibleIf(condition: Boolean) =
    run { visibility = if (condition) View.VISIBLE else View.GONE }

infix fun View.uuGoneIf(condition: Boolean) =
    run { visibility = if (condition) View.GONE else View.VISIBLE }

infix fun View.uuInvisibleIf(condition: Boolean) =
    run { visibility = if (condition) View.INVISIBLE else View.VISIBLE }