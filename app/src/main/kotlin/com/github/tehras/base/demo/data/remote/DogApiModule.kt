package com.github.tehras.base.demo.data.remote

import com.github.tehras.base.demo.data.breeddetails.remote.BreedDetailsService
import com.github.tehras.base.demo.data.breedlist.remote.BreedListService
import com.github.tehras.base.restapi.DefaultClient
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val DOG_API_BASE_URL = "https://dog.ceo/"

@Module
object DogApiModule {

    @Provides
    @JvmStatic
    @ApplicationScope
    fun provideBreedListService(retrofit: Retrofit): BreedListService = retrofit.create(BreedListService::class.java)

    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesBreedDetailsService(retrofit: Retrofit): BreedDetailsService =
        retrofit.create(BreedDetailsService::class.java)

    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesRetrofit(
        @DefaultClient
        okHttpClient: OkHttpClient,
        rxJavaAdapter: RxJava2CallAdapterFactory,
        moshiAdapter: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DOG_API_BASE_URL)
            .addConverterFactory(moshiAdapter)
            .client(okHttpClient)
            .addCallAdapterFactory(rxJavaAdapter)
            .build()
    }
}