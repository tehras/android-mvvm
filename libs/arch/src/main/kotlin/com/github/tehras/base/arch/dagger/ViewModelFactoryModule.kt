package com.github.tehras.base.arch.dagger

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

/**
 * @author tkoshkin created on 8/25/18
 */
@Suppress("unused")
@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}