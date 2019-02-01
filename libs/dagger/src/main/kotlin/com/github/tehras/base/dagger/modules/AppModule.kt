package com.github.tehras.dagger.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * @author tkoshkin created on 8/24/18
 */
@Module
abstract class AppModule {

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ApplicationScope
        fun bindContext(application: Application): Context = application.applicationContext

        @Provides
        @JvmStatic
        @ApplicationScope
        fun providesSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences("default_prefs", Context.MODE_PRIVATE)
    }
}