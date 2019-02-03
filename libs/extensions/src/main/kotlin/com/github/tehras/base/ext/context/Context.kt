package com.github.tehras.base.ext.context

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.calculateNumberOfColumns(widthDp: Int = 180, widthPx: Int = -1): Int {
    val displayMetrics = resources.displayMetrics
    return if (widthPx == -1) {
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        (dpWidth / widthDp).toInt()
    } else {
        displayMetrics.widthPixels / widthPx
    }
}

fun Context.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.hideKeyboard() {
    val focus = (this as? Activity)?.currentFocus

    focus?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}