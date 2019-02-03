package com.github.tehras.base.restapi

import com.github.tehras.dagger.scopes.ApplicationScope
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Qualifier
import kotlin.annotation.MustBeDocumented


/**
 * @author tkoshkin created on 8/26/18
 */
@Module
object RetrofitModule {
    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesRxConverter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    @JvmStatic
    @ApplicationScope
    fun providesMoshiConverter(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @JvmStatic
    @DefaultClient
    @ApplicationScope
    fun providesDefaultOkHttp(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor {
                val request = it.request()

                Timber.d("request :: $request")
                val response = it.proceed(request)
                Timber.d("response :: $response")

                response
            }
            .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultClient