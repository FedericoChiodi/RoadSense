package com.sanpc.roadsense.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sanpc.roadsense.data.model.Drop

@Dao
interface DropDao {

    @Insert
    suspend fun insert(drop: Drop)

    @Query("DELETE FROM drops")
    suspend fun clearDrops()

    @Query("SELECT * FROM drops WHERE syncStatus = 0")
    suspend fun getUnsyncedDrops(): List<Drop>

    @Query("UPDATE drops SET syncStatus = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)

    @Query("SELECT * FROM drops")
    suspend fun getAllDrops(): List<Drop>

    @Query("SELECT * FROM drops WHERE username = :username ORDER BY detectionDate DESC")
    suspend fun getDropsByUsername(username: String): List<Drop>
}
