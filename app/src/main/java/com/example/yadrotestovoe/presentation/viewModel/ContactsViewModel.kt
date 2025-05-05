package com.example.yadrotestovoe.presentation.viewModel

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.domain.usecase.inter.GetLocalContactsUseCase
import com.example.yadrotestovoe.presentation.state.ContactsScreenState
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

    private var lastSelectedContact: Contact? = null

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

    // Реакция на нажатие кнопки
    fun onContactPressed(
        context: Context,
        contact: Contact,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        error: String
    ) {
        lastSelectedContact = contact

        // Если номер не пустой, то запрашиваем разрешение на звонок
        if (contact.phoneNumber != null) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Разрешение есть — звоним
                startCall(context, contact.phoneNumber)
            } else {
                // Нет разрешения — запрашиваем
                requestPermissionLauncher.launch(CALL_PHONE)
            }
        } else {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

    }

    // Обработка результата запроса разрешения
    fun handlePermissionResult(context: Context, error: String, isGranted: Boolean) {
        if (isGranted && lastSelectedContact?.phoneNumber != null) {
            startCall(context, lastSelectedContact!!.phoneNumber!!)
            lastSelectedContact = null
        } else if(!isGranted) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }


    // Создание намерения и запуск звонка
    private fun startCall(context: Context, phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = ("tel:$phoneNumber").toUri()
        }
        context.startActivity(callIntent)
    }

    companion object {
        // Константа для неизвестной ошибки
        private const val UNKNOWN_ERROR = "Неизвестная ошибка"
        // Константа знака для контактов, у которых нет имени
        private const val SIGN_UNNAMED_CONTACT = '#'
    }

}