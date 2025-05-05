package com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yadrotestovoe.R
import com.example.yadrotestovoe.domain.model.Contact
import com.example.yadrotestovoe.presentation.state.DuplicateState
import com.example.yadrotestovoe.presentation.viewModel.ContactsViewModel
import com.example.yadrotestovoe.presentation.viewModel.DuplicatesViewModel


@Composable
fun SuccessContactsScreen(
    duplicatesViewModel: DuplicatesViewModel,
    groupedContacts: Map<Char, List<Contact>>,
    contactsViewModel: ContactsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val errorNumber = stringResource(R.string.error_empty_number)
    val errorCallPermission = stringResource(R.string.error_call_permission)
    val deleteStatus by duplicatesViewModel.deleteStatus.collectAsState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Обработка результата через ViewModel
        contactsViewModel.handlePermissionResult(context, errorCallPermission, isGranted)
    }

    when(deleteStatus) {
        is DuplicateState.Success -> {
            Toast.makeText(context, stringResource(R.string.success_delete_duplicate), Toast.LENGTH_SHORT).show()
            contactsViewModel.loadContacts()
            duplicatesViewModel.clearDeleteStatus()
        }
        is DuplicateState.Empty -> {
            Toast.makeText(context, stringResource(R.string.empty_delete_duplicate), Toast.LENGTH_SHORT).show()
            duplicatesViewModel.clearDeleteStatus()
        }
        is DuplicateState.Error -> {
            Toast.makeText(context, stringResource(R.string.error_delete_duplicate), Toast.LENGTH_SHORT).show()
            duplicatesViewModel.clearDeleteStatus()
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        // Отображение списка контактов
        LazyColumn (
            modifier = Modifier
                .weight(1f)
        ) {
            groupedContacts.forEach { (initial, contactsInGroup) ->
                item {
                    ContactFirstLetter(initial = initial)
                }
                items(contactsInGroup) { contact ->
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .clickable {
                                contactsViewModel.onContactPressed(
                                    context = context,
                                    contact = contact,
                                    requestPermissionLauncher,
                                    error = errorNumber
                                )
                            }
                            .border(BorderStroke(1.dp, Color.Blue))
                    ) {
                        ContactThumbnail(thumbnailUri = contact.thumbnailUri)
                        ContactInfo(context = context, contact = contact)
                    }
                }
            }
        }
        Button(
            onClick = { duplicatesViewModel.deleteDuplicates() }
        ){
            Text(text = stringResource(R.string.button_delete_duplicate))
        }
    }
}
