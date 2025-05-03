package com.example.yadrotestovoe.presentation.screens.contactsScreen

import com.example.yadrotestovoe.domain.model.Contact

sealed interface ContactsScreenState {
    data class Success(val contacts: Map<Char, List<Contact>>) : ContactsScreenState
    data object Loading: ContactsScreenState
    data object Empty: ContactsScreenState
    data class Error(val errorMessage: String) : ContactsScreenState
}