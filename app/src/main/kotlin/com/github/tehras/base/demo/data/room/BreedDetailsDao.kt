package com.github.tehras.base.demo.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.tehras.base.demo.data.room.entities.BreedDetails
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface BreedDetailsDao {
    @Query("SELECT * FROM breed_details WHERE `key` IS :breed")
    fun get(breed: String): Observable<BreedDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(details: BreedDetails): Completable
}