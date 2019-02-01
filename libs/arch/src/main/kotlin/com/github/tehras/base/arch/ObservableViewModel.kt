/*
 * Copyright (c) 2019 Evernote Corporation. All rights reserved.
 */
package com.github.tehras.base.arch

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * @author tkoshkin
 */
@Suppress("MemberVisibilityCanBePrivate", "NON_EXHAUSTIVE_WHEN", "unused")
abstract class ObservableViewModel<State, UiEvent> : ViewModel(), Consumer<UiEvent> {

    private val stateRelay = BehaviorRelay.create<State>()!!
    private val uiEvents = PublishRelay.create<UiEvent>()!!

    private val lifecycleRelay = BehaviorRelay.createDefault(Lifecycle.State.INITIALIZED)!!
    private val lifecycle get() = lifecycleRelay.value!!

    override fun accept(uiEvent: UiEvent) {
        uiEvents.accept(uiEvent)
    }

    /**
     * This is called once when the view model is created. This gives you a chance to initialize streams after the
     * constructor call.
     */
    @MainThread
    protected open fun onCreate() {
    }

    /**
     * Called when the view model becomes visible. Here you should subscribe to expensive streams that shouldn't
     * trigger any action when the view model isn't visible. This method can be called multiple times together
     * with [onStop]. Note that this method is also called on an orientation change.
     */
    @MainThread
    protected open fun onStart() {
    }

    /**
     * Called when the view model isn't visible anymore. Here you should clean up and stop expensive operations
     * that shouldn't run when the view model isn't visible anymore. This method can be called multiple times
     * together with [onStart]. Note that this method is also called on an orientation change.
     */
    @MainThread
    protected open fun onStop() {
    }

    /**
     * Called once when the view model isn't needed anymore. After that no other lifecycle method will be called.
     * You should use this method to clean up expensive resources or streams.
     */
    @MainThread
    protected open fun onDestroy() {
    }

    @MainThread
    internal fun create() {
        if (lifecycle === Lifecycle.State.INITIALIZED) {
            lifecycleRelay.accept(Lifecycle.State.CREATED)
            onCreate()
        }
        // else ignore
    }

    @MainThread
    internal fun start() {
        if (lifecycle === Lifecycle.State.INITIALIZED) {
            create() // weird, call create first
        }
        if (lifecycle === Lifecycle.State.CREATED) {
            lifecycleRelay.accept(Lifecycle.State.STARTED)
            onStart()
        }
    }

    @MainThread
    internal fun stop() {
        if (lifecycle === Lifecycle.State.INITIALIZED) {
            create() // weird, shouldn't happen, but just in case
        }
        if (lifecycle === Lifecycle.State.STARTED) {
            lifecycleRelay.accept(Lifecycle.State.CREATED)
            onStop()
        }
    }

    @MainThread
    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun destroy() {
        if (lifecycle === Lifecycle.State.INITIALIZED) {
            create() // weird, shouldn't happen, but just in case call create() for consistency
        }
        if (lifecycle === Lifecycle.State.STARTED) {
            stop() // stop first
        }
        if (lifecycle === Lifecycle.State.CREATED) {
            lifecycleRelay.accept(Lifecycle.State.DESTROYED)
            onDestroy()
        }
    }

    /**
     * Returns a stream of state changes. The stream completes when the view model is destroyed.
     * The stream always replays the last state event. The only exception is if not a single
     * state event was emitted and the view model is destroyed, then the stream completes
     * without emitting an event.
     */
    fun observeState(): Observable<State> {
        if (lifecycle === Lifecycle.State.DESTROYED) {
            // try emitting the last event if possible
            return stateRelay.value?.let { Observable.just(it) } ?: Observable.empty()
        }

        return stateRelay
            .takeUntilDestroyed()
    }

    final override fun onCleared() {
        destroy()
    }

    /**
     * Returns an Observable that emits events until this view model is destroyed and then completes.
     */
    protected fun <T> Observable<T>.takeUntilDestroyed(): Observable<T> {
        return this.takeUntil(lifecycleRelay.filter { it === Lifecycle.State.DESTROYED })
    }

    /**
     * Returns an Observable that emits events until this view model is stopped and then completes.
     * If the view model isn't started, then the stream completes immediately.
     */
    protected fun <T> Observable<T>.takeUntilStopped(): Observable<T> {
        return this.takeUntil(lifecycleRelay.filter { !it.isAtLeast(Lifecycle.State.STARTED) })
    }

    /**
     * Subscribes the observable to the state events that you can consume through [observeState]
     * until the view model is destroyed.
     */
    @SuppressLint("CheckResult")
    protected fun Observable<State>.subscribeUntilDestroyed() {
        takeUntilDestroyed().subscribe(stateRelay)
    }

    /**
     * Subscribes the observable to the state events that you can consume through [observeState]
     * until the view model is stopped. If the view model isn't started, then the stream completes
     * immediately.
     */
    @SuppressLint("CheckResult")
    protected fun Observable<State>.subscribeUntilStopped() {
        takeUntilStopped().subscribe(stateRelay)
    }

    /**
     * Returns the stream of all UI events. This stream completes when the view model is destroyed.
     */
    protected fun uiEvents(): Observable<UiEvent> = uiEvents
}