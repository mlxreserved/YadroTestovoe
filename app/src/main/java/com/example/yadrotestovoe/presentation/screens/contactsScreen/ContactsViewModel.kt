package com.example.yadrotestovoe.presentation.screens.contactsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.usecase.GetLocalContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getLocalContactsUseCase: GetLocalContactsUseCase
) : ViewModel() {

    // Инкапсуляция состояния приложения
    private val _contactsState: MutableStateFlow<ContactsScreenState> =
        MutableStateFlow(ContactsScreenState.Loading)
    // Доступное к чтению состояние приложения
    val contactsState: StateFlow<ContactsScreenState> = _contactsState.asStateFlow()

    var lastSelectedContact: Contact? = null
        private set

    init {
        loadContacts()
    }

    // Загрузка контактов
    private fun loadContacts() {
        viewModelScope.launch {
            _contactsState.update { ContactsScreenState.Loading }
            try {
                val localContacts = getLocalContactsUseCase()

                if (localContacts.isEmpty()) {
                    _contactsState.update { ContactsScreenState.Empty }
                } else {
                    val groupedContacts: Map<Char, List<Contact>> = localContacts
                        .groupBy { it.name?.firstOrNull()?.uppercaseChar() ?: SIGN_UNNAMED_CONTACT }
                        .toSortedMap()
                    _contactsState.update { ContactsScreenState.Success(groupedContacts) }
                }

            } catch (e: Exception) {
                _contactsState.update {
                    ContactsScreenState.Error(
                        e.message ?: UNKNOWN_ERROR
                    )
                }
            }
        }
    }

    fun setLastSelectedContact(contact: Contact?) {
        lastSelectedContact = contact
    }

    companion object {
        // Константа для неизвестной ошибки
        private const val UNKNOWN_ERROR = "Неизвестная ошибка"
        // Константа знака для контактов, у которых нет имени
        private const val SIGN_UNNAMED_CONTACT = '#'
    }

}