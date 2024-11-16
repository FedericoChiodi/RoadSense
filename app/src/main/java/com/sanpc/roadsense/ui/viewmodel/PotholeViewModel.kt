package com.sanpc.roadsense.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.data.repository.PotholeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PotholeViewModel @Inject constructor(
    private val potholeRepository: PotholeRepository
) : ViewModel() {

    private val _potholes = MutableStateFlow<List<Pothole>>(emptyList())
    val potholes: StateFlow<List<Pothole>> get() = _potholes


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

    fun getPotholesByUsername(username: String): StateFlow<List<Pothole>> {
        viewModelScope.launch {
            val potholesList = potholeRepository.getPotholesByUsername(username)
            _potholes.emit(potholesList)
        }
        return _potholes
    }

}