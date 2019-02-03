package com.github.tehras.base.demo

import android.app.Application
import com.github.tehras.base.dagger.components.MainComponent
import com.github.tehras.base.demo.data.remote.DogApiModule
import com.github.tehras.base.demo.data.room.AppDatabaseModule
import com.github.tehras.base.demo.ui.breeddetail.BreedDetailsComponentCreator
import com.github.tehras.base.demo.ui.doglist.DogListComponentCreator
import com.github.tehras.base.moshi.MoshiModule
import com.github.tehras.base.restapi.RetrofitModule
import com.github.tehras.dagger.modules.AppModule
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@Suppress("unused")
@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        MoshiModule::class,
        RetrofitModule::class,
        AppDatabaseModule::class,
        DogApiModule::class
    ]
)
interface DemoAppComponent :
    MainComponent,
    UiComponentCreators {

    fun plusApplication(application: DemoApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): DemoAppComponent
    }
}

interface UiComponentCreators : DogListComponentCreator, BreedDetailsComponentCreator