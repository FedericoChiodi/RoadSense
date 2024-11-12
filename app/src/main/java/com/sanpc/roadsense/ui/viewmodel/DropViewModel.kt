package com.sanpc.roadsense.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.repository.DropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DropViewModel @Inject constructor(
    private val dropRepository: DropRepository
) : ViewModel() {

    suspend fun insert(drop: Drop) {
        dropRepository.insert(drop)
    }

    suspend fun getUnsyncedDrops() : List<Drop> {
        return dropRepository.getUnsyncedDrops()
    }

    suspend fun markAsSynced(id: Int) {
        dropRepository.markAsSynced(id)
    }

    suspend fun getAllDrops() : List<Drop> {
        return dropRepository.getAllDrops()
    }

    suspend fun getDropsByUsername(username : String) : List<Drop> {
        return dropRepository.getDropsByUsername(username)
    }

}