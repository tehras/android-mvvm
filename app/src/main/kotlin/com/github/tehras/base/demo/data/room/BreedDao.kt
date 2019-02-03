package com.github.tehras.base.demo.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.tehras.base.demo.data.room.entities.Breed
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.POST

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds")
    fun getAll(): Observable<List<Breed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(breeds: List<Breed>): Completable
}