package com.example.yadrotestovoe.presentation.screens.contactsScreen

import android.content.Context
import android.widget.Toast
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

    fun deleteDuplicates(context: Context) {
        viewModelScope.launch {
            try {
                val result = deleteDuplicateContactsUseCase()
                val resultString = result.getOrNull()
                _deleteStatus.update { resultString }
                Toast.makeText(context, _deleteStatus, )
            } catch (e: Exception) {
                _deleteStatus.update { "Error deleting duplicates" }
            }
        }
    }

}