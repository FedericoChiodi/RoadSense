package com.sanpc.roadsense.data.repository

import com.sanpc.roadsense.data.dao.PotholeDao
import com.sanpc.roadsense.data.model.Pothole
import javax.inject.Inject

class PotholeRepository @Inject constructor(
    private val potholeDao: PotholeDao
) {

    suspend fun insert(pothole: Pothole) {
        potholeDao.insert(pothole)
    }

    suspend fun clearPotholes() {
        potholeDao.clearPotholes()
    }

    suspend fun getUnsyncedPotholes(): List<Pothole> {
        return potholeDao.getUnsyncedPotholes()
    }

    suspend fun markAsSynced(id: Int) {
        potholeDao.markAsSynced(id)
    }

    suspend fun getAllPotholes(): List<Pothole> {
        return potholeDao.getAllPotholes()
    }

    suspend fun getPotholesByUsername(username: String): List<Pothole> {
        return potholeDao.getPotholesByUsername(username)
    }

}