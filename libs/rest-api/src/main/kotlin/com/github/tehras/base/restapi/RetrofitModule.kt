package com.github.tehras.base.restapi

import com.github.tehras.dagger.scopes.ApplicationScope
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * @author tkoshkin created on 8/26/18
 */
@Module
abstract class RetrofitModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        @ApplicationScope
        fun providesRxConverter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        @Provides
        @JvmStatic
        @ApplicationScope
        fun providesMoshiConverter(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)
    }
}