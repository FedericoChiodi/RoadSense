package com.sanpc.roadsense.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "drops")
data class Drop(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double,
    val endLongitude: Double,
    val detectionDate: String,
    val syncStatus: Boolean
) : Parcelable
