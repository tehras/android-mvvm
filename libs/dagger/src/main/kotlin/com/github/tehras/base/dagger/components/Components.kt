package com.github.tehras.base.dagger.components

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * @author tkoshkin created on 8/24/18
 */

fun <T> Fragment.findComponent(): T {
    @Suppress("UNCHECKED_CAST")
    return (this.context?.applicationContext as? DaggerApplication)?.getComponent() as T
}

fun <T> Activity.findComponent(): T {
    @Suppress("UNCHECKED_CAST")
    return (this.application as DaggerApplication).getComponent() as T
}