package com.example.yadrotestovoe.presentation.screens.contactsScreen

import com.example.yadrotestovoe.domain.model.Contact

/*
    Состояния приложения
    Success - удачное получение контактов
    Loading - загрузка контактов
    Empty - пустой список контактов
    Error - произошла ошибка при получении контактов
 */
sealed interface ContactsScreenState {
    data class Success(
        val contacts: Map<Char, List<Contact>>,
        val lastSelectedContact: Contact?
    ) : ContactsScreenState
    data object Loading: ContactsScreenState
    data object Empty: ContactsScreenState
    data class Error(val errorMessage: String) : ContactsScreenState
}