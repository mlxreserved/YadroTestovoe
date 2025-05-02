package com.example.yadrotestovoe.presentation.screens.contactsScreen

import androidx.lifecycle.ViewModel
import com.example.yadrotestovoe.domain.usecase.GetLocalContactsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val getLocalContactsUseCase: GetLocalContactsUseCase
) : ViewModel() {

    private val _contactsState: MutableStateFlow<ContactsScreenState> =
        MutableStateFlow<ContactsScreenState>(ContactsScreenState.Loading)
    val contactsState: StateFlow<ContactsScreenState> = _contactsState.asStateFlow()

    init {

    }

    fun fetchContacts() {
        _contactsState.update { ContactsScreenState.Loading }
        try {
            val localContacts = getLocalContactsUseCase()
            if(localContacts.isEmpty()) {
                _contactsState.update { ContactsScreenState.Empty }
            }
            else {
                _contactsState.update { ContactsScreenState.Success(localContacts) }
            }
        } catch (e: Exception) {
            _contactsState.update { ContactsScreenState.Error(e.message.toString()) }
        }

    }

}