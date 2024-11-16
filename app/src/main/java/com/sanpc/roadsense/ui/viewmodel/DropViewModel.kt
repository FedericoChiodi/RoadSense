package com.sanpc.roadsense.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.repository.DropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DropViewModel @Inject constructor(
    private val dropRepository: DropRepository
) : ViewModel() {

    private val _drops = MutableStateFlow<List<Drop>>(emptyList())
    val drops: StateFlow<List<Drop>> get() = _drops

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

    fun getDropsByUsername(username: String): StateFlow<List<Drop>> {
        viewModelScope.launch {
            val dropsList = dropRepository.getDropsByUsername(username)
            _drops.emit(dropsList)
        }
        return _drops
    }

}