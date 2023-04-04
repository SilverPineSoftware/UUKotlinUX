package com.silverpine.uu.ux

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.uuAfterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}

fun EditText.uuFocus() {
    text?.let { setSelection(it.length) }
    postDelayed({
        requestFocus()
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 200)
}

fun EditText.uuMultilineIme(action: Int) {
    imeOptions = action
    inputType = EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
    setHorizontallyScrolling(false)
    maxLines = Integer.MAX_VALUE
}

fun EditText.uuMultilineDone(callback: (() -> Unit)? = null) {
    val action = EditorInfo.IME_ACTION_DONE
    uuMultilineIme(action)
    setOnEditorActionListener { _, actionId, _ ->
        if (action == actionId) {
            callback?.invoke()
            true
        }
        false
    }
}