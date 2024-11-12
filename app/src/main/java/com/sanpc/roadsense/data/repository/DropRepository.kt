package com.sanpc.roadsense.data.repository

import com.sanpc.roadsense.data.dao.DropDao
import com.sanpc.roadsense.data.model.Drop
import javax.inject.Inject

class DropRepository @Inject constructor(
    private val dropDao: DropDao
) {

    suspend fun insert(drop: Drop) {
        dropDao.insert(drop)
    }

    suspend fun getUnsyncedDrops(): List<Drop> {
        return dropDao.getUnsyncedDrops()
    }

    suspend fun markAsSynced(id: Int) {
        dropDao.markAsSynced(id)
    }

    suspend fun getAllDrops(): List<Drop> {
        return dropDao.getAllDrops()
    }

    suspend fun getDropsByUsername(username: String): List<Drop> {
        return dropDao.getDropsByUsername(username)
    }

}