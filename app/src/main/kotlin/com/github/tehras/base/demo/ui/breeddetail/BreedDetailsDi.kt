package com.github.tehras.base.demo.ui.breeddetail

import androidx.lifecycle.ViewModel
import com.github.tehras.base.arch.dagger.ViewModelFactoryModule
import com.github.tehras.base.arch.dagger.ViewModelKey
import com.github.tehras.base.dagger.components.SubComponentCreator
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.dagger.scopes.FragmentScope
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class BreedDetailsModule {
    @Binds
    @IntoMap
    @ViewModelKey(BreedDetailsViewModel::class)
    abstract fun bindBreedDetailsViewModel(breedDetailsViewModel: BreedDetailsViewModel): ViewModel
}

@FragmentScope
@Subcomponent(modules = [BreedDetailsModule::class])
interface BreedDetailsComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun breed(breed: Breed): Builder

        fun build(): BreedDetailsComponent
    }

    fun inject(fragment: BreedDetailsFragment)
}

interface BreedDetailsComponentCreator : SubComponentCreator {
    fun plusBreedDetailsComponent(): BreedDetailsComponent.Builder
}