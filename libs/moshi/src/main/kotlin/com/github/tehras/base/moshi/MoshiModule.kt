package com.github.tehras.base.moshi

import com.github.tehras.dagger.scopes.ApplicationScope
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

/**  * @author tkoshkin created on 8/26/18  */
@Module
class MoshiModule {
    @Module
    companion object {
        @Provides
        @ApplicationScope
        @JvmStatic
        fun providesMoshi(): Moshi = Moshi.Builder().build()
    }
}