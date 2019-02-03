package com.github.tehras.base.demo.data.room.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.tehras.base.demo.data.room.converters.BreedDetailsConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "breed_details")
data class BreedDetails(
    @PrimaryKey
    val key: String,
    val images: List<String>
) : Parcelable