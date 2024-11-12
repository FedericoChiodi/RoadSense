package com.sanpc.roadsense.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.data.repository.PotholeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PotholeViewModel @Inject constructor(
    private val potholeRepository: PotholeRepository
) : ViewModel() {

    suspend fun insert(pothole: Pothole) {
        potholeRepository.insert(pothole)
    }

    suspend fun getUnsyncedPotholes() : List<Pothole> {
        return potholeRepository.getUnsyncedPotholes()
    }

    suspend fun markAsSynced(id: Int) {
        potholeRepository.markAsSynced(id)
    }

    suspend fun getAllPotholes() : List<Pothole> {
        return potholeRepository.getAllPotholes()
    }

    suspend fun getPotholesByUsername(username : String) : List<Pothole> {
        return potholeRepository.getPotholesByUsername(username)
    }

}