package com.github.tehras.theme

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes


fun Context.resolveAttribute(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)

    return typedValue.resourceId
}