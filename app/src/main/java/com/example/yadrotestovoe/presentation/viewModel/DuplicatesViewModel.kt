package com.example.yadrotestovoe.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadrotestovoe.domain.usecase.inter.BindServiceUseCase
import com.example.yadrotestovoe.domain.usecase.inter.DeleteDuplicateContactsUseCase
import com.example.yadrotestovoe.domain.usecase.inter.UnbindServiceUseCase
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

    private val _deleteStatus = MutableStateFlow<String?>(null)
    val deleteStatus: StateFlow<String?> = _deleteStatus.asStateFlow()

    init {
        viewModelScope.launch {
            bindServiceUseCase()
        }
    }

    fun deleteDuplicates() {
        viewModelScope.launch {
            try {
                val result = deleteDuplicateContactsUseCase()
                val resultString = result.getOrNull()
                _deleteStatus.update { resultString }
            } catch (e: Exception) {
                _deleteStatus.update { "Error deleting duplicates" }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        unbindServiceUseCase()
    }
}