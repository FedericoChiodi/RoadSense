package com.sanpc.roadsense.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "potholes")
data class Pothole(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val latitude: Double,
    val longitude: Double,
    val detectionDate: String,
    val syncStatus: Boolean
)
