package com.github.tehras.base.demo.ui.doglist

import androidx.lifecycle.ViewModel
import com.github.tehras.base.arch.dagger.ViewModelFactoryModule
import com.github.tehras.base.arch.dagger.ViewModelKey
import com.github.tehras.base.dagger.components.SubComponentCreator
import com.github.tehras.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class DogListModule {
    @Binds
    @IntoMap
    @ViewModelKey(BreedListViewModel::class)
    abstract fun bindDogListViewModel(dogListViewModel: BreedListViewModel): ViewModel
}

@FragmentScope
@Subcomponent(modules = [DogListModule::class])
interface DogListComponent {
    fun inject(fragment: BreedListFragment)
}

interface DogListComponentCreator : SubComponentCreator {
    fun plusDogListComponent(): DogListComponent
}