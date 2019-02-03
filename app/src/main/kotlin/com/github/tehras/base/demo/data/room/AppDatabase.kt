package com.github.tehras.base.demo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.tehras.base.demo.data.room.converters.BreedDetailsConverter
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.base.demo.data.room.entities.BreedDetails

@Database(entities = [Breed::class, BreedDetails::class], version = 1, exportSchema = false)
@TypeConverters(BreedDetailsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
    abstract fun breedDetailsDao(): BreedDetailsDao
}