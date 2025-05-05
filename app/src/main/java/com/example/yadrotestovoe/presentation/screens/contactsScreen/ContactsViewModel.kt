package com.example.yadrotestovoe.presentation.screens.contactsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.usecase.GetGroupedContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getGroupedContactsUseCase: GetGroupedContactsUseCase
) : ViewModel() {

    // Инкапсуляция состояния приложения
    private val _contactsState: MutableStateFlow<ContactsScreenState> =
        MutableStateFlow(ContactsScreenState.Loading)
    // Доступное к чтению состояние приложения
    val contactsState: StateFlow<ContactsScreenState> = _contactsState.asStateFlow()

    init {
        loadContacts()
    }

    // Загрузка контактов
    private fun loadContacts() {
        viewModelScope.launch {
            _contactsState.update { ContactsScreenState.Loading }
            try {
                val localContacts = getGroupedContactsUseCase.getGroupedContacts()

                if (localContacts.isEmpty()) {
                    _contactsState.update { ContactsScreenState.Empty }
                } else {
                    _contactsState.update { ContactsScreenState.Success(
                        contacts = localContacts,
                        lastSelectedContact = null
                    ) }
                }

            } catch (e: Exception) {
                _contactsState.update {
                    ContactsScreenState.Error(
                        e.message ?: ""
                    )
                }
            }
        }
    }

    fun setLastSelectedContact(contact: Contact?) {
        _contactsState.update { old ->
            (old as? ContactsScreenState.Success)?.copy(
                lastSelectedContact = contact
            ) ?: old
        }
    }

}