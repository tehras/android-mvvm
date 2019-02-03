package com.github.tehras.base.arch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Binds the view model to the activity where this delegate is used. See [ObservableViewModel] for recommendations
 * how to use this method with Dagger.
 */
inline fun <reified T : ObservableViewModel<*, *>> viewModelActivity(crossinline factoryProvider: () -> ViewModelProvider.Factory): ReadOnlyProperty<FragmentActivity, T> =
    object : ReadOnlyProperty<FragmentActivity, T> {
        private var instance: T? = null

        override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
            if (instance == null) {
                instance = ViewModelProviders.of(thisRef, factoryProvider.invoke()).get(T::class.java).also {
                    thisRef.lifecycle.addObserver(ObservableViewModelInstructor(it))
                }
            }

            return instance!!
        }
    }

/**
 * Binds the view model to the fragment where this delegate is used. See [ObservableViewModel] for recommendations
 * how to use this method with Dagger.
 */
inline fun <reified T : ObservableViewModel<*, *>> viewModel(crossinline factoryProvider: () -> ViewModelProvider.Factory): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T> {
        private var instance: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            if (instance == null) {
                instance = ViewModelProviders.of(thisRef, factoryProvider.invoke()).get(T::class.java).also {
                    thisRef.lifecycle.addObserver(ObservableViewModelInstructor(it))
                }
            }

            return instance!!
        }
    }