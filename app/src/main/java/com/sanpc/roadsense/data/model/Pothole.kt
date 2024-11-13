package com.sanpc.roadsense.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "potholes")
data class Pothole(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val latitude: Double,
    val longitude: Double,
    val detectionDate: String,
    val syncStatus: Boolean
) : Parcelable
