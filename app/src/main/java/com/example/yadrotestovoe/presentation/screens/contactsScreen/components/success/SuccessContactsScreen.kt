package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.yadrotestovoe.R
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.presentation.screens.contactsScreen.ContactsScreenState
import com.example.yadrotestovoe.presentation.screens.contactsScreen.ContactsViewModel


@Composable
fun SuccessContactsScreen(
    state: ContactsScreenState.Success,
    contactsViewModel: ContactsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val errorNumber = stringResource(R.string.error_empty_number)
    val errorCallPermission = stringResource(R.string.error_call_permission)
    val groupedContacts = state.contacts
    val lastPhoneNumber = state.lastSelectedContact?.phoneNumber

    // Разрешение на звонки
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && lastPhoneNumber != null) {
            startCall(context, lastPhoneNumber)
            contactsViewModel.setLastSelectedContact(null)
        } else if (!isGranted) {
            Toast.makeText(context, errorCallPermission, Toast.LENGTH_SHORT).show()
        }
    }

    // Отображение списка контактов
    LazyColumn {
        groupedContacts.forEach { (initial, contactsInGroup) ->
            item {
                ContactFirstLetter(initial = initial)
            }
            items(contactsInGroup) { contact ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .clickable {
                            onContactPressed(
                                contactsViewModel = contactsViewModel,
                                context = context,
                                contact = contact,
                                requestPermissionLauncher = requestPermissionLauncher,
                                error = errorNumber
                            )
                        }
                        .border(BorderStroke(1.dp, Color.Blue))
                ) {
                    ContactThumbnail(thumbnailUri = contact.thumbnailUri)
                    ContactInfo(contact = contact)
                }
            }
        }
    }
}


// Создание намерения и запуск звонка
private fun startCall(context: Context, phoneNumber: String) {
    val callIntent = Intent(Intent.ACTION_CALL).apply {
        data = ("tel:$phoneNumber").toUri()
    }
    context.startActivity(callIntent)
}

// Реакция на нажатие кнопки
fun onContactPressed(
    contactsViewModel: ContactsViewModel,
    context: Context,
    contact: Contact,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    error: String
) {
    contactsViewModel.setLastSelectedContact(contact)
    Log.d("Permissions", contact.phoneNumber.toString())

    // Если номер не пустой, то запрашиваем разрешение на звонок
    if (contact.phoneNumber != null) {
        if (ContextCompat.checkSelfPermission(
                context,
                CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Разрешение есть — звоним
            startCall(context, contact.phoneNumber)
            contactsViewModel.setLastSelectedContact(null)
        } else {
            // Нет разрешения — запрашиваем
            requestPermissionLauncher.launch(CALL_PHONE)
        }
    } else {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}