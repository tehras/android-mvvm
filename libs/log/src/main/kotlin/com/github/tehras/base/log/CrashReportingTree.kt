package com.github.tehras.base.log

import android.util.Log
import timber.log.Timber

/**
 * @author tkoshkin created on 8/26/18
 */
class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        // TODO use this when we figure out an App deployment
//        when (priority) {
//            Log.ERROR -> AppCenterLog.error(tag, message, t)
//            Log.INFO -> AppCenterLog.info(tag, message, t)
//            Log.WARN -> AppCenterLog.warn(tag, message, t)
//            Log.ASSERT -> AppCenterLog.logAssert(tag, message, t)
//        }
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority > Log.DEBUG
    }

}