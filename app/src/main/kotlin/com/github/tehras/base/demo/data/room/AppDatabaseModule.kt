package com.github.tehras.base.demo.data.room

import android.content.Context
import androidx.room.Room
import com.github.tehras.dagger.scopes.ApplicationScope
import dagger.Module
import dagger.Provides


@Module
object AppDatabaseModule {
    @Provides
    @ApplicationScope
    @JvmStatic
    fun providesAppDatabase(context: Context): AppDatabase {
        // Create and return a database instance
        // Because we defined the scope as `@ApplicationScope` we can safely assume that Dagger will take care of keeping
        // This same database instance through the app
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-db").build()
    }

    @Provides
    @ApplicationScope
    @JvmStatic
    fun providesBreedDao(appDatabase: AppDatabase): BreedDao = appDatabase.breedDao()

    @Provides
    @ApplicationScope
    @JvmStatic
    fun providesBreedDetailsDao(appDatabase: AppDatabase): BreedDetailsDao = appDatabase.breedDetailsDao()
}