package com.github.tehras.base.ext.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <T : Fragment> T.withBundle(block: Bundle.() -> Unit): T {
    val b = android.os.Bundle()
        .apply {
            block()
        }
    arguments = b

    return this
}