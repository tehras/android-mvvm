package com.github.tehras.base.arch.rx

import io.reactivex.Observable

fun <T> Observable<T>.shareBehavior(): Observable<T> {
    return this.replay(1).refCount()
}