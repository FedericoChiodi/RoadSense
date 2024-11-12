package com.sanpc.roadsense.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.data.dao.DropDao
import com.sanpc.roadsense.data.dao.PotholeDao

@Database(entities = [Pothole::class, Drop::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun potholeDao(): PotholeDao
    abstract fun dropDao(): DropDao
}
