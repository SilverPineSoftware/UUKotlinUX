package com.silverpine.uu.ux

import android.graphics.Paint
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.uuSetSeeMoreOrLessView(msg: String, maxLengthToShowSeeMore: Int, textClickable: Boolean){
    if (msg.length <= maxLengthToShowSeeMore) {
        text = msg
        return
    }
    val seeMoreText = "..."
    val seeLessText = ""
    val spannableTextSeeMore = SpannableString("${msg.take(maxLengthToShowSeeMore)}$seeMoreText")
    val spannableTextSeeLess = SpannableString("$msg$seeLessText")

    val clickableSpan = object : ClickableSpan(){
        override fun onClick(widget: View) {
            //change spannable string
            val currentTag = tag as? String?
            if (currentTag?.equals(seeMoreText) == true){
                text = spannableTextSeeLess
                tag = seeLessText
            } else {
                text = spannableTextSeeMore
                tag = seeMoreText
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    if(textClickable) {
        this.setOnClickListener {
            clickableSpan.onClick(this)
        }
    }

    spannableTextSeeMore.setSpan(
        clickableSpan,
        maxLengthToShowSeeMore,
        maxLengthToShowSeeMore+seeMoreText.length,
        0
    )

    spannableTextSeeLess.setSpan(
        clickableSpan,
        msg.length,
        msg.length+seeLessText.length,
        0
    )

    text = spannableTextSeeMore // default
    tag = seeMoreText
    movementMethod = LinkMovementMethod()
}

fun TextView.uuUnderline(enabled: Boolean) {
    paintFlags = if(enabled) {
        paintFlags or Paint.UNDERLINE_TEXT_FLAG
    } else {
        paintFlags xor Paint.UNDERLINE_TEXT_FLAG
    }
}