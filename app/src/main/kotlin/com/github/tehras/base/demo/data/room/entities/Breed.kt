package com.github.tehras.base.demo.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "breeds")
data class Breed(
    @PrimaryKey
    val key: String,
    val name: String
) : Parcelable