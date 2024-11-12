package com.sanpc.roadsense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sanpc.roadsense.data.model.Pothole

@Dao
interface PotholeDao {

    @Insert
    suspend fun insert(pothole: Pothole)

    @Query("SELECT * FROM potholes WHERE syncStatus = 0")
    suspend fun getUnsyncedPotholes(): List<Pothole>

    @Query("UPDATE potholes SET syncStatus = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)

    @Query("SELECT * FROM potholes")
    suspend fun getAllPotholes(): List<Pothole>

    @Query("SELECT * FROM potholes WHERE username = :username")
    suspend fun getPotholesByUsername(username: String): List<Pothole>
}
