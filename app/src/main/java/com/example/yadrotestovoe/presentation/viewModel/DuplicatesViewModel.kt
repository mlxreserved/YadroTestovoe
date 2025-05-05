package com.example.yadrotestovoe.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadrotestovoe.domain.usecase.inter.BindServiceUseCase
import com.example.yadrotestovoe.domain.usecase.inter.DeleteDuplicateContactsUseCase
import com.example.yadrotestovoe.domain.usecase.inter.UnbindServiceUseCase
import com.example.yadrotestovoe.presentation.state.DuplicateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuplicatesViewModel @Inject constructor(
    private val deleteDuplicateContactsUseCase: DeleteDuplicateContactsUseCase,
    private val bindServiceUseCase: BindServiceUseCase,
    private val unbindServiceUseCase: UnbindServiceUseCase
): ViewModel() {

    private val _deleteStatus = MutableStateFlow<DuplicateState?>(null)
    val deleteStatus: StateFlow<DuplicateState?> = _deleteStatus.asStateFlow()


    fun bindService() {
        viewModelScope.launch {
            bindServiceUseCase()
        }
    }

    fun deleteDuplicates() {
        viewModelScope.launch {
            try {
                val result = deleteDuplicateContactsUseCase()
                val resultCode = result.getOrDefault(0)
                if(resultCode == 0){
                    _deleteStatus.update { DuplicateState.Empty(resultCode) }
                } else {
                    _deleteStatus.update { DuplicateState.Success(resultCode) }
                }
            } catch (e: Exception) {
                _deleteStatus.update { DuplicateState.Error(e.message) }
            }
        }
    }

    fun clearDeleteStatus() {
        _deleteStatus.update { null }
    }

    override fun onCleared() {
        super.onCleared()
        unbindServiceUseCase()
    }
}