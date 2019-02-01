package com.github.tehras.base.ext.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.viewHolderFromParent(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}