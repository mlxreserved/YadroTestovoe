package com.example.yadrotestovoe.presentation.screens.contactsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aidl.DeleteDuplicateContacts
import com.example.yadrotestovoe.presentation.screens.contactsScreen.components.empty.EmptyContactsScreen
import com.example.yadrotestovoe.presentation.screens.contactsScreen.components.error.ErrorContactsScreen
import com.example.yadrotestovoe.presentation.screens.contactsScreen.components.loading.LoadingContactsScreen
import com.example.yadrotestovoe.presentation.screens.contactsScreen.components.success.SuccessContactsScreen

@Composable
fun ContactsListScreen(
    deleteDuplicateContacts: DeleteDuplicateContacts?,
    modifier: Modifier = Modifier,
    contactsViewModel: ContactsViewModel = hiltViewModel()
) {
    val contactsState by contactsViewModel.contactsState.collectAsState()

    Box(modifier = modifier) {
         when (val state = contactsState) {
            ContactsScreenState.Empty -> { EmptyContactsScreen() }
            is ContactsScreenState.Error -> { ErrorContactsScreen() }
            ContactsScreenState.Loading -> { LoadingContactsScreen() }

            is ContactsScreenState.Success -> SuccessContactsScreen(
                deleteDuplicateContacts = deleteDuplicateContacts,
                groupedContacts = state.contacts,
                contactsViewModel = contactsViewModel
            )
        }
    }

}
